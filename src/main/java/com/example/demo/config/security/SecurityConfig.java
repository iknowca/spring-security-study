package com.example.demo.config.security;

import com.example.demo.security.filter.JwtLoginFilter;
import com.example.demo.security.filter.TokenCheckFilter;
import com.example.demo.security.filter.UserTokenCheckFilter;
import com.example.demo.security.filter.UserTokenLoginFilter;
import com.example.demo.security.costomUser.CustomUserDetailsService;
import com.example.demo.security.handler.JwtLoginFailHandler;
import com.example.demo.security.handler.JwtLoginSuccessHandler;
import com.example.demo.security.handler.login.UserTokenLoginFailureHandler;
import com.example.demo.security.handler.login.UserTokenLoginSuccessHandler;
import com.example.demo.security.utils.JwtUtil;
import com.example.demo.security.utils.UserTokenUtils;
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
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.web.filter.CharacterEncodingFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    final CustomUserDetailsService customUserDetailsService;
    final UserTokenRepository userTokenRepository;
    final UserTokenUtils userTokenUtils;
    final JwtUtil jwtUtil;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//        CharacterEncodingFilter filter = new CharacterEncodingFilter();
//        filter.setEncoding("UTF-8");
//        filter.setForceEncoding(true);
//        http.addFilterBefore(filter, CsrfFilter.class);


        http.csrf(AbstractHttpConfigurer::disable);

        AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.userDetailsService(customUserDetailsService).passwordEncoder(passwordEncoder());
        AuthenticationManager authenticationManager = authenticationManagerBuilder.build();
        http.authenticationManager(authenticationManager);

//        UserTokenLoginFilter userTokenLoginFilter = new UserTokenLoginFilter("/user/sign-in");
//        userTokenLoginFilter.setAuthenticationManager(authenticationManager);
//        userTokenLoginFilter.setAuthenticationSuccessHandler(new UserTokenLoginSuccessHandler(userTokenRepository));
//        userTokenLoginFilter.setAuthenticationFailureHandler(new UserTokenLoginFailureHandler());
        JwtLoginFilter jwtLoginFilter = new JwtLoginFilter("/user/sign-in");
        jwtLoginFilter.setAuthenticationManager(authenticationManager);
        jwtLoginFilter.setAuthenticationSuccessHandler(new JwtLoginSuccessHandler(jwtUtil));
        jwtLoginFilter.setAuthenticationFailureHandler(new JwtLoginFailHandler());

        http.addFilterBefore(jwtLoginFilter, UsernamePasswordAuthenticationFilter.class);

//        UserTokenCheckFilter userTokenCheckFilter = new UserTokenCheckFilter(userTokenRepository, customUserDetailsService, userTokenUtils);
//        http.addFilterBefore(userTokenCheckFilter, UsernamePasswordAuthenticationFilter.class);

        TokenCheckFilter tokenCheckFilter = new TokenCheckFilter(jwtUtil, customUserDetailsService);
        http.addFilterBefore(tokenCheckFilter, UsernamePasswordAuthenticationFilter.class);

        return http
                .authorizeHttpRequests((authorizeRequests) -> {
                    authorizeRequests.requestMatchers("/user/sign-up")
                            .permitAll();
                    authorizeRequests.requestMatchers("/user/{userId}")
                            .hasAnyRole("NORMAL");
                })
                .build();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
