package service;

import entity.Feedback;

import java.util.List;

public interface FeedbackService {

    List<Feedback> getAll();

    List<Feedback> getByWatchParty(String watchPartyId);

    Feedback create(Feedback feedback);

    Feedback update(String id, Feedback feedback);

    void delete(String id);
}