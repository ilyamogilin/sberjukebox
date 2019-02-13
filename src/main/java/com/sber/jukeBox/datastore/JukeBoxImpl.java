package com.sber.jukeBox.datastore;

import com.sber.jukeBox.datastore.api.JukeBox;
import com.sber.jukeBox.json.TrackList;
import com.sber.jukeBox.model.TrackEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import static com.sber.jukeBox.json.TrackList.emptyPlaylist;

/**
 * @author FORESTER
 */
public class JukeBoxImpl implements JukeBox {

    private Map<Integer, List<TrackEntity>> usersTracksMap = new LinkedHashMap<>();

    private TrackList trackList = emptyPlaylist();

    private static final Logger log = LoggerFactory.getLogger(JukeBoxImpl.class);

    private ReadWriteLock lock = new ReentrantReadWriteLock();

    @Override
    public void addTrack(TrackEntity trackEntity) {
        lock.writeLock().lock();
        try {
            log.debug("Add track: " + trackEntity);
            if (trackEntity.getUserId() == 0) {
                throw new RuntimeException("UserId is empty");
            }
            usersTracksMap.computeIfAbsent(trackEntity.getUserId(), k -> new ArrayList<>()).add(trackEntity);
            refreshTrackList();
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public TrackList popTrackListWithNowPlaying() {
        lock.writeLock().lock();
        try {
            windTrackList(popTrack());
            return trackList;
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public TrackList getTrackList() {
        lock.readLock().lock();
        try {
            return new TrackList(trackList.getTracks());
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public boolean isEmpty() {
        lock.readLock().lock();
        try {
            return trackList.getNowPlaying() == null;
        } finally {
            lock.readLock().unlock();
        }
    }

    private void refreshTrackList() {
        trackList.setTracks(getTracksInFairOrder());
        log.debug("TrackList refreshed: " + trackList);
    }

    private List<TrackEntity> getTracksInFairOrder() {
        List<TrackEntity> tracks = new ArrayList<>();
        boolean hasMoreTracks = true;
        int i = 0;

        while (hasMoreTracks) {
            hasMoreTracks = false;
            for (List<TrackEntity> userTracks : usersTracksMap.values()) {
                if (userTracks.size() > i) {
                    tracks.add(userTracks.get(i));
                    hasMoreTracks = true;
                }
            }
            i++;
        }
        return tracks;
    }

    private void windTrackList(TrackEntity nowPlaying) {
        trackList.setNowPlaying(nowPlaying);
        if (nowPlaying == null) {
            return;
        }
        trackList.getTracks().remove(nowPlaying);
    }

    private TrackEntity popTrack() {
        if (CollectionUtils.isEmpty(trackList.getTracks())) {
            return null;
        }
        TrackEntity nowPlaying = trackList.getTracks().get(0);
        List<TrackEntity> userTracks = usersTracksMap.get(nowPlaying.getUserId());
        userTracks.remove(nowPlaying);

        updateMap(nowPlaying.getUserId());

        return nowPlaying;
    }

    private void updateMap(int userId) {
        List<TrackEntity> remainingUserTracks = usersTracksMap.remove(userId);
        if (!CollectionUtils.isEmpty(remainingUserTracks)) {
            usersTracksMap.put(userId, remainingUserTracks);
        }
    }
}
