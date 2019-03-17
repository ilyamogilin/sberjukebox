package ru.jointvibe.jukebox.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import ru.jointvibe.common.pojo.TrackList;

@Component
public class PlayerWebServiceClient {

    @Autowired
    @LoadBalanced
    private RestTemplate restTemplate;

    private static final String SERVICE_URL = "http://player-web-service";

    public void refreshTrackList(String jukeboxId, TrackList trackList){
        restTemplate.postForObject(SERVICE_URL + "/refresh/" + jukeboxId, trackList, TrackList.class);
    }
}
