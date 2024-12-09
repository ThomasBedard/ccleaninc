package com.ccleaninc.cclean.feedbacksubdomain.businesslayer;

import com.ccleaninc.cclean.feedbacksubdomain.datalayer.Feedback;
import com.ccleaninc.cclean.feedbacksubdomain.datalayer.FeedbackRepository;
import com.ccleaninc.cclean.feedbacksubdomain.datamapperlayer.FeedbackRequestMapper;
import com.ccleaninc.cclean.feedbacksubdomain.datamapperlayer.FeedbackResponseMapper;
import com.ccleaninc.cclean.feedbacksubdomain.presentationlayer.FeedbackRequestModel;
import com.ccleaninc.cclean.feedbacksubdomain.presentationlayer.FeedbackResponseModel;
import com.ccleaninc.cclean.feedbacksubdomain.presentationlayer.State;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ActiveProfiles;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ActiveProfiles("test")
class FeedbackServiceUnitTest {

    @InjectMocks
    FeedbackServiceImpl feedbackService;

    @Mock
    FeedbackRepository feedbackRepository;

    @Mock
    FeedbackResponseMapper feedbackResponseMapper;

    @Mock
    FeedbackRequestMapper feedbackRequestMapper;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void getAllFeedbacks_shouldSucceed(){
        String feedbackId = "uuid-feed1";
        String status = "INVISIBLE";
        String userId = "uuid-user1";
        Integer stars = 4;
        String content = "test";
        Feedback feedback = new Feedback(userId, stars, content, State.valueOf(status));
        feedback.getFeedbackIdentifier().setFeedbackId(feedbackId);
        List<Feedback> feedbacks = Collections.singletonList(feedback);


        when(feedbackRepository.findAll()).thenReturn(feedbacks);
        when(feedbackRepository.findAllFeedbacksByFeedbackIdentifier_FeedbackIdStartingWith(feedbackId)).thenReturn(feedbacks);
        when(feedbackRepository.findAllFeedbacksByUserIdStartingWith(userId)).thenReturn(feedbacks);
        when(feedbackRepository.findAllFeedbacksByStatus(State.valueOf(status))).thenReturn(feedbacks);
        FeedbackResponseModel responseModel = FeedbackResponseModel.builder()
                .userId("test")
                .content("test")
                .stars(3)
                .status(State.valueOf(status))
                .build();

        List<FeedbackResponseModel> responseModels = Collections.singletonList(responseModel);
        when(feedbackResponseMapper.entityListToResponseModelList(feedbacks)).thenReturn(responseModels);

        List<FeedbackResponseModel> result = feedbackService.getAllFeedback(null, null, null);

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(responseModels.size(), result.size());
        assertEquals(responseModels.get(0).getFeedbackId(), result.get(0).getFeedbackId());

        List<FeedbackResponseModel> result2 = feedbackService.getAllFeedback(feedbackId,null, null);

        assertNotNull(result2);
        assertFalse(result2.isEmpty());
        assertEquals(responseModels.size(), result2.size());
        assertEquals(responseModels.get(0).getFeedbackId(), result2.get(0).getFeedbackId());
        assertEquals(responseModels.get(0).getUserId(), result2.get(0).getUserId());
        assertEquals(responseModels.get(0).getStatus(), result2.get(0).getStatus());
        assertEquals(responseModels.get(0).getStars(), result2.get(0).getStars());
        assertEquals(responseModels.get(0).getContent(), result2.get(0).getContent());

        List<FeedbackResponseModel> result3 = feedbackService.getAllFeedback(null,userId, null);

        assertNotNull(result3);
        assertFalse(result3.isEmpty());
        assertEquals(responseModels.size(), result3.size());
        assertEquals(responseModels.get(0).getFeedbackId(), result3.get(0).getFeedbackId());
        assertEquals(responseModels.get(0).getUserId(), result3.get(0).getUserId());
        assertEquals(responseModels.get(0).getStatus(), result3.get(0).getStatus());
        assertEquals(responseModels.get(0).getStars(), result3.get(0).getStars());
        assertEquals(responseModels.get(0).getContent(), result3.get(0).getContent());

        List<FeedbackResponseModel> result4 = feedbackService.getAllFeedback(null,null, status);

        assertNotNull(result4);
        assertFalse(result4.isEmpty());
        assertEquals(responseModels.size(), result4.size());
        assertEquals(responseModels.get(0).getFeedbackId(), result4.get(0).getFeedbackId());
        assertEquals(responseModels.get(0).getUserId(), result4.get(0).getUserId());
        assertEquals(responseModels.get(0).getStatus(), result4.get(0).getStatus());
        assertEquals(responseModels.get(0).getStars(), result4.get(0).getStars());
        assertEquals(responseModels.get(0).getContent(), result4.get(0).getContent());


    }

    @Test
    public void addFeedback_shouldSucceed(){
        String status = "INVISIBLE";
        FeedbackRequestModel requestModel = new FeedbackRequestModel("uuid-user1", 3,  "test", State.valueOf(status));

        Feedback entity = mock(Feedback.class);
        FeedbackResponseModel mockedResponse = new FeedbackResponseModel("feed-id1", "uuid-user1", 3,  "test", State.valueOf(status));
        when(feedbackResponseMapper.entityToResponseModel(entity)).thenReturn(mockedResponse);
        when(feedbackRequestMapper.requestModelToEntity(requestModel)).thenReturn(entity);
        when(feedbackRepository.save(entity)).thenReturn(entity);

        FeedbackResponseModel result = feedbackService.addFeedback(requestModel);
        assertNotNull(result);
        assertNotNull(result.getFeedbackId());
        assertNotNull(result.getUserId());
        assertNotNull(result.getContent());
        assertNotNull(result.getStatus());
    }

    @Test
    public void removeFeedback_validFeedbackId_shouldSucceed(){
        String feedbackId = "feed-id1";
        Feedback entity = mock(Feedback.class);

        when(feedbackRepository.findFeedbackByFeedbackIdentifier_FeedbackId(feedbackId)).thenReturn(entity);

        feedbackService.removeFeedback(feedbackId);

        verify(feedbackRepository, times(1)).delete(entity);
    }

    @Test
    public void removeFeedback_invalidFeedbackId_shouldFail(){
        String feedbackId = "invalid-feed-id1";

        when(feedbackRepository.findFeedbackByFeedbackIdentifier_FeedbackId(feedbackId)).thenReturn(null);

        feedbackService.removeFeedback(feedbackId);

        verify(feedbackRepository, never()).delete(any(Feedback.class));
    }

    @Test
    public void updateFeedbackStatus_shouldSucceed(){
        String feedbackId = "uuid-feed1";
        String status = "VISIBLE";

        Feedback feedback = new Feedback();
        feedback.getFeedbackIdentifier().setFeedbackId(feedbackId);

        FeedbackResponseModel responseModel = new FeedbackResponseModel(
                "uuid-feed1",
                "uuid-user1",
                4,
                "testContent",
                State.INVISIBLE
        );

        when(feedbackRepository.findFeedbackByFeedbackIdentifier_FeedbackId(feedbackId)).thenReturn(feedback);
        when(feedbackResponseMapper.entityToResponseModel(feedback)).thenReturn(responseModel);

        FeedbackResponseModel result = feedbackService.updateFeedbackState(feedbackId, status);

        assertEquals(State.INVISIBLE, result.getStatus());
    }

}
