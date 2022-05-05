package com.epam.gateway.gatewayapi.controllers;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.server.WebSession;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

@Controller
public class GreetingController {

    private Mono<ClientRegistration> registration;

    public GreetingController(ReactiveClientRegistrationRepository registrations) {
        this.registration = registrations.findByRegistrationId("auth0");
    }

    @RequestMapping("/home")
    public String greeting(@AuthenticationPrincipal OidcUser oidcUser, Model model,
                           @RegisteredOAuth2AuthorizedClient("auth0") OAuth2AuthorizedClient client) {

        model.addAttribute("username", oidcUser.getName());
        model.addAttribute("idToken", oidcUser.getIdToken());
        model.addAttribute("accessToken", client.getAccessToken());
        model.addAttribute("userInfo", oidcUser.getUserInfo());
        return "home";
    }

    @RequestMapping("/greeting")
    public String greeting(@AuthenticationPrincipal OidcIdToken oidcIdToken) {

        return "greeting";
    }

    @GetMapping("/")
    @ResponseBody
    public Mono<String> index(@AuthenticationPrincipal Mono<OAuth2User> oauth2User) {
        return oauth2User
                .map(OAuth2User::getName)
                .map(name -> String.format("Hi, %s", name));
    }


    @GetMapping("/api/logout")
    @ResponseBody
    public Mono<Map<String, String>> logout(@AuthenticationPrincipal(expression = "idToken") OidcIdToken idToken, WebSession session) {
        return session.invalidate().then(
                this.registration.map(oidc -> oidc.getProviderDetails().getConfigurationMetadata().get("end_session_endpoint").toString())
                        .map(logoutUrl -> {
                            Map<String, String> logoutDetails = new HashMap<>();
                            logoutDetails.put("logoutUrl", logoutUrl);
                            logoutDetails.put("idToken", idToken.getTokenValue());
                            return logoutDetails;
                        })
        );
    }


}
