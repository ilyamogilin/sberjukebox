package com.sber.jukeBox.datastore;

import com.sber.jukeBox.datastore.api.JukeBoxStore;
import com.sber.jukeBox.model.TrackEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class JukeBoxStoreImpl implements JukeBoxStore {

    private static Map<Integer, TrackEntity> tracks = new ConcurrentHashMap<>();

    //TODO think about intellectual balancing of the store
    public void addTrack(TrackEntity entity) {
        tracks.put(entity.getUserId(), entity);
    }

    public TrackEntity getTrack(int userId) {
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

    public List<TrackEntity> getAllTracks() {
        return tracks.values().stream().collect(Collectors.toList());
    }
}
