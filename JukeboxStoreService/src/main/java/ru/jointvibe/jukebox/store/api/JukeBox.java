package ru.jointvibe.jukebox.store.api;

import ru.jointvibe.common.pojo.TrackEntity;
import ru.jointvibe.common.pojo.TrackList;

/**
 * @author FORESTER
 */
public interface JukeBox {

    void addTrack(TrackEntity trackEntity);

    TrackList popTrackListWithNowPlaying();

    TrackList getTrackList();

    boolean isEmpty();
}
