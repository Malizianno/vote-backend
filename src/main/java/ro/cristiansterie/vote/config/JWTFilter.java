package ro.cristiansterie.vote.config;

import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
import ro.cristiansterie.vote.service.UserService;
import ro.cristiansterie.vote.util.JWTTokenAuthentication;
import ro.cristiansterie.vote.util.JWTUtils;
import ro.cristiansterie.vote.util.UserRoleEnum;

public class JWTFilter extends OncePerRequestFilter {
    
    protected static final String[] ALLOW_PATTERNS = new String[] { "/login/**", "/index.html" };
    protected static final Logger log = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @Autowired
    private JWTUtils jwtUtil;

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
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            if (!requestMatcher.matches(request)) {
                log.info("Requested URI: {}", request.getRequestURI());

                String jwt = parseJWTToken(request);

                // log.warn("Requested authentication with token: {}", jwt);

                if (null != jwt && jwtUtil.validateJWTToken(jwt)) {
                    UserDetails userDetails = userService.loadUserByUsername(jwtUtil.parseUsername(jwt));
                    UserRoleEnum role = UserRoleEnum.valueOf(userDetails.getAuthorities().toArray()[0].toString());

                    Authentication auth = new JWTTokenAuthentication(jwt, userDetails.getUsername(), role,
                            userDetails.getAuthorities());
                    auth.setAuthenticated(true);

                    SecurityContextHolder.getContext().setAuthentication(auth);
                    log.info("Authentication successfull for: {} for {} area!", userDetails.getUsername(),
                            role.toString().toUpperCase());
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

    private String parsePrincipal(HttpServletRequest request) {
        String principal = request.getHeader("Principal");

        if (StringUtils.hasText(principal)) {
            return principal;
        }

        return null;
    }
}
