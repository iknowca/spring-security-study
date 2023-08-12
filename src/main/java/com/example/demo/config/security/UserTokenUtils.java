package com.example.demo.config.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

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
