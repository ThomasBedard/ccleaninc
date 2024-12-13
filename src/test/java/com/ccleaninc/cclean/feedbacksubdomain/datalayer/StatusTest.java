package com.ccleaninc.cclean.feedbacksubdomain.datalayer;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class StatusTest {

    @Test
    public void testEnumValues() {
        Status[] values = Status.values();
        assertEquals(7, values.length);

        assertEquals(Status.REQUESTED, Status.valueOf("REQUESTED"));
        assertEquals(Status.SCHEDULED, Status.valueOf("SCHEDULED"));
        assertEquals(Status.CANCELLED, Status.valueOf("CANCELLED"));
        assertEquals(Status.ACTIVE, Status.valueOf("ACTIVE"));
        assertEquals(Status.VISIBLE, Status.valueOf("VISIBLE"));
        assertEquals(Status.INVISIBLE, Status.valueOf("INVISIBLE"));
        assertEquals(Status.COMPLETED, Status.valueOf("COMPLETED"));
    }
}
