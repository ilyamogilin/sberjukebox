package com.sber.jukeBox.datastore;

import com.sber.jukeBox.datastore.api.JukeBoxStore;
import com.sber.jukeBox.model.TrackEntity;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class JukeBoxStoreImpl implements JukeBoxStore {

    private static JukeBoxStoreImpl ourInstance = new JukeBoxStoreImpl();

    public static JukeBoxStoreImpl getInstance() {
        return ourInstance;
    }

    private static Map<Integer, List<TrackEntity>> tracks = new ConcurrentHashMap<>();


    //TODO think about intellectual balancing of the store
    public void addTrack(TrackEntity entity) {
        if (tracks.containsKey(entity.getUserId())) {
            List<TrackEntity> trackList = tracks.get(entity.getUserId());
            trackList.add(entity);
        }
        tracks.put(entity.getUserId(), Arrays.asList(entity));
    }

    public List<TrackEntity> getTracksById(int userId) {
        if (!tracks.containsKey(userId)) {
            throw new RuntimeException("Track with id: " + userId + " is not found");
        }
        return tracks.get(userId);
    }

    public void remove(int trackId) {
        if (!tracks.containsKey(trackId)) {
            throw new RuntimeException("Track with id: " + trackId + " has been already removed");
        }
        tracks.remove(trackId);
    }

    public Map<Integer, List<TrackEntity>> getTracks() {
        return tracks;
    }
}
