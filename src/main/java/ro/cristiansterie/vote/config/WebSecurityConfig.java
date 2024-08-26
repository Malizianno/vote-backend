package ro.cristiansterie.vote.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer.FrameOptionsConfig;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Configuration
@EnableWebMvc
@EnableWebSecurity
public class WebSecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, AuthEntryPointJwt unauthorizedHandler,
            LocalLogoutSuccessHandler logoutSuccessHandler) throws Exception {
        String DEFAULT_CHECK_HEALTH_URL = "/actuator";
        String DEFAULT_H2_ENDPOINT = "/h2/**";
        String DEFAULT_LOGIN_ENDPOINT = "/login/**";
        String DEFAULT_SINGUP_ENDPOINT = "/users/save/**";

        String[] AUTH_WHITELIST = {
                "/v3/api-docs/**",
                "/docs/ui/**",
                "/docs/swagger-ui/**",
                DEFAULT_CHECK_HEALTH_URL,
                DEFAULT_H2_ENDPOINT,
                DEFAULT_LOGIN_ENDPOINT,
                DEFAULT_SINGUP_ENDPOINT
        };

        http.authorizeHttpRequests(
                auth -> auth.requestMatchers(AUTH_WHITELIST).permitAll().anyRequest().authenticated())
                // .cors(cors -> cors.disable())
                .csrf(csrf -> csrf.disable())
                .exceptionHandling(ex -> ex.authenticationEntryPoint(unauthorizedHandler))
                .headers(headers -> headers.frameOptions(FrameOptionsConfig::sameOrigin))
                .logout(logout -> logout.logoutSuccessHandler(logoutSuccessHandler));

        http.addFilterBefore(jwtFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public JWTFilter jwtFilter() {
        return new JWTFilter();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(10);
    }
}
