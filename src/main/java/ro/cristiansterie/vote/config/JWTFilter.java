package ro.cristiansterie.vote.config;

import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ro.cristiansterie.vote.properties.AuthProperties;
import ro.cristiansterie.vote.service.UserService;
import ro.cristiansterie.vote.util.AppConstants;
import ro.cristiansterie.vote.util.JWTTokenAuthentication;
import ro.cristiansterie.vote.util.JWTUtils;
import ro.cristiansterie.vote.util.UserRoleEnum;

public class JWTFilter extends OncePerRequestFilter {

    protected static final String[] ALLOW_PATTERNS = new String[] { "/login/**", "/users/save/**", "/index.html" };
    protected static final Logger log = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @Autowired
    private JWTUtils jwtUtil;

    @Autowired
    private AuthProperties authProperties;

    @Autowired
    private UserService userService;

    private RequestMatcher requestMatcher;

    public JWTFilter() {
        List<RequestMatcher> matchers = new ArrayList<>();

        for (String pattern : ALLOW_PATTERNS) {
            matchers.add(new AntPathRequestMatcher(pattern));
        }

        this.requestMatcher = new OrRequestMatcher(matchers);
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain)
            throws ServletException, IOException {
        try {
            if (!requestMatcher.matches(request)) {
                String apikey = request.getHeader(AppConstants.AUTH_APIKEY);

                log.info("Requested URI: {}", request.getRequestURI());
                log.info("Requested with apikey: {}", apikey);

                String jwt = parseJWTToken(request);

                if (null != jwt && jwtUtil.validateJWTToken(jwt) && apikey.equals(authProperties.getFrontend())) {
                    log.info("validating ADMIN user...");

                    UserDetails userDetails = userService.loadUserByUsername(jwtUtil.parseUsername(jwt));
                    UserRoleEnum role = UserRoleEnum.valueOf(userDetails.getAuthorities().toArray()[0].toString());

                    Authentication auth = new JWTTokenAuthentication(jwt, userDetails.getUsername(), role,
                            userDetails.getAuthorities());
                    auth.setAuthenticated(true);

                    SecurityContextHolder.getContext().setAuthentication(auth);
                    log.info("Authentication successfull for user: {}, apikey: {}!", userDetails.getUsername(), apikey);
                }

                if (null != jwt && jwtUtil.validateJWTToken(jwt) && apikey.equals(authProperties.getMobile())) {
                    log.info("validating VOTANT user...");

                    UserDetails userDetails = userService.loadUserByUsername(jwtUtil.parseUsername(jwt));
                    UserRoleEnum role = UserRoleEnum.valueOf(userDetails.getAuthorities().toArray()[0].toString());

                    Authentication auth = new JWTTokenAuthentication(jwt, userDetails.getUsername(), role,
                            userDetails.getAuthorities());
                    auth.setAuthenticated(true);

                    SecurityContextHolder.getContext().setAuthentication(auth);
                    log.info("Authentication successfull for user: {}, apikey: {}!", userDetails.getUsername(), apikey);
                }
            }
        } catch (Exception e) {
            log.error("Cannot set user authentication: {}", e.getMessage());
        }

        filterChain.doFilter(request, response);
    }

    private String parseJWTToken(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");

        if (StringUtils.hasText(authHeader) && authHeader.equals("Bearer null")) {
            return null;
        }

        if (StringUtils.hasText(authHeader) && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7, authHeader.length());
        }

        return null;
    }

    @SuppressWarnings("unused")
    private String parsePrincipal(HttpServletRequest request) {
        String principal = request.getHeader("Principal");

        if (StringUtils.hasText(principal)) {
            return principal;
        }

        return null;
    }
}
