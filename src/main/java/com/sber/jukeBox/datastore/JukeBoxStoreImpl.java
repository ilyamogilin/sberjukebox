package com.sber.jukeBox.datastore;

import com.sber.jukeBox.datastore.api.JukeBox;
import com.sber.jukeBox.datastore.api.JukeBoxStore;
import com.sber.jukeBox.json.TrackList;
import com.sber.jukeBox.model.TrackEntity;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

import static com.sber.jukeBox.json.TrackList.emptyPlaylist;

/**
 * @author FORESTER
 */
@Component
public class JukeBoxStoreImpl implements JukeBoxStore {

    private Map<String, JukeBoxImpl> jukeBoxes = new HashMap<>();

    @Override
    public void addTrack(TrackEntity track) {
        if (isEmpty(track.getJukeboxId())) {
            throw new RuntimeException("JukeBoxId is empty");
        }
        JukeBox jukeBox;
        synchronized (track.getJukeboxId()) {
            jukeBox = jukeBoxes.computeIfAbsent(track.getJukeboxId(), k -> new JukeBoxImpl());
        }
        jukeBox.addTrack(track);
    }

    @Override
    public TrackList popTrackListWithNowPlaying(String jukeboxId) {
        if (!jukeBoxes.containsKey(jukeboxId)) {
            return emptyPlaylist();
        }
        return jukeBoxes.get(jukeboxId).popTrackListWithNowPlaying();
    }

    @Override
    public TrackList getTrackList(String jukeboxId) {
        if (!jukeBoxes.containsKey(jukeboxId)) {
            return emptyPlaylist();
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
