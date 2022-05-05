package com.epam.gateway.gatewayapi.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.oauth2.core.oidc.user.OidcUserAuthority;
import org.springframework.security.oauth2.core.user.OAuth2UserAuthority;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoders;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.logout.RedirectServerLogoutSuccessHandler;
import org.springframework.security.web.server.authentication.logout.ServerLogoutSuccessHandler;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Configuration
@EnableWebFluxSecurity
@Slf4j
public class AuthConfig {

    private final String issuer = "https://dev-hf6j43cb.eu.auth0.com/";
    private static final String ROLES_CLAIM = "http://schemas.microsoft.com/ws/2008/06/identity/claims/role";

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {

        http.oauth2ResourceServer().jwt().jwtDecoder(jwtDecoder());

        return http.httpBasic().disable()
                .authorizeExchange()
//                .pathMatchers("/static/**", "/resources/**","/resources/static/**")
//                .permitAll()
                .pathMatchers("/callback", "/images/**").permitAll()
                .pathMatchers("/admin1/**").hasRole("Admin")
                .pathMatchers("/upload-app/**", "/music/**").hasRole("Admin")
                .pathMatchers("/test").hasAuthority("SCOPE_read:all")
                .anyExchange().authenticated()
                .and().oauth2Login()
                .and().logout().and().csrf().disable().build();
    }

    @Bean
    public GrantedAuthoritiesMapper userAuthoritiesMapper() {
        return authorities -> {
            Set<GrantedAuthority> mappedAuthorities = new HashSet<>();
            var authority = authorities.iterator().next();
            boolean isOidc = authority instanceof OidcUserAuthority;

            if (isOidc) {
                var oidcUserAuthority = (OidcUserAuthority) authority;
                var userInfo = oidcUserAuthority.getUserInfo();

                if (userInfo.containsClaim(ROLES_CLAIM)) {
                    var roles = userInfo.getClaimAsStringList(ROLES_CLAIM);
                    mappedAuthorities.addAll(generateAuthoritiesFromClaim(roles));
                }
            } else {
                var oauth2UserAuthority = (OAuth2UserAuthority) authority;
                Map<String, Object> userAttributes = oauth2UserAuthority.getAttributes();

                if (userAttributes.containsKey(ROLES_CLAIM)) {
                    var roles = (Collection<String>) userAttributes.get(ROLES_CLAIM);
                    mappedAuthorities.addAll(generateAuthoritiesFromClaim(roles));
                }
            }

            return mappedAuthorities;
        };
    }

    private Collection<GrantedAuthority> generateAuthoritiesFromClaim(Collection<String> roles) {
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                .collect(Collectors.toList());
    }

    @Bean
    public ServerLogoutSuccessHandler logoutSuccessHandler() {
        String returnTo = "http://localhost:8080/";

        String logoutUrl = UriComponentsBuilder
                .fromHttpUrl(issuer + "v2/logout?client_id={clientId}&returnTo={returnTo}")
                .encode()
                .buildAndExpand("4U0RJAhsG9JJp23wQVdwuaoby62ICwgj", returnTo)
                .toUriString();
        RedirectServerLogoutSuccessHandler handler = new RedirectServerLogoutSuccessHandler();
        handler.setLogoutSuccessUrl(URI.create(logoutUrl));
        return handler;
    }

    @Bean
    public ReactiveJwtDecoder jwtDecoder() {
        return ReactiveJwtDecoders.fromIssuerLocation(issuer);
    }
}