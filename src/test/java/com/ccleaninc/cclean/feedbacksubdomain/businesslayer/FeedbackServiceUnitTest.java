package com.ccleaninc.cclean.feedbacksubdomain.businesslayer;

import com.ccleaninc.cclean.customerssubdomain.businesslayer.CustomerService;
import com.ccleaninc.cclean.feedbacksubdomain.datalayer.Feedback;
import com.ccleaninc.cclean.feedbacksubdomain.datalayer.FeedbackRepository;
import com.ccleaninc.cclean.feedbacksubdomain.datamapperlayer.FeedbackRequestMapper;
import com.ccleaninc.cclean.feedbacksubdomain.datamapperlayer.FeedbackResponseMapper;
import com.ccleaninc.cclean.feedbacksubdomain.presentationlayer.FeedbackRequestModel;
import com.ccleaninc.cclean.feedbacksubdomain.presentationlayer.FeedbackResponseModel;
import com.ccleaninc.cclean.feedbacksubdomain.presentationlayer.State;
import com.ccleaninc.cclean.customerssubdomain.presentationlayer.CustomerResponseModel;
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

    @Mock
    CustomerService customerService; // Add this mock

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void getAllFeedbacks_shouldSucceed() {
        String feedbackId = "uuid-feed1";
        String status = "INVISIBLE";
        String customerId = "uuid-customer1";
        Integer stars = 4;
        String content = "test";

        Feedback feedback = new Feedback(customerId, stars, content, State.valueOf(status));
        feedback.getFeedbackIdentifier().setFeedbackId(feedbackId);
        List<Feedback> feedbacks = Collections.singletonList(feedback);

        when(feedbackRepository.findAll()).thenReturn(feedbacks);
        when(feedbackRepository.findAllFeedbacksByFeedbackIdentifier_FeedbackIdStartingWith(feedbackId)).thenReturn(feedbacks);
        when(feedbackRepository.findAllFeedbacksByCustomerIdStartingWith(customerId)).thenReturn(feedbacks);
        when(feedbackRepository.findAllFeedbacksByStatus(State.valueOf(status))).thenReturn(feedbacks);

        FeedbackResponseModel responseModel = FeedbackResponseModel.builder()
                .feedbackId(feedbackId)
                .customerId(customerId)
                .content(content)
                .stars(stars)
                .status(State.valueOf(status))
                .build();

        List<FeedbackResponseModel> responseModels = Collections.singletonList(responseModel);
        when(feedbackResponseMapper.entityListToResponseModelList(feedbacks)).thenReturn(responseModels);

        // No filters
        List<FeedbackResponseModel> result = feedbackService.getAllFeedback(null, null, null);
        assertNotNull(result);
        assertFalse(result.isEmpty());

        // By feedbackId
        List<FeedbackResponseModel> result2 = feedbackService.getAllFeedback(feedbackId, null, null);
        assertNotNull(result2);
        assertFalse(result2.isEmpty());

        // By customerId
        List<FeedbackResponseModel> result3 = feedbackService.getAllFeedback(null, customerId, null);
        assertNotNull(result3);
        assertFalse(result3.isEmpty());

        // By status
        List<FeedbackResponseModel> result4 = feedbackService.getAllFeedback(null, null, status);
        assertNotNull(result4);
        assertFalse(result4.isEmpty());
    }

    @Test
    public void addFeedback_shouldSucceed() {
        String status = "INVISIBLE";
        String customerId = "uuid-customer1";
        FeedbackRequestModel requestModel = new FeedbackRequestModel(customerId, 3, "test", State.valueOf(status));
        Feedback entity = new Feedback(customerId, 3, "test", State.INVISIBLE);
        entity.getFeedbackIdentifier().setFeedbackId("feed-id1");

        FeedbackResponseModel mockedResponse = new FeedbackResponseModel("feed-id1", customerId, 3, "test", State.INVISIBLE);

        // Mock customerService call
        CustomerResponseModel mockCustomerResponse = CustomerResponseModel.builder()
                .customerId(customerId)
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .phoneNumber("1234567890")
                .build();

        when(customerService.getCustomerByCustomerId(customerId)).thenReturn(mockCustomerResponse);

        when(feedbackRequestMapper.requestModelToEntity(requestModel)).thenReturn(entity);
        when(feedbackRepository.save(entity)).thenReturn(entity);
        when(feedbackResponseMapper.entityToResponseModel(entity)).thenReturn(mockedResponse);

        FeedbackResponseModel result = feedbackService.addFeedback(requestModel);
        assertNotNull(result);
        assertEquals("feed-id1", result.getFeedbackId());
        assertEquals(customerId, result.getCustomerId());
        assertEquals("test", result.getContent());
        assertEquals(State.INVISIBLE, result.getStatus());
    }

    @Test
    public void removeFeedback_validFeedbackId_shouldSucceed() {
        String feedbackId = "feed-id1";
        Feedback entity = mock(Feedback.class);

        when(feedbackRepository.findFeedbackByFeedbackIdentifier_FeedbackId(feedbackId)).thenReturn(entity);

        feedbackService.removeFeedback(feedbackId);

        verify(feedbackRepository, times(1)).delete(entity);
    }

    @Test
    public void removeFeedback_invalidFeedbackId_shouldFail() {
        String feedbackId = "invalid-feed-id1";
        when(feedbackRepository.findFeedbackByFeedbackIdentifier_FeedbackId(feedbackId)).thenReturn(null);

        feedbackService.removeFeedback(feedbackId);

        verify(feedbackRepository, never()).delete(any(Feedback.class));
    }

    @Test
    public void updateFeedbackStatus_shouldSucceed() {
        String feedbackId = "uuid-feed1";
        String status = "VISIBLE";

        Feedback feedback = new Feedback();
        feedback.getFeedbackIdentifier().setFeedbackId(feedbackId);
        feedback.setStatus(State.INVISIBLE);

        when(feedbackRepository.findFeedbackByFeedbackIdentifier_FeedbackId(feedbackId)).thenReturn(feedback);
        when(feedbackRepository.save(feedback)).thenReturn(feedback);
        when(feedbackResponseMapper.entityToResponseModel(feedback)).thenAnswer(invocation -> {
            Feedback updatedFeedback = invocation.getArgument(0);
            return new FeedbackResponseModel(
                    updatedFeedback.getFeedbackIdentifier().getFeedbackId(),
                    "uuid-customer1",
                    4,
                    "testContent",
                    updatedFeedback.getStatus()
            );
        });

        FeedbackResponseModel result = feedbackService.updateFeedbackState(feedbackId, status);
        assertEquals(State.VISIBLE, result.getStatus());
    }
}
