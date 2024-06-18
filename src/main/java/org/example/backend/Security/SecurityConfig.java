package org.example.backend.Security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final CustomUserDetailsService userDetailsService;

    public SecurityConfig(CustomUserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return userDetailsService;
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeRequests(authorizeRequests ->
                        authorizeRequests
                                .requestMatchers(
                                        new AntPathRequestMatcher("/signup"),
                                        new AntPathRequestMatcher("/login")
                                ).permitAll() // Allow access to /login and /register for all users
                                .anyRequest().authenticated() // Require authentication for all other requests
                )
                .formLogin(formLogin ->
                        formLogin
                                .loginPage("/login") // Custom login page URL
                                .defaultSuccessUrl("/chat/chatroom") // Redirect to /chat after successful login
                                .permitAll() // Allow access to the login page for all users
                )
                .logout(logout ->
                        logout
                                .logoutUrl("/logout") // Logout URL
                                .logoutSuccessUrl("/login") // Redirect to /login after logout
                                .permitAll() // Allow access to the logout URL for all users
                )
                .build();
    }
}