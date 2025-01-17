package com.springsecuritystarter.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CardController {

    @GetMapping("/myCards")
    public String getCards() {
        return  "get my cards api";
    }
}
