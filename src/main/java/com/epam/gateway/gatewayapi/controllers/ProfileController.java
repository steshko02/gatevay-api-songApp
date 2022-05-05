package com.epam.gateway.gatewayapi.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class ProfileController {

    private final Logger log = LoggerFactory.getLogger(this.getClass());


    @GetMapping("/profile")
    public String profile(Model model, @AuthenticationPrincipal Authentication oidcUser) {
        model.addAttribute("profile", oidcUser.toString());
        return "ds";
    }

    private String claimsToJson(Map<String, Object> claims) {
        try {
            return new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(claims);
        } catch (JsonProcessingException jpe) {
            log.error("Error parsing claims to JSON", jpe);
        }
        return "Error parsing claims to JSON.";
    }
}
