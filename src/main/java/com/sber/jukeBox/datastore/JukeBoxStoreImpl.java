package com.sber.jukeBox.datastore;

import com.sber.jukeBox.datastore.api.JukeBoxStore;
import com.sber.jukeBox.json.TrackList;
import com.sber.jukeBox.model.TrackEntity;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class JukeBoxStoreImpl implements JukeBoxStore {

    private Map<Integer, Boolean> isPlayerEmptyMap = new HashMap<>();
    private Map<Integer, List<TrackEntity>> trackStore = new ConcurrentHashMap<>();
    private TrackList trackList;

    public void addTrack(TrackEntity track) {
        trackStore.computeIfAbsent(track.getJukeboxId(), k -> new ArrayList<>()).add(track);
        isPlayerEmptyMap.computeIfAbsent(track.getJukeboxId(), k -> true);
    }

    public TrackList getTracksWithNowPlaying(int jukeboxId) {
        if (!trackStore.containsKey(jukeboxId)) {
            return emptyTrackList();
        }
        List<TrackEntity> playlist = trackStore.get(jukeboxId);
        if (playlist.isEmpty()){
            return emptyTrackList();
        }
        TrackEntity nowPlaying = playlist.get(0);
        playlist.remove(nowPlaying);
        isPlayerEmptyMap.replace(jukeboxId, playlist.isEmpty());
        return TrackList.builder().nowPlaying(nowPlaying).trackList(playlist).build();
    }

    private TrackList emptyTrackList(){
        return TrackList.builder().build();
    }

    public TrackList getAllTracks(int jukeboxId) {
        if (!trackStore.containsKey(jukeboxId)) {
            throw new RuntimeException("Track List with id: " + jukeboxId + " is not found");
        }
        return TrackList.builder().trackList(trackStore.get(jukeboxId)).build();
    }

    public boolean isPlayerEmpty(int jukeboxId){
        if (isPlayerEmptyMap.containsKey(jukeboxId)) {
            return isPlayerEmptyMap.get(jukeboxId);
        }
        return true;
    }

    public void setPlayerEmpty(int jukeboxId, boolean isEmpty){
        if (isPlayerEmptyMap.containsKey(jukeboxId)){
            isPlayerEmptyMap.replace(jukeboxId, isEmpty);
        }
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
