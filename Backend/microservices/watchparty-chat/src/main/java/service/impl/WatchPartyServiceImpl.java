package service.impl;

import client.ContentClient;
import entity.WatchParty;
import feign.FeignException;
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
    private final ContentClient contentClient;

    public WatchPartyServiceImpl(WatchPartyRepository repository, FeedbackRepository feedbackRepository, ContentClient contentClient) {
        this.repository = repository;
        this.feedbackRepository = feedbackRepository;
        this.contentClient = contentClient;
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
        try {
            contentClient.getContentById(watchParty.getContenuId());
        } catch (FeignException.NotFound e) {
            throw new IllegalArgumentException("Contenu introuvable : " + watchParty.getContenuId());
        } catch (FeignException e) {
            throw new IllegalArgumentException("Service content indisponible");
        }
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