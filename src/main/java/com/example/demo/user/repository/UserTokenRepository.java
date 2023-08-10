package com.example.demo.user.repository;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class UserTokenRepository {
    static final Map<String, String> userTokenMap = new HashMap<>();

    public void setUserToken(String userToken, String email) {
        userTokenMap.put(userToken, email);
    }

    public void getIdByUserToken(String userToken) {
        userTokenMap.get(userToken);
    }
}
