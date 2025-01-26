package com.ccleaninc.cclean.security.presentationlayer;

import com.ccleaninc.cclean.security.AuthenticationErrorHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.AuthenticationException;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

class AuthenticationErrorHandlerTest {

    private AuthenticationErrorHandler authenticationErrorHandler;
    private MockHttpServletResponse response;
    private MockHttpServletRequest request;
    private AuthenticationException authException;

    @BeforeEach
    void setup() {
        ObjectMapper mapper = new ObjectMapper();
        authenticationErrorHandler = new AuthenticationErrorHandler(mapper);
        response = new MockHttpServletResponse();
        request = new MockHttpServletRequest();
        authException = mock(AuthenticationException.class);
    }

    @Test
    void shouldReturnUnauthorizedWithErrorMessage() throws IOException, ServletException {
        authenticationErrorHandler.commence(request, response, authException);

        assertEquals(401, response.getStatus());
        assertEquals("application/json", response.getContentType());
        assertEquals("{\"message\":\"Requires authentication\"}", response.getContentAsString());
    }

}

