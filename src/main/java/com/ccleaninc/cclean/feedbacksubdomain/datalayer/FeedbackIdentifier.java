package com.ccleaninc.cclean.feedbacksubdomain.datalayer;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;
import java.util.UUID;
@Embeddable
@Getter
@Setter
public class FeedbackIdentifier {
    private String feedbackId;
    public FeedbackIdentifier() {
        this.feedbackId = UUID.randomUUID().toString();
    }
}
