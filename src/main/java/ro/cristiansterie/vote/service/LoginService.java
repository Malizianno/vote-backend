package ro.cristiansterie.vote.service;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import ro.cristiansterie.vote.config.CustomAuthenticationManager;
import ro.cristiansterie.vote.config.FaceIDAuthentication;
import ro.cristiansterie.vote.dto.LoginRequestDTO;
import ro.cristiansterie.vote.dto.LoginResponseDTO;
import ro.cristiansterie.vote.dto.LogoutRequestDTO;
import ro.cristiansterie.vote.dto.LogoutResponseDTO;
import ro.cristiansterie.vote.dto.UserDTO;
import ro.cristiansterie.vote.dto.UserVoterDTO;
import ro.cristiansterie.vote.util.AppConstants;
import ro.cristiansterie.vote.util.EventActionEnum;
import ro.cristiansterie.vote.util.EventScreenEnum;
import ro.cristiansterie.vote.util.JWTUtils;
import ro.cristiansterie.vote.util.UserRoleEnum;

@Service
public class LoginService {
    protected static final Logger log = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private CustomAuthenticationManager authManager;
    private JWTUtils jwtUtil;
    private UserService userService;
    private final EventService events;

    public LoginService(CustomAuthenticationManager authManager, JWTUtils jwtUtil, UserService userService,
            EventService events) {
        this.authManager = authManager;
        this.jwtUtil = jwtUtil;
        this.userService = userService;
        this.events = events;
    }

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

                // save event
                events.save(EventActionEnum.LOGIN, EventScreenEnum.LOGIN,
                        AppConstants.EVENT_LOGIN_AUTHENTICATED + request.getUsername());

                return response;
                }
        } catch (Exception ex) {
            log.error("Exception happened while logging in: {}", ex);
        }

        return null;
    }

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

                // save event
                events.save(EventActionEnum.LOGIN, EventScreenEnum.LOGIN,
                        AppConstants.EVENT_LOGIN_AUTHENTICATED_VOTER + user.getId());

                return token;
            }
        } catch (Exception ex) {
            log.error("Exception happened while logging in: {}", ex);
        }

        return null;
    }

    public LogoutResponseDTO logout(LogoutRequestDTO request) {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (null != auth && auth.isAuthenticated()) {
                String username = (String) auth.getPrincipal();

                if (!username.isEmpty() && !request.getUsername().isEmpty()) {
                    log.info("Logout successfull for username: {}", username);

                    // save event
                    events.save(EventActionEnum.LOGOUT, EventScreenEnum.LOGIN,
                            AppConstants.EVENT_LOGIN_LOGOUT + request.getUsername());

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
