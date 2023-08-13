package com.example.demo.security.filter;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.json.GsonJsonParser;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;

import java.io.IOException;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
public class UserTokenLoginFilter extends AbstractAuthenticationProcessingFilter {
    public UserTokenLoginFilter(String defaultFilterProcessesUrl) {
        super(defaultFilterProcessesUrl);
    }
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
        log.info("UserTokenLoginFilter");
        Map<String, Object> parseRequestToMap = parseJson(request);
        String username = (String) parseRequestToMap.get("email");
        String password = (String) parseRequestToMap.get("pw");

        UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(username, password);
        return super.getAuthenticationManager().authenticate(authRequest);
    }

    private Map<String, Object> parseJson(HttpServletRequest request) throws IOException {
        String body = request.getReader().lines().collect(Collectors.joining());
        GsonJsonParser gsonJsonParser = new GsonJsonParser();
        return gsonJsonParser.parseMap(body);
    }
}
