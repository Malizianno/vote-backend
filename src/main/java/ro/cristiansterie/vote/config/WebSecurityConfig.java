package ro.cristiansterie.vote.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer.FrameOptionsConfig;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

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
        String DEFAULT_REGISTER_ENDPOINT = "/register/**";

        String[] AUTH_WHITELIST = {
                "/v3/api-docs/**",
                "/docs/ui/**",
                "/docs/swagger-ui/**",
                DEFAULT_CHECK_HEALTH_URL,
                DEFAULT_H2_ENDPOINT,
                DEFAULT_LOGIN_ENDPOINT,
                DEFAULT_REGISTER_ENDPOINT
        };

        http.authorizeHttpRequests(
                auth -> auth.requestMatchers(AUTH_WHITELIST).permitAll().anyRequest().authenticated())
                .csrf(csrf -> csrf.disable())
                .cors(cors -> cors.configurationSource(request -> {
                    // Reuse your existing MVC CORS config
                    CorsConfiguration config = new CorsConfiguration();
                    config.setAllowedOrigins(List.of(
                            "https://vote-frontend-t3wz.onrender.com",
                            "https://vote-mobile-1fk7.onrender.com",
                            "http://localhost:4090"));
                    config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
                    config.setAllowedHeaders(List.of("*"));
                    config.setAllowCredentials(true);
                    return config;
                }))
                .exceptionHandling(ex -> ex.authenticationEntryPoint(unauthorizedHandler))
                .headers(headers -> headers.frameOptions(FrameOptionsConfig::sameOrigin))
                .logout(logout -> logout.logoutSuccessHandler(logoutSuccessHandler));

        http.addFilterBefore(jwtFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    // keep this, but might remove it later if we don't need it for the frontend
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(@NonNull CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins("http://localhost:4090") // or your frontend origin
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                        .allowedHeaders("*")
                        .allowCredentials(true);
            }
        };
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
