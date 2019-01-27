package com.sber.jukeBox.datastore.api;

import com.sber.jukeBox.json.TrackList;
import com.sber.jukeBox.model.TrackEntity;

public interface JukeBoxStore {

    void addTrack(TrackEntity entity);

    TrackList getTracksWithNowPlaying(int jukeboxId);

    TrackList getAllTracks(int jukeboxId);
}
