package com.example.demo.security.utils;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class UserTokenUtils {
    @Value("${uri.exclude.tokenCheckFilter}")
    private List<String> tokenCheckFilterExcludeUris;
    public boolean isTokenCheckFilterExcludeUris(String uri) {
        for(String tokenCheckFilterExcludeUri : tokenCheckFilterExcludeUris) {
            if(uri.startsWith(tokenCheckFilterExcludeUri)) {
                return true;
            }
        }
        return false;
    }

}
