package com.sber.jukeBox.datastore.api;

import com.sber.jukeBox.json.TrackList;
import com.sber.jukeBox.model.TrackEntity;

/**
 * @author FORESTER
 */
public interface JukeBox {

    void addTrack(TrackEntity trackEntity);

    TrackList popTrackListWithNowPlaying();

    TrackList getTrackList();

    boolean isEmpty();
}
