package com.ccleaninc.cclean.feedbacksubdomain.datalayer;

import com.ccleaninc.cclean.feedbacksubdomain.presentationlayer.State;
import jakarta.persistence.*;
import lombok.Data;
@Data
@Entity
@Table(name="feedback_threads")
public class Feedback {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Embedded
    private FeedbackIdentifier feedbackIdentifier;
    private String userId;
    private Integer stars;
    private String content;

    @Enumerated(EnumType.STRING)
    private State status;

    public Feedback(){
        this.feedbackIdentifier = new FeedbackIdentifier();
    }

    public Feedback( String userId, Integer stars, String content, State status) {
        this.feedbackIdentifier = new FeedbackIdentifier();
        this.userId = userId;
        this.stars = stars;
        this.content = content;
        this.status = status;
    }
}
