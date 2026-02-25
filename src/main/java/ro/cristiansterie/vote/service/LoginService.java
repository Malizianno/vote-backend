package ro.cristiansterie.vote.service;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import ro.cristiansterie.vote.aspect.Loggable;
import ro.cristiansterie.vote.config.CustomAuthenticationManager;
import ro.cristiansterie.vote.config.FaceIDAuthentication;
import ro.cristiansterie.vote.dto.LoginRequestDTO;
import ro.cristiansterie.vote.dto.LoginResponseDTO;
import ro.cristiansterie.vote.dto.LogoutRequestDTO;
import ro.cristiansterie.vote.dto.LogoutResponseDTO;
import ro.cristiansterie.vote.dto.UserVoterDTO;
import ro.cristiansterie.vote.util.AppConstants;
import ro.cristiansterie.vote.util.JWTUtils;
import ro.cristiansterie.vote.util.UserRoleEnum;

@Service
public class LoginService extends GenericService {
    private final CustomAuthenticationManager authManager;
    private final JWTUtils jwtUtil;
    private final UserService userService;

    public LoginService(CustomAuthenticationManager authManager, JWTUtils jwtUtil, UserService userService) {
        this.authManager = authManager;
        this.jwtUtil = jwtUtil;
        this.userService = userService;
    }

    @Loggable(action = AppConstants.EVENT_ACTION_LOGIN, screen = AppConstants.EVENT_SCREEN_LOGIN, message = AppConstants.EVENT_LOGIN_AUTHENTICATED)
    public LoginResponseDTO login(LoginRequestDTO request) {
        try {
            UserDetails foundUser = userService.loadUserByUsername(request.getUsername());

            if (null != request && null != request.getRole() && UserRoleEnum.ADMIN.equals(request.getRole())
                    && foundUser.getAuthorities().size() == 1
                    && foundUser.getAuthorities().contains(new SimpleGrantedAuthority(UserRoleEnum.ADMIN.name()))) {
                Authentication auth = authManager.authenticate(
                        new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));

                SecurityContextHolder.getContext().setAuthentication(auth);
                log.info("Authentication successful for ADMIN username: {}", request.getUsername());

                String token = jwtUtil.generateJWTToken(auth);

                LoginResponseDTO response = new LoginResponseDTO();

                response.setUsername(request.getUsername());
                response.setRole(request.getRole());
                response.setToken(token);

                return response;
            }
        } catch (Exception ex) {
            log.error("Exception happened while logging in: {}", ex);
        }

        return null;
    }

    @Loggable(action = AppConstants.EVENT_ACTION_LOGIN, screen = AppConstants.EVENT_SCREEN_LOGIN, message = AppConstants.EVENT_LOGIN_AUTHENTICATED_VOTER)
    public String loginUserWithFace(UserVoterDTO user) {
        try {
            UserVoterDTO foundUser = userService.getVoter(user.getId());
            if (null != user && null != user.getRole() && UserRoleEnum.VOTANT.equals(user.getRole())
                    && foundUser.getRole() != null
                    && foundUser.getRole().compareTo(UserRoleEnum.VOTANT) == 0) {
                List<SimpleGrantedAuthority> authorities = new ArrayList<>();
                authorities.add(new SimpleGrantedAuthority(UserRoleEnum.VOTANT.name()));

                Authentication auth = authManager.authenticate(new FaceIDAuthentication(foundUser.getId(),
                        Base64.getEncoder().encodeToString(foundUser.getFaceImage()), authorities));

                SecurityContextHolder.getContext().setAuthentication(auth);
                log.info("Authentication successful for VOTANT id: {}", user.getId());

                String token = jwtUtil.generateJWTToken(auth);

                return token;
            }
        } catch (Exception ex) {
            log.error("Exception happened while logging in: {}", ex);
        }

        return null;
    }

    @Loggable(action = AppConstants.EVENT_ACTION_LOGOUT, screen = AppConstants.EVENT_SCREEN_LOGIN, message = AppConstants.EVENT_LOGIN_LOGOUT)
    public LogoutResponseDTO logout(LogoutRequestDTO request) {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (null != auth && auth.isAuthenticated()) {
                String username = (String) auth.getPrincipal();

                if (!username.isEmpty() && !request.getUsername().isEmpty()) {
                    log.info("Logout successfull for username: {}", username);

                    SecurityContextHolder.getContext().setAuthentication(null);
                }

                LogoutResponseDTO response = new LogoutResponseDTO();

                response.setUsername(request.getUsername());
                response.setResponse(auth.isAuthenticated());

                return response;
            }
        } catch (Exception e) {
            log.info("Exception happened while logging out: {}", e);
        }

        return null;
    }
}
