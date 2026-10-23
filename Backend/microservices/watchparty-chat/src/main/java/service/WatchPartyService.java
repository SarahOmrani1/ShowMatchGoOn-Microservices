package service;

import entity.WatchParty;

import java.util.List;

public interface WatchPartyService {

    List<WatchParty> getAll();

    WatchParty getById(String id);

    WatchParty create(WatchParty watchParty);

    WatchParty update(String id, WatchParty watchParty);

    void delete(String id);
}