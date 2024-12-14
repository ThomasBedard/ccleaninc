package com.ccleaninc.cclean.feedbacksubdomain.datamapperlayer;

import com.ccleaninc.cclean.feedbacksubdomain.datalayer.Feedback;
import com.ccleaninc.cclean.feedbacksubdomain.datalayer.FeedbackIdentifier;
import com.ccleaninc.cclean.feedbacksubdomain.presentationlayer.FeedbackResponseModel;
import com.ccleaninc.cclean.feedbacksubdomain.presentationlayer.State;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class FeedbackResponseMapperTest {

    private FeedbackResponseMapper mapper = new FeedbackResponseMapperImpl();

    @Test
    public void entityToResponseModel_shouldMapCorrectly() {
        // Given
        Feedback feedback = new Feedback();
        FeedbackIdentifier fid = new FeedbackIdentifier();
        feedback.setCustomerId("cust-5678");
        feedback.setContent("Needs improvement");
        feedback.setStars(2);
        feedback.setStatus(State.INVISIBLE);
        fid.setFeedbackId("feedback-uuid");
        feedback.setFeedbackIdentifier(fid);

        // When
        FeedbackResponseModel responseModel = mapper.entityToResponseModel(feedback);

        // Then
        assertNotNull(responseModel);
        assertEquals("feedback-uuid", responseModel.getFeedbackId());
        assertEquals("cust-5678", responseModel.getCustomerId());
        assertEquals(2, responseModel.getStars());
        assertEquals("Needs improvement", responseModel.getContent());
        assertEquals(State.INVISIBLE, responseModel.getStatus());
    }

    @Test
    public void entityListToResponseModelList_shouldMapListCorrectly() {
        // Given
        Feedback feedback1 = new Feedback("cust-1", 5, "Excellent", State.VISIBLE);
        feedback1.getFeedbackIdentifier().setFeedbackId("fb1");
        Feedback feedback2 = new Feedback("cust-2", 3, "Average", State.INVISIBLE);
        feedback2.getFeedbackIdentifier().setFeedbackId("fb2");

        List<Feedback> feedbacks = List.of(feedback1, feedback2);

        // When
        List<FeedbackResponseModel> responseList = mapper.entityListToResponseModelList(feedbacks);

        // Then
        assertNotNull(responseList);
        assertEquals(2, responseList.size());

        FeedbackResponseModel rm1 = responseList.get(0);
        assertEquals("fb1", rm1.getFeedbackId());
        assertEquals("cust-1", rm1.getCustomerId());
        assertEquals(5, rm1.getStars());
        assertEquals("Excellent", rm1.getContent());
        assertEquals(State.VISIBLE, rm1.getStatus());

        FeedbackResponseModel rm2 = responseList.get(1);
        assertEquals("fb2", rm2.getFeedbackId());
        assertEquals("cust-2", rm2.getCustomerId());
        assertEquals(3, rm2.getStars());
        assertEquals("Average", rm2.getContent());
        assertEquals(State.INVISIBLE, rm2.getStatus());
    }
}
