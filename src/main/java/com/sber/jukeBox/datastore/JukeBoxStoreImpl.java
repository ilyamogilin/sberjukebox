package com.sber.jukeBox.datastore;

import com.sber.jukeBox.datastore.api.JukeBoxStore;
import com.sber.jukeBox.json.TrackList;
import com.sber.jukeBox.model.TrackEntity;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class JukeBoxStoreImpl implements JukeBoxStore {

    private static JukeBoxStoreImpl ourInstance = new JukeBoxStoreImpl();

    public static JukeBoxStoreImpl getInstance() {
        return ourInstance;
    }

    private static Map<Integer, List<TrackEntity>> trackStore = new ConcurrentHashMap<>();
    private TrackList trackList;

    public void addTrack(TrackEntity track) {
        trackStore.computeIfAbsent(track.getJukeboxId(), k -> new ArrayList<>()).add(track);
    }

    public TrackList getTracksById(int jukeboxId) {
        if (!trackStore.containsKey(jukeboxId)) {
            throw new RuntimeException("Track List with id: " + jukeboxId + " is not found");
        }
        List<TrackEntity> playlist = trackStore.get(jukeboxId);
        TrackEntity nowPlaying = playlist.get(0);
        playlist.remove(nowPlaying);
        return TrackList.builder().nowPlaying(nowPlaying).trackList(playlist).build();
    }


}
