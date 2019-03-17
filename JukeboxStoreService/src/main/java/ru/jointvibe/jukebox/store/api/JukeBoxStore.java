package ru.jointvibe.jukebox.store.api;

import ru.jointvibe.common.pojo.TrackEntity;
import ru.jointvibe.common.pojo.TrackList;

/**
 * @author FORESTER
 */
public interface JukeBoxStore {

    void addTrack(TrackEntity entity);

    TrackList popTrackListWithNowPlaying(String jukeboxId);

    //TODO мб удалить
    TrackList getTrackList(String jukeboxId);

    //TODO мб удалить
    boolean isEmpty(String jukeboxId);

    TrackList getTrackListOrPopIfEmpty(String jukeboxId);
}
