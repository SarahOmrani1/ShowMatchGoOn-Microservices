package controller;

import entity.WatchParty;
import service.WatchPartyService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/watchparties")
public class WatchPartyController {

    private final WatchPartyService watchPartyService;

    public WatchPartyController(WatchPartyService watchPartyService) {
        this.watchPartyService = watchPartyService;
    }

    @GetMapping
    public ResponseEntity<List<WatchParty>> getAll() {
        return ResponseEntity.ok(watchPartyService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<WatchParty> getById(@PathVariable String id) {
        WatchParty wp = watchPartyService.getById(id);
        if (wp == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(wp);
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody WatchParty watchParty) {
        try {
            return new ResponseEntity<>(watchPartyService.create(watchParty), HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable String id, @RequestBody WatchParty watchParty) {
        try {
            return ResponseEntity.ok(watchPartyService.update(id, watchParty));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        watchPartyService.delete(id);
        return ResponseEntity.noContent().build();
    }
}