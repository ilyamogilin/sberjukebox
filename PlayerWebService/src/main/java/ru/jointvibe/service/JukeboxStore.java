package ru.jointvibe.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.jointvibe.common.pojo.TrackEntity;
import ru.jointvibe.common.pojo.TrackList;

@Service
public class JukeboxStore {

    @Autowired
    @LoadBalanced
    private RestTemplate restTemplate;

    private static final String SERVICE_URL = "http://jukebox-store-service";

    public TrackList popTrackListWithNowPlaying(String jukeboxId) {
        return restTemplate.getForObject(SERVICE_URL + "/pop/" + jukeboxId, TrackList.class);
    }
}
