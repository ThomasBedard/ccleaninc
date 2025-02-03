package com.ccleaninc.cclean.feedbacksubdomain.presentationlayer;

import com.ccleaninc.cclean.feedbacksubdomain.businesslayer.FeedbackService;
import com.ccleaninc.cclean.utils.exceptions.InvalidInputException;
import com.ccleaninc.cclean.utils.exceptions.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("api/v1")
@CrossOrigin(origins = {
        "http://localhost:5173",
        "https://ccleaninc.vercel.app"
})
public class FeedbackController {
    private final FeedbackService feedbackService;

    public FeedbackController(FeedbackService feedbackService) {
        this.feedbackService = feedbackService;
    }

    @GetMapping("/feedbacks")
    public List<FeedbackResponseModel> getAllFeedbackThreads(@RequestParam(required = false) String feedbackId,
                                                             @RequestParam(required = false) String customerId,
                                                             @RequestParam(required = false) String status) {
        return feedbackService.getAllFeedback(feedbackId, customerId, status);
    }

    @GetMapping("/feedbacks/{feedbackId}")
    public ResponseEntity<FeedbackResponseModel> getFeedbackById(@PathVariable String feedbackId) {
        try {
            FeedbackResponseModel feedback = feedbackService.getFeedbackByFeedbackId(feedbackId);
            return ResponseEntity.ok(feedback);
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/feedback")
    public ResponseEntity<FeedbackResponseModel> addFeedback(@RequestBody FeedbackRequestModel feedbackRequestModel) {
        try {
            FeedbackResponseModel response = feedbackService.addFeedback(feedbackRequestModel);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (InvalidInputException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @DeleteMapping("/feedbacks/{feedbackId}")
    public ResponseEntity<Void> deleteFeedbackById(@PathVariable String feedbackId) {
        feedbackService.removeFeedback(feedbackId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PatchMapping("/feedbacks/{feedbackId}/publish")
    public ResponseEntity<FeedbackResponseModel> updateFeedbackState(@PathVariable String feedbackId, @RequestBody String status) {
        try {
            FeedbackResponseModel updatedFeedback = feedbackService.updateFeedbackState(feedbackId, status);
            return ResponseEntity.ok(updatedFeedback);
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}


