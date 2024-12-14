package com.ccleaninc.cclean.feedbacksubdomain.presentationlayer;

import com.ccleaninc.cclean.feedbacksubdomain.businesslayer.FeedbackService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class FeedbackControllerUnitTest {

    @Mock
    private FeedbackService feedbackService;

    @InjectMocks
    private FeedbackController feedbackController;
    private FeedbackResponseModel feedbackResponseModel;
    private FeedbackRequestModel feedbackRequestModel;

    @BeforeEach
    void setUp() {
        feedbackResponseModel = feedbackResponseModel.builder()
                .feedbackId("1")
                .customerId("Test Feedback")
                .stars(4)
                .content("Test Description")
                .status(State.INVISIBLE)
                .build();
    }

    @Test
    void getAllFeedbackThreadsReturnsListOfFeedbacks() {
        when(feedbackService.getAllFeedback(null, null, null)).thenReturn(List.of(feedbackResponseModel));
        List<FeedbackResponseModel> feedbacks = feedbackController.getAllFeedbackThreads(null, null, null);
        assertEquals(1, feedbacks.size());
        assertEquals(feedbackResponseModel, feedbacks.get(0));
    }

    @Test
    void addFeedbackReturnsCreatedFeedback() {
        when(feedbackService.addFeedback(feedbackRequestModel)).thenReturn(feedbackResponseModel);
        ResponseEntity<FeedbackResponseModel> response = feedbackController.addFeedback(feedbackRequestModel);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(feedbackResponseModel, response.getBody());
    }

    @Test
    void updateFeedbackStateReturnsUpdatedFeedback() {
        when(feedbackService.updateFeedbackState("1", "PUBLISHED")).thenReturn(feedbackResponseModel);
        ResponseEntity<FeedbackResponseModel> response = feedbackController.updateFeedbackState("1", "PUBLISHED");
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(feedbackResponseModel, response.getBody());
    }
}