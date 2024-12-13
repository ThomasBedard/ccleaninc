package com.ccleaninc.cclean.feedbacksubdomain.datalayer;

import com.ccleaninc.cclean.feedbacksubdomain.presentationlayer.State;
import jakarta.persistence.Embeddable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

@Embeddable
public interface FeedbackRepository extends JpaRepository<Feedback, Integer> {
    Feedback findFeedbackByFeedbackIdentifier_FeedbackId(String feedbackId);


    List<Feedback> findAllFeedbacksByFeedbackIdentifier_FeedbackIdStartingWith(String feedbackId);

    List<Feedback> findAllFeedbacksByStatus(State status);

    List<Feedback> findAllFeedbacksByCustomerIdStartingWith(String customerId);}