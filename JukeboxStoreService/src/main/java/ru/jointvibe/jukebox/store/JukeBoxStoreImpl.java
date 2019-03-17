package ru.jointvibe.jukebox.store;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import ru.jointvibe.common.pojo.TrackEntity;
import ru.jointvibe.common.pojo.TrackList;
import ru.jointvibe.jukebox.store.api.JukeBox;
import ru.jointvibe.jukebox.store.api.JukeBoxStore;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @author FORESTER
 */
@Component
public class JukeBoxStoreImpl implements JukeBoxStore {

    @Autowired
    private JukeboxModificationListener listener;

    private Map<String, JukeBoxImpl> jukeBoxes = new HashMap<>();

    @Override
    public void addTrack(TrackEntity track) {
        String jukeboxId = track.getJukeboxId();
        if (StringUtils.isEmpty(jukeboxId)) {
            throw new RuntimeException("JukeBoxId is empty");
        }
        JukeBox jukeBox;
        //TODO ???
        synchronized (jukeboxId) {
            jukeBox = jukeBoxes.computeIfAbsent(jukeboxId, k -> new JukeBoxImpl());
        }
        jukeBox.addTrack(track);
        listener.onTrackAdded(jukeboxId);
    }

    @Override
    public TrackList popTrackListWithNowPlaying(String jukeboxId) {
        if (!jukeBoxes.containsKey(jukeboxId)) {
            return TrackList.emptyPlaylist();
        }
        return jukeBoxes.get(jukeboxId).popTrackListWithNowPlaying();
    }

    @Override
    public TrackList getTrackList(String jukeboxId) {
        if (!jukeBoxes.containsKey(jukeboxId)) {
            return TrackList.emptyPlaylist();
        }
        return jukeBoxes.get(jukeboxId).getTrackList();
    }

    @Override
    public boolean isEmpty(String jukeboxId) {
        if (!jukeBoxes.containsKey(jukeboxId)) {
            return true;
        }
        return jukeBoxes.get(jukeboxId).isEmpty();
    }

    @Override
    public TrackList getTrackListOrPopIfEmpty(String jukeboxId) {
        return isEmpty(jukeboxId) ? popTrackListWithNowPlaying(jukeboxId) : getTrackList(jukeboxId);
    }
}
