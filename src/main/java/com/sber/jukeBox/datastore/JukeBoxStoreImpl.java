package com.sber.jukeBox.datastore;

import com.sber.jukeBox.datastore.api.JukeBoxStore;
import com.sber.jukeBox.json.TrackList;
import com.sber.jukeBox.model.TrackEntity;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class JukeBoxStoreImpl implements JukeBoxStore {

    private static JukeBoxStoreImpl ourInstance = new JukeBoxStoreImpl();

    public static JukeBoxStoreImpl getInstance() {
        return ourInstance;
    }

    private static Map<Integer, List<TrackEntity>> tracks = new ConcurrentHashMap<>();


    //TODO think about intellectual balancing of the store
    public void addTrack(TrackEntity entity) {
        if (tracks.containsKey(entity.getUserId())) {
            List<TrackEntity> trackList = tracks.get(entity.getUserId());
            trackList.add(entity);
        }
        tracks.put(entity.getUserId(), Arrays.asList(entity));
    }

    public List<TrackEntity> getTracksById(int userId) {
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
        List<TrackEntity> allTracks = new ArrayList<>();
        for (Integer userId : tracks.keySet()) {
            allTracks.addAll(getTracksById(userId));
        }
        return allTracks;
    }

    public TrackList getTracksListMock(){
        TrackList trackList = new TrackList();
        TrackEntity trackEntity = TrackEntity.builder()
                .trackName("track")
                .trackUrl("https://cs1-76v4.vkuseraudio.net/p17/641974acff62ce.mp3?extra=omaFKfh6z_pP8x4kSaFY_1P3w5lrS8WClK_T42z391nAScE8Tn7zhsOBGx6Sb5tZeBlU2rZbdtlR19SL1dWa7g1u-rdOf8-YjquJA7VQYX5J5zVgioPkwz0OBcDApmktg4OjoAoGSTa4oQGKv8hwjrzA")
                .artistName("artist")
                .userId(321)
                .build();

        trackList.setNowPlaying(trackEntity);
        ArrayList<TrackEntity> list = new ArrayList<>();
        list.add(trackEntity);
        list.add(trackEntity);
        trackList.setTrackList(list);
        return trackList;
    }
}
