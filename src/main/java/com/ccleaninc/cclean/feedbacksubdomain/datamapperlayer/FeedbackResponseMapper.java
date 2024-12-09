package com.ccleaninc.cclean.feedbacksubdomain.datamapperlayer;

import com.ccleaninc.cclean.feedbacksubdomain.datalayer.Feedback;
import com.ccleaninc.cclean.feedbacksubdomain.presentationlayer.FeedbackResponseModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import java.util.List;
@Mapper(componentModel = "spring")
public interface FeedbackResponseMapper {
    @Mapping(target = "feedbackId", expression = "java(feedback.getFeedbackIdentifier().getFeedbackId())")
    @Mapping(target = "userId", source = "userId")
    @Mapping(target = "stars", source = "stars")
    @Mapping(target = "content", source = "content")
    @Mapping(target = "status", source = "status")
    FeedbackResponseModel entityToResponseModel(Feedback feedback);
    List<FeedbackResponseModel> entityListToResponseModelList(List<Feedback> feedbacks);
}
