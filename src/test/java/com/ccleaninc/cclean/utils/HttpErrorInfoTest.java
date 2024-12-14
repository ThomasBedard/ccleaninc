package com.ccleaninc.cclean.utils;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import static org.junit.jupiter.api.Assertions.*;

public class HttpErrorInfoTest {

    @Test
    public void testHttpErrorInfoFields() {
        HttpErrorInfo errorInfo = new HttpErrorInfo(HttpStatus.BAD_REQUEST, "uri=/somepath", "Bad request");
        assertEquals(HttpStatus.BAD_REQUEST, errorInfo.getHttpStatus());
        assertEquals("uri=/somepath", errorInfo.getPath());
        assertEquals("Bad request", errorInfo.getMessage());
        assertNotNull(errorInfo.getTimestamp());
    }
}
