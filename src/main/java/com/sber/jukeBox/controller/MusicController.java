package com.sber.jukeBox.controller;

import com.sber.jukeBox.datastore.JukeBoxStoreImpl;
import com.sber.jukeBox.model.TrackEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class MusicController {



    private final Logger log = LoggerFactory.getLogger(getClass());

    @MessageMapping("/hello")
    @SendTo("/topic/greetings")
    public List<TrackEntity> greeting(int id){
        return JukeBoxStoreImpl.getInstance().getTracksById(id);
    }

//    @RequestMapping("/track/{id}/")
//    public List<TrackEntity> getTrack(@PathVariable("id") int id) {
//        log.info("Track: {}", JukeBoxStoreImpl.getInstance().getTracksById(id));
//
//        return JukeBoxStoreImpl.getInstance().getTracksById(id);
//    }


    @MessageMapping("/track/{trackId}")
    @SendTo("/user")
    public List<TrackEntity> getTrack(@PathVariable("trackId") int trackId) {
        log.info("get Track: {}",  JukeBoxStoreImpl.getInstance().getTracksById(trackId));

        return JukeBoxStoreImpl.getInstance().getTracksById(trackId);
    }

}