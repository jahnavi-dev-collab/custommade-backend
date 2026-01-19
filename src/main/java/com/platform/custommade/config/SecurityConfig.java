package com.platform.custommade.config;

import com.platform.custommade.security.JwtAuthFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;

    public SecurityConfig(JwtAuthFilter jwtAuthFilter) {
        this.jwtAuthFilter = jwtAuthFilter;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                //  Disable CSRF (JWT based)
                .csrf(csrf -> csrf.disable())

                //  Disable session creation (STATELESS)
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                //  Authorization rules
                .authorizeHttpRequests(auth -> auth

                        // ✅ PUBLIC ENDPOINTS
                        .requestMatchers(
                                "/api/auth/**",
                                "/api/users/register",
                                "/api/users/health"
                        ).permitAll()

                        // 🔒 ADMIN ONLY
                        .requestMatchers("/api/admin/**").hasRole("ADMIN")

                        // 🔐 EVERYTHING ELSE NEEDS AUTH
                        .anyRequest().authenticated()
                )

                //  JWT FILTER (must be before UsernamePasswordAuthenticationFilter)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
