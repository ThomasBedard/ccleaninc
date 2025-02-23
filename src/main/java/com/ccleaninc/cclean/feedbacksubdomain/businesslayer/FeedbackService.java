package com.ccleaninc.cclean.feedbacksubdomain.businesslayer;


import com.ccleaninc.cclean.feedbacksubdomain.presentationlayer.FeedbackRequestModel;
import com.ccleaninc.cclean.feedbacksubdomain.presentationlayer.FeedbackResponseModel;

import java.util.List;
public interface FeedbackService {
    List<FeedbackResponseModel> getAllFeedback(String feedbackId, String customerid, String status);
    FeedbackResponseModel getFeedbackByFeedbackId(String feedbackId);
    // FeedbackResponseModel addFeedback(FeedbackRequestModel feedbackRequestModel);
    FeedbackResponseModel updateFeedbackState(String status, String feedbackId);
    void removeFeedback(String feedbackId);
    FeedbackResponseModel addFeedback(FeedbackRequestModel feedbackRequestModel, String userEmail);
}
