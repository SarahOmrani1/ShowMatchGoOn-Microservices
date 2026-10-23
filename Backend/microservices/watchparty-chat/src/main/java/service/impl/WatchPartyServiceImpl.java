package service.impl;

import entity.WatchParty;
import repository.FeedbackRepository;
import repository.WatchPartyRepository;
import service.WatchPartyService;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class WatchPartyServiceImpl implements WatchPartyService {

    private final WatchPartyRepository repository;
    private final FeedbackRepository feedbackRepository;

    public WatchPartyServiceImpl(WatchPartyRepository repository, FeedbackRepository feedbackRepository) {
        this.repository = repository;
        this.feedbackRepository = feedbackRepository;
    }

    @Override
    public List<WatchParty> getAll() {
        return repository.findAll();
    }

    @Override
    public WatchParty getById(String id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    public WatchParty create(WatchParty watchParty) {
        Date now = new Date();
        watchParty.setDateCreation(now);
        watchParty.setUpdatedAt(now);
        return repository.save(watchParty);
    }

    @Override
    public WatchParty update(String id, WatchParty watchParty) {
        watchParty.setId(id);
        watchParty.setUpdatedAt(new Date());
        return repository.save(watchParty);
    }

    @Override
    public void delete(String id) {
        feedbackRepository.deleteByWatchPartyId(id);
        repository.deleteById(id);
    }
}