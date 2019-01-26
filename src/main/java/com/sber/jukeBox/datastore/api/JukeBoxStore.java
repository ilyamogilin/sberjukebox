package com.sber.jukeBox.datastore.api;

import com.sber.jukeBox.model.TrackEntity;

public interface JukeBoxStore {

    void addTrack(TrackEntity entity);

    TrackEntity getTrack(int trackId);

    void remove(int trackId);
}
