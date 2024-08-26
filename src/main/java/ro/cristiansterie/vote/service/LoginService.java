package ro.cristiansterie.vote.service;

import java.lang.invoke.MethodHandles;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import ro.cristiansterie.vote.config.CustomAuthenticationManager;
import ro.cristiansterie.vote.dto.LoginRequestDTO;
import ro.cristiansterie.vote.dto.LoginResponseDTO;
import ro.cristiansterie.vote.dto.UserDTO;
import ro.cristiansterie.vote.util.JWTUtils;
import ro.cristiansterie.vote.util.UserRoleEnum;

@Service
public class LoginService {
    protected static final Logger log = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private CustomAuthenticationManager authManager;
    private JWTUtils jwtUtil;
    private UserService userService;

    public LoginService(CustomAuthenticationManager authManager, JWTUtils jwtUtil, UserService userService) {
        this.authManager = authManager;
        this.jwtUtil = jwtUtil;
        this.userService = userService;
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
                log.info("Authentication successfull for ADMIN username: {}", request.getUsername());

                String token = jwtUtil.generateJWTToken(auth);

                LoginResponseDTO response = new LoginResponseDTO();

                response.setUsername(request.getUsername());
                response.setRole(request.getRole());
                response.setToken(token);

                return response;
            }

            // WIP: else login a votant user type via biometrics not via user+pass
            if (null != request && null != request.getRole() && UserRoleEnum.VOTANT.equals(request.getRole())
                    && foundUser.getAuthorities().size() == 1
                    && foundUser.getAuthorities().contains(new SimpleGrantedAuthority(UserRoleEnum.VOTANT.name()))) {
                Authentication auth = authManager.authenticate(
                        new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));

                SecurityContextHolder.getContext().setAuthentication(auth);
                log.info("Authentication successfull for VOTANT username: {}", request.getUsername());

                String token = jwtUtil.generateJWTToken(auth);

                LoginResponseDTO response = new LoginResponseDTO();

                response.setUsername(request.getUsername());
                response.setRole(request.getRole());
                response.setToken(token);
                response.setHasVoted(userService.hasVotedByUsername(request.getUsername()));

                UserDTO foundLocalUser = userService.getByUsername(request.getUsername());

                if (null != foundLocalUser && null != foundLocalUser.getId()) {
                    response.setId(foundLocalUser.getId());
                }

                return response;
            }
        } catch (Exception e) {
            log.info("Exception happened while logging in: {}", e);
        }

        return null;
    }
}
