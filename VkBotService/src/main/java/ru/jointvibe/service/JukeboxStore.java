package ru.jointvibe.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.jointvibe.common.pojo.TrackEntity;

@Service
public class JukeboxStore {

    @Autowired
    @LoadBalanced
    private RestTemplate restTemplate;

    private static final String SERVICE_URL = "http://jukebox-store-service";

    //TODO что если не добавился - check errors
    public void addTrack(TrackEntity track) {
        restTemplate.postForObject(SERVICE_URL + "/add", track, TrackEntity.class);
    }


}
