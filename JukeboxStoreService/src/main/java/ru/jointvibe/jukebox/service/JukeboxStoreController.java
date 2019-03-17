package ru.jointvibe.jukebox.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.jointvibe.common.pojo.TrackEntity;
import ru.jointvibe.common.pojo.TrackList;
import ru.jointvibe.jukebox.store.api.JukeBoxStore;

@SuppressWarnings("unused")
@RestController
public class JukeboxStoreController {

    @Autowired
    JukeBoxStore jukeBoxStore;

    @PostMapping("/add")
    void addTrack(@RequestBody TrackEntity entity) {
        jukeBoxStore.addTrack(entity);
    }

    @GetMapping("/pop/{jukeboxId}")
    TrackList popTrackListWithNowPlaying(@PathVariable String jukeboxId) {
        return jukeBoxStore.popTrackListWithNowPlaying(jukeboxId);
    }

    @GetMapping("/get/{jukeboxId}")
    TrackList getTrackList(@PathVariable String jukeboxId) {
        return jukeBoxStore.getTrackList(jukeboxId);
    }

    @GetMapping("/empty/{jukeboxId}")
    boolean isEmpty(@PathVariable String jukeboxId) {
        return jukeBoxStore.isEmpty(jukeboxId);
    }

    @GetMapping("/getOrPop/{jukeboxId}")
    TrackList getTrackListOrPopIfEmpty(@PathVariable String jukeboxId) {
        return jukeBoxStore.getTrackListOrPopIfEmpty(jukeboxId);
    }
}
