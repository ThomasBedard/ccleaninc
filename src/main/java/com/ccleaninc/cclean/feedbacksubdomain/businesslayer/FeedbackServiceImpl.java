package com.ccleaninc.cclean.feedbacksubdomain.businesslayer;


import com.ccleaninc.cclean.customerssubdomain.businesslayer.CustomerService;
import com.ccleaninc.cclean.customerssubdomain.presentationlayer.CustomerResponseModel;
import com.ccleaninc.cclean.feedbacksubdomain.datalayer.Feedback;
import com.ccleaninc.cclean.feedbacksubdomain.datalayer.FeedbackRepository;
import com.ccleaninc.cclean.feedbacksubdomain.datamapperlayer.FeedbackRequestMapper;
import com.ccleaninc.cclean.feedbacksubdomain.datamapperlayer.FeedbackResponseMapper;
import com.ccleaninc.cclean.feedbacksubdomain.presentationlayer.FeedbackRequestModel;
import com.ccleaninc.cclean.feedbacksubdomain.presentationlayer.FeedbackResponseModel;
import com.ccleaninc.cclean.feedbacksubdomain.presentationlayer.State;
import com.ccleaninc.cclean.utils.exceptions.NotFoundException;
import org.springframework.stereotype.Service;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class FeedbackServiceImpl implements FeedbackService {
    private final FeedbackResponseMapper feedbackResponseMapper;
    private final FeedbackRepository feedbackRepository;
    private final FeedbackRequestMapper feedbackRequestMapper;
    private final CustomerService customerService;

    public FeedbackServiceImpl(FeedbackRepository feedbackRepository, FeedbackRequestMapper feedbackRequestMapper,
                               FeedbackResponseMapper feedbackResponseMapper, CustomerService customerService) {
        this.feedbackRepository = feedbackRepository;
        this.feedbackRequestMapper = feedbackRequestMapper;
        this.feedbackResponseMapper = feedbackResponseMapper;
        this.customerService = customerService;
    }

    @Override
    public List<FeedbackResponseModel> getAllFeedback(String feedbackId, String customerId, String status) {
        Set<Feedback> feedbacks = new HashSet<>();

        if (feedbackId != null) {
            feedbacks.addAll(feedbackRepository.findAllFeedbacksByFeedbackIdentifier_FeedbackIdStartingWith(feedbackId));
        } else if (customerId != null) {
            feedbacks.addAll(feedbackRepository.findAllFeedbacksByCustomerIdStartingWith(customerId));
        } else if (status != null) {
            feedbacks.addAll(feedbackRepository.findAllFeedbacksByStatus(State.valueOf(status)));
        } else {
            feedbacks.addAll(feedbackRepository.findAll());
        }

        return feedbackResponseMapper.entityListToResponseModelList(
                feedbacks.stream()
                        .sorted(Comparator.comparing(f -> f.getFeedbackIdentifier().getFeedbackId()))
                        .toList()
        );
    }

    @Override
    public FeedbackResponseModel getFeedbackByFeedbackId(String feedbackId) {
        Feedback feedback = feedbackRepository.findFeedbackByFeedbackIdentifier_FeedbackId(feedbackId);
        if (feedback == null) {
            throw new NotFoundException("Feedback not found with id: " + feedbackId);
        }
        return feedbackResponseMapper.entityToResponseModel(feedback);
    }

    // @Override
    // public FeedbackResponseModel addFeedback(FeedbackRequestModel feedbackRequestModel) {
    //     // Validate customerId as a valid customer ID
    //     customerService.getCustomerByCustomerId(feedbackRequestModel.getCustomerId());

    //     Feedback feedback = feedbackRequestMapper.requestModelToEntity(feedbackRequestModel);
    //     feedback.setStatus(State.INVISIBLE);
    //     Feedback savedFeedback = feedbackRepository.save(feedback);

    //     return feedbackResponseMapper.entityToResponseModel(savedFeedback);
    // }

    @Override
    public FeedbackResponseModel updateFeedbackState(String feedbackId, String status) {
        Feedback feedback = feedbackRepository.findFeedbackByFeedbackIdentifier_FeedbackId(feedbackId);
        if (feedback == null) {
            throw new NotFoundException("Feedback not found with id: " + feedbackId);
        }

        feedback.setStatus(State.valueOf(status));
        feedbackRepository.save(feedback);
        return feedbackResponseMapper.entityToResponseModel(feedback);
    }

    @Override
    public void removeFeedback(String feedbackId) {
        Feedback existingFeedback = feedbackRepository.findFeedbackByFeedbackIdentifier_FeedbackId(feedbackId);
        if (existingFeedback == null) {
            return;
        }
        feedbackRepository.delete(existingFeedback);
    }

    @Override
    public FeedbackResponseModel addFeedback(FeedbackRequestModel requestModel, String userEmail) {
        // 1) Lookup the customer by email -> returns a CustomerResponseModel
        CustomerResponseModel customerResponse = customerService.getCustomerByEmail(userEmail);
        if (customerResponse == null) {
            throw new NotFoundException("No customer found for email: " + userEmail);
        }

        // 2) Convert the request model to a Feedback entity
        Feedback feedbackEntity = feedbackRequestMapper.requestModelToEntity(requestModel);

        // 3) Overwrite the entity’s customerId with the real one from the DB
        feedbackEntity.setCustomerId(customerResponse.getCustomerId());

        // 4) Save the new feedback
        Feedback savedEntity = feedbackRepository.save(feedbackEntity);

        // 5) Convert entity -> response model
        return feedbackResponseMapper.entityToResponseModel(savedEntity);
    }
}
