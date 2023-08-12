package com.example.demo.config.security.filter;

import com.example.demo.config.security.UserTokenUtils;
import com.example.demo.security.costomUser.CustomUserDetailsService;
import com.example.demo.security.exception.AccesstokenException;
import com.example.demo.user.repository.UserTokenRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class UserTokenCheckFilter extends OncePerRequestFilter {
    final UserTokenRepository userTokenRepository;
    final CustomUserDetailsService customUserDetailsService;
    final UserTokenUtils userTokenUtils;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        log.info("userTokenCheckFilter");
        String method = request.getMethod();
        String uri = request.getRequestURI();
        if(userTokenUtils.isTokenCheckFilterExcludeUris(uri)){
            filterChain.doFilter(request, response);
            return;
        }
        try {
            String userToken = request.getHeader("userToken");
            String username = userTokenRepository.getIdByUserToken(userToken);
            UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            filterChain.doFilter(request, response);
        } catch (AccesstokenException e) {
            e.sendResponseError(response);
        }
    }
}
