package com.ccleaninc.cclean.feedbacksubdomain.presentationlayer;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;
@Value
@Builder
@AllArgsConstructor
public class FeedbackResponseModel {
    String feedbackId;
    String customerId; // renamed from userId
    Integer stars;
    String content;
    State status;
}
