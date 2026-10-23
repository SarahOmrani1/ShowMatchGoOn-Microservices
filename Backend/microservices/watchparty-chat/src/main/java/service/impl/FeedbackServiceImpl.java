package service.impl;

import entity.Feedback;
import repository.FeedbackRepository;
import service.FeedbackService;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class FeedbackServiceImpl implements FeedbackService {

    private final FeedbackRepository repository;

    public FeedbackServiceImpl(FeedbackRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<Feedback> getAll() {
        return repository.findAll();
    }

    @Override
    public List<Feedback> getByWatchParty(String watchPartyId) {
        return repository.findByWatchPartyId(watchPartyId);
    }

    @Override
    public Feedback create(Feedback feedback) {
        feedback.setDateFeedback(new Date());
        return repository.save(feedback);
    }

    @Override
    public Feedback update(String id, Feedback feedback) {
        feedback.setId(id);
        return repository.save(feedback);
    }

    @Override
    public void delete(String id) {
        repository.deleteById(id);
    }
}