package com.ccleaninc.cclean.feedbacksubdomain.datamapperlayer;

import com.ccleaninc.cclean.feedbacksubdomain.datalayer.Feedback;
import com.ccleaninc.cclean.feedbacksubdomain.presentationlayer.FeedbackRequestModel;
import com.ccleaninc.cclean.feedbacksubdomain.presentationlayer.State;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class FeedbackRequestMapperTest {

    // Directly instantiate the implementation class that MapStruct generates.
    // The class name is typically the interface name + "Impl".
    private FeedbackRequestMapper mapper = new FeedbackRequestMapperImpl();

    @Test
    public void requestModelToEntity_shouldMapCorrectly() {
        // Given
        FeedbackRequestModel requestModel = FeedbackRequestModel.builder()
                .customerId("cust-1234")
                .stars(4)
                .content("Great job!")
                .status(State.INVISIBLE)
                .build();

        // When
        Feedback feedback = mapper.requestModelToEntity(requestModel);

        // Then
        assertNotNull(feedback);
        assertNotNull(feedback.getFeedbackIdentifier());
        assertNotNull(feedback.getFeedbackIdentifier().getFeedbackId()); // UUID generated
        assertEquals("cust-1234", feedback.getCustomerId());
        assertEquals(4, feedback.getStars());
        assertEquals("Great job!", feedback.getContent());
        assertEquals(State.INVISIBLE, feedback.getStatus());
    }

    @Test
    public void createFeedbackIdentifier_shouldGenerateUUID() {
        // When
        var identifier = mapper.createFeedbackIdentifier();

        // Then
        assertNotNull(identifier);
        assertNotNull(identifier.getFeedbackId());
        assertFalse(identifier.getFeedbackId().isEmpty());
    }
}
