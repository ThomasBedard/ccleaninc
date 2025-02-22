package com.ccleaninc.cclean.security.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Service
public class Auth0UserManagementService {

    private final Auth0ManagementTokenService tokenService;
    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${auth0.mgmt.domain}")
    private String auth0Domain;

    public Auth0UserManagementService(Auth0ManagementTokenService tokenService) {
        this.tokenService = tokenService;
    }

    // (Optional) getUsers() and getUserPermissions() if you want to see user details or permissions
    public String getUsers() {
        String token = tokenService.getManagementApiToken();
        String url = "https://" + auth0Domain + "/api/v2/users";

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));

        ResponseEntity<String> response = restTemplate.exchange(
                url, HttpMethod.GET, new HttpEntity<>(headers), String.class
        );
        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new RuntimeException("Failed to get users: " + response);
        }
        return response.getBody();
    }

    public String getUserPermissions(String userId) {
        String token = tokenService.getManagementApiToken();
        String url = "https://" + auth0Domain + "/api/v2/users/" + userId + "/permissions";

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));

        ResponseEntity<String> response = restTemplate.exchange(
                url, HttpMethod.GET, new HttpEntity<>(headers), String.class
        );
        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new RuntimeException("Failed to get user permissions: " + response);
        }
        return response.getBody();
    }

    /**
     * Assign a role to the user.
     * POST /api/v2/users/{userId}/roles
     * Body: { "roles": ["rol_abc123"] }
     */
    public void assignRole(String userId, String roleId) {
        String token = tokenService.getManagementApiToken();
        String url = "https://" + auth0Domain + "/api/v2/users/" + userId + "/roles";

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, String[]> body = Map.of("roles", new String[]{roleId});

        ResponseEntity<String> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                new HttpEntity<>(body, headers),
                String.class
        );
        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new RuntimeException("Failed to assign role: " + response);
        }
    }

    /**
     * Remove a role from the user.
     * DELETE /api/v2/users/{userId}/roles
     * Body: { "roles": ["rol_abc123"] }
     */
    public void removeRole(String userId, String roleId) {
        String token = tokenService.getManagementApiToken();
        String url = "https://" + auth0Domain + "/api/v2/users/" + userId + "/roles";

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, String[]> body = Map.of("roles", new String[]{roleId});

        ResponseEntity<String> response = restTemplate.exchange(
                url,
                HttpMethod.DELETE,
                new HttpEntity<>(body, headers),
                String.class
        );
        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new RuntimeException("Failed to remove role: " + response);
        }
    }
}

