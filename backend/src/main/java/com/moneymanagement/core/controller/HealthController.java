package com.moneymanagement.core.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthController {

    @CrossOrigin(origins = "http://localhost:8080")
    @GetMapping("/health")
    public String health() {
        return "OK";
    }
}
