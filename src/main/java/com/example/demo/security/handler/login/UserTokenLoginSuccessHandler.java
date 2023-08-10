package com.example.demo.security.handler.login;

import com.example.demo.user.repository.UserTokenRepository;
import com.google.gson.Gson;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;
import java.util.UUID;

@RequiredArgsConstructor
public class UserTokenLoginSuccessHandler implements AuthenticationSuccessHandler {
    final UserTokenRepository userTokenRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        String userToken = UUID.randomUUID().toString();

        userTokenRepository.setUserToken(userToken, authentication.getName());

        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        Gson gson = new Gson();
        String jsonStr = gson.toJson(Map.of("userToken", userToken));
        out.write(jsonStr);
        out.flush();
        out.close();
    }
}
