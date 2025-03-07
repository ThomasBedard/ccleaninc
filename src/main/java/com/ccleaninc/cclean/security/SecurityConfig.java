package com.ccleaninc.cclean.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain configure(HttpSecurity http) throws Exception {
        return http
                // Disable CSRF for stateless APIs
                .csrf(csrf -> csrf.disable())

                // Let all requests pass freely (for dev/demo)
                .authorizeHttpRequests(authz -> authz.anyRequest().permitAll())

                // Allow CORS
                .cors(Customizer.withDefaults())

                // OAuth2 resource server with JWT
                .oauth2ResourceServer(oauth2 ->
                        oauth2.jwt(jwt -> jwt.jwtAuthenticationConverter(
                                CustomJwtAuthenticationConverter.jwtAuthenticationConverter()
                        ))
                )
                .build();
    }
}

