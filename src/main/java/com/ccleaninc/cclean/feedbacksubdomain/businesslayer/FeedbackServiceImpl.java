package com.ccleaninc.cclean.feedbacksubdomain.businesslayer;


import com.ccleaninc.cclean.feedbacksubdomain.datalayer.Feedback;
import com.ccleaninc.cclean.feedbacksubdomain.datalayer.FeedbackRepository;
import com.ccleaninc.cclean.feedbacksubdomain.datamapperlayer.FeedbackRequestMapper;
import com.ccleaninc.cclean.feedbacksubdomain.datamapperlayer.FeedbackResponseMapper;
import com.ccleaninc.cclean.feedbacksubdomain.presentationlayer.FeedbackRequestModel;
import com.ccleaninc.cclean.feedbacksubdomain.presentationlayer.FeedbackResponseModel;
import com.ccleaninc.cclean.feedbacksubdomain.presentationlayer.State;
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
    public FeedbackServiceImpl(FeedbackRepository feedbackRepository, FeedbackRequestMapper feedbackRequestMapper, FeedbackResponseMapper feedbackResponseMapper){
        this.feedbackRepository = feedbackRepository;
        this.feedbackRequestMapper = feedbackRequestMapper;
        this.feedbackResponseMapper = feedbackResponseMapper;
    }

    @Override
    public List<FeedbackResponseModel> getAllFeedback(String feedbackId, String userid, String status) {
        Set<Feedback> feedbacks = new HashSet<>();
        if(feedbackId != null){
            feedbacks.addAll(feedbackRepository.findAllFeedbacksByFeedbackIdentifier_FeedbackIdStartingWith(feedbackId));
        } else if(userid != null){
            feedbacks.addAll(feedbackRepository.findAllFeedbacksByUserIdStartingWith(userid));
        } else if(status != null){
            feedbacks.addAll(feedbackRepository.findAllFeedbacksByStatus(State.valueOf(status)));
        } else {
            feedbacks.addAll(feedbackRepository.findAll());
        }
        return feedbackResponseMapper.entityListToResponseModelList(feedbacks.stream().sorted(Comparator.comparing(feedback -> feedback.getFeedbackIdentifier().getFeedbackId())).toList());
    }

    @Override
    public FeedbackResponseModel getFeedbackByFeedbackId(String feedbackId) {
        return null;
    }

    @Override
    public FeedbackResponseModel addFeedback(FeedbackRequestModel feedbackRequestModel) {
        Feedback feedback = feedbackRequestMapper.requestModelToEntity(feedbackRequestModel);
        feedback.setStatus(State.INVISIBLE);
        Feedback savedFeedback = feedbackRepository.save(feedback);
        return feedbackResponseMapper.entityToResponseModel(savedFeedback);
    }

    @Override
    public FeedbackResponseModel updateFeedbackState(String feedbackId, String status) {
        Feedback feedback = feedbackRepository.findFeedbackByFeedbackIdentifier_FeedbackId(feedbackId);
        feedback.setStatus(State.valueOf(status));
        feedbackRepository.save(feedback);
        return feedbackResponseMapper.entityToResponseModel(feedback);
    }

    @Override
    public void removeFeedback(String feedbackId) {
        Feedback existingFeedback = feedbackRepository.findFeedbackByFeedbackIdentifier_FeedbackId(feedbackId);
        if(existingFeedback == null){
            return;
        }
        feedbackRepository.delete(existingFeedback);
    }
}
