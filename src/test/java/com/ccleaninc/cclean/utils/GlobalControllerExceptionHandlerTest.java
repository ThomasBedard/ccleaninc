package com.ccleaninc.cclean.utils;

import com.ccleaninc.cclean.utils.exceptions.InvalidInputException;
import com.ccleaninc.cclean.utils.exceptions.NotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.context.request.WebRequest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class GlobalControllerExceptionHandlerTest {

    @Test
    public void handleNotFoundException_shouldReturnNotFound() {
        GlobalControllerExceptionHandler handler = new GlobalControllerExceptionHandler();
        WebRequest mockRequest = mock(WebRequest.class);

        // Mock getDescription to return some path info
        when(mockRequest.getDescription(false)).thenReturn("uri=/testpath");
        NotFoundException ex = new NotFoundException("Item not found");

        HttpErrorInfo errorInfo = handler.handleNotFoundException(mockRequest, ex);

        assertNotNull(errorInfo);
        assertEquals(HttpStatus.NOT_FOUND, errorInfo.getHttpStatus());
        assertEquals("Item not found", errorInfo.getMessage());
        assertTrue(errorInfo.getPath().contains("/testpath"));
        assertNotNull(errorInfo.getTimestamp());
    }

    @Test
    public void handleInvalidInputException_shouldReturnUnprocessableEntity() {
        GlobalControllerExceptionHandler handler = new GlobalControllerExceptionHandler();
        WebRequest mockRequest = mock(WebRequest.class);

        when(mockRequest.getDescription(false)).thenReturn("uri=/testInvalid");
        InvalidInputException ex = new InvalidInputException("Invalid input provided");

        HttpErrorInfo errorInfo = handler.handleInvalidInputException(mockRequest, ex);

        assertNotNull(errorInfo);
        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, errorInfo.getHttpStatus());
        assertEquals("Invalid input provided", errorInfo.getMessage());
        assertTrue(errorInfo.getPath().contains("/testInvalid"));
        assertNotNull(errorInfo.getTimestamp());
    }
}
