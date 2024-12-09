package com.ccleaninc.cclean.feedbacksubdomain.datamapperlayer;

import com.ccleaninc.cclean.feedbacksubdomain.datalayer.Feedback;
import com.ccleaninc.cclean.feedbacksubdomain.datalayer.FeedbackIdentifier;
import com.ccleaninc.cclean.feedbacksubdomain.presentationlayer.FeedbackRequestModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
@Mapper(componentModel = "spring")
public interface FeedbackRequestMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "feedbackIdentifier", expression = "java(createFeedbackIdentifier())")
    @Mapping(target = "userId", source = "userId")
    @Mapping(target = "stars", source = "stars")
    @Mapping(target = "content", source = "content")
    @Mapping(target = "status", source = "status")
    Feedback requestModelToEntity(FeedbackRequestModel feedbackRequestModel);
    default FeedbackIdentifier createFeedbackIdentifier() {
        return new FeedbackIdentifier();
    }
}
