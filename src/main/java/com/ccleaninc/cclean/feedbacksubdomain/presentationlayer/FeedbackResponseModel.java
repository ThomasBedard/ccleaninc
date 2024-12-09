package com.ccleaninc.cclean.feedbacksubdomain.presentationlayer;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;
@Value
@Builder
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class FeedbackResponseModel {
    String feedbackId;
    String userId;
    Integer stars;
    String content;
    State status;
}
