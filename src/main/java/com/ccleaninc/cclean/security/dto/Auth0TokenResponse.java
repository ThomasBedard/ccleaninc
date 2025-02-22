package com.ccleaninc.cclean.security.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
@AllArgsConstructor
public class Auth0TokenResponse {
    String access_token;
    String token_type;
    long expires_in;
}

