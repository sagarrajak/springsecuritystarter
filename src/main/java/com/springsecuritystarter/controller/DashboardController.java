package com.springsecuritystarter.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DashboardController {
    @GetMapping("/myDashboard")
    String getMyDashboard() {
        return  "myDashboard";
    }
}
