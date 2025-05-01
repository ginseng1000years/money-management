package com.moneymanagement.core.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.Map;
import java.util.HashMap;

@RestController
public class UserController {

    @GetMapping("/login/oauth2/code/google/")
    public Map<String, Object> getUserLoginBaseOnSSO(@AuthenticationPrincipal OAuth2User principal) {
        return principal.getAttributes();
    }

    @GetMapping("/api/userinfo")
    public Map<String, Object> userInfo(@AuthenticationPrincipal OAuth2User principal) {
        // Add token info if available (this is a placeholder, actual token retrieval may differ)
        return new HashMap<>(principal.getAttributes());
    }
}
