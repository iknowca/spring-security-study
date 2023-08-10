package com.example.demo.config.security;

import com.example.demo.config.security.filter.UserTokenLoginFilter;
import com.example.demo.security.costomUser.CustomUserDetailsService;
import com.example.demo.security.handler.login.UserTokenLoginFailureHandler;
import com.example.demo.security.handler.login.UserTokenLoginSuccessHandler;
import com.example.demo.user.repository.UserRepository;
import com.example.demo.user.repository.UserTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    final CustomUserDetailsService customUserDetailsService;
    final UserTokenRepository userTokenRepository;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable);

        AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.userDetailsService(customUserDetailsService).passwordEncoder(passwordEncoder());
        AuthenticationManager authenticationManager = authenticationManagerBuilder.build();
        http.authenticationManager(authenticationManager);

        UserTokenLoginFilter userTokenLoginFilter = new UserTokenLoginFilter("/user/sign-in");
        userTokenLoginFilter.setAuthenticationManager(authenticationManager);
        userTokenLoginFilter.setAuthenticationSuccessHandler(new UserTokenLoginSuccessHandler(userTokenRepository));
        userTokenLoginFilter.setAuthenticationFailureHandler(new UserTokenLoginFailureHandler());

        http.addFilterBefore(userTokenLoginFilter, UsernamePasswordAuthenticationFilter.class);

        return http
                .authorizeHttpRequests((authorizeRequests)-> {
                    authorizeRequests.anyRequest().permitAll();
                })
                .build();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
