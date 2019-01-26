package com.sber.jukeBox.datastore.api;

import com.sber.jukeBox.model.TrackEntity;

import java.util.List;

public interface JukeBoxStore {

    void addTrack(TrackEntity entity);

    List<TrackEntity> getTracksById(int trackId);

    void remove(int trackId);
}
