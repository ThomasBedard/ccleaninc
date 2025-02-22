package com.ccleaninc.cclean.security.services;

import com.ccleaninc.cclean.security.dto.Auth0TokenResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Configuration
public class Auth0ManagementTokenService {

    @Value("${auth0.mgmt.domain}")
    private String auth0Domain;

    @Value("${auth0.mgmt.clientId}")
    private String clientId;

    @Value("${auth0.mgmt.clientSecret}")
    private String clientSecret;

    @Value("${auth0.mgmt.audience}")
    private String audience;

    private final RestTemplate restTemplate = new RestTemplate();

    private String managementApiToken;
    private long expiryTimeMillis = 0L; // store expiry as a timestamp

    /**
     * Get a valid Management API token. If the token is expired or not fetched yet,
     * request a new one from Auth0. Otherwise, return the cached token.
     */
    public synchronized String getManagementApiToken() {
        // If token not set or about to expire, fetch a new one
        if (managementApiToken == null || isTokenExpired()) {
            fetchNewToken();
        }
        return managementApiToken;
    }

    private boolean isTokenExpired() {
        long now = System.currentTimeMillis();
        return now > expiryTimeMillis;
    }

    private void fetchNewToken() {
        String url = "https://" + auth0Domain + "/oauth/token";
        // Prepare request
        Map<String, String> body = Map.of(
                "client_id", clientId,
                "client_secret", clientSecret,
                "audience", audience,
                "grant_type", "client_credentials"
        );
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, String>> request = new HttpEntity<>(body, headers);

        // Call Auth0
        ResponseEntity<Auth0TokenResponse> response =
                restTemplate.exchange(url, HttpMethod.POST, request, Auth0TokenResponse.class);

        if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
            Auth0TokenResponse tokenResponse = response.getBody();
            this.managementApiToken = tokenResponse.getAccess_token();
            // Expires in X seconds => convert to absolute timestamp
            long expiresInMillis = tokenResponse.getExpires_in() * 1000L;
            this.expiryTimeMillis = System.currentTimeMillis() + expiresInMillis - 60000;
            // subtract 60s to be safe
        } else {
            throw new RuntimeException("Failed to fetch Auth0 Management API token. Response = " + response);
        }
    }
}
