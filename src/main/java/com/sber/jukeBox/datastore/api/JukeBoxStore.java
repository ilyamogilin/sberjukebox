package com.sber.jukeBox.datastore.api;

import com.sber.jukeBox.json.TrackList;
import com.sber.jukeBox.model.TrackEntity;

/**
 * @author FORESTER
 */
public interface JukeBoxStore {

    void addTrack(TrackEntity entity);

    TrackList popTrackListWithNowPlaying(String jukeboxId);

    TrackList getTrackList(String jukeboxId);

    boolean isEmpty(String jukeboxId);

    TrackList getTrackListOrPopIfEmpty(String jukeboxId);
}
