package controller;

import entity.Feedback;
import service.FeedbackService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/feedback")
@CrossOrigin(origins = "http://localhost:4200")
public class FeedbackController {

    private final FeedbackService feedbackService;

    public FeedbackController(FeedbackService feedbackService) {
        this.feedbackService = feedbackService;
    }

    @GetMapping
    public ResponseEntity<List<Feedback>> getAll() {
        return ResponseEntity.ok(feedbackService.getAll());
    }

    @GetMapping("/watchparty/{watchPartyId}")
    public ResponseEntity<List<Feedback>> getByWatchParty(@PathVariable String watchPartyId) {
        return ResponseEntity.ok(feedbackService.getByWatchParty(watchPartyId));
    }

    @PostMapping
    public ResponseEntity<Feedback> create(@RequestBody Feedback feedback) {
        return new ResponseEntity<>(feedbackService.create(feedback), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Feedback> update(@PathVariable String id, @RequestBody Feedback feedback) {
        return ResponseEntity.ok(feedbackService.update(id, feedback));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        feedbackService.delete(id);
        return ResponseEntity.noContent().build();
    }
}