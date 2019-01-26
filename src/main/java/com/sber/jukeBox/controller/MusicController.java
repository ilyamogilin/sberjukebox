package com.sber.jukeBox.controller;

import com.sber.jukeBox.datastore.JukeBoxStoreImpl;
import com.sber.jukeBox.model.TrackEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

@Controller
public class MusicController {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @RequestMapping("/allTracks")
    public String getAllTracks() {
        log.info("get Track1: {}", JukeBoxStoreImpl.getInstance().getTracks());

        Map<Integer, TrackEntity> tracks = JukeBoxStoreImpl.getInstance().getTracks();

        return tracks.toString();
    }

    @RequestMapping("/track/{id}/")
    public TrackEntity getTrack(@PathVariable("id") int id) {
        log.info("get Track1: {}", JukeBoxStoreImpl.getInstance().getTrack(id));

        return JukeBoxStoreImpl.getInstance().getTrack(id);
    }


//    @MessageMapping("/track/{trackId}")
//    @SendTo("/user")
//    public TrackEntity getTrack(@PathVariable("trackId") int trackId) {
//        log.info("get Track: {}",   store.getTrack(trackId));
//
//        return store.getTrack(trackId);
//    }

}