package com.meet.meet.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private CustomUserDetailService userDetailService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(
                authorize -> authorize
                            .requestMatchers("/login", "/register", "/groups", "/css/**", "/js/**")
                            .permitAll()
                            .anyRequest()
                            .authenticated()
            )
            .formLogin(form -> form.loginPage("/login")
                                    .defaultSuccessUrl("/groups")
                                    .loginProcessingUrl("/login")
                                    .failureUrl("/login?fail=true")
                                    .permitAll()
            ).rememberMe(
                rememberMe -> rememberMe.rememberMeParameter("remember-me").tokenValiditySeconds(100)
            )
            .logout(logout -> logout.logoutRequestMatcher(
                new AntPathRequestMatcher("/logout")
            ).permitAll());

        return http.build();
    }

    @Bean
    public static PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    public void configure(AuthenticationManagerBuilder builder) throws Exception {
        builder.userDetailsService(userDetailService).passwordEncoder(passwordEncoder());
    }
}
