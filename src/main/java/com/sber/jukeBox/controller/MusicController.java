package com.sber.jukeBox.controller;

import com.sber.jukeBox.datastore.JukeBoxStoreImpl;
import com.sber.jukeBox.json.TrackList;
import com.sber.jukeBox.model.TrackEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

@Controller
public class MusicController {

    @Bean
    public JukeBoxStoreImpl jukeBoxStore() {
        return JukeBoxStoreImpl.getInstance();
    }

    @Autowired
    private JukeBoxStoreImpl jukeBoxStore;

    private final Logger log = LoggerFactory.getLogger(getClass());

    @MessageMapping("/hello")
    @SendTo("/topic/greetings")
    public List<TrackEntity> greeting(int id){
        return jukeBoxStore.getTracksById(id);
    }


    @MessageMapping("/test")
    @SendTo("/topic/test")
    public @ResponseBody TrackList getTracks(){
        log.error("METHOD CALL");
        System.out.println("METH CALL");
        TrackList trackList = new TrackList();
        TrackEntity trackEntity = TrackEntity.builder()
                .trackName("track")
                .trackUrl("url")
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


    @MessageMapping("/track/{trackId}")
    @SendTo("/user")
    public List<TrackEntity> getTrack(@PathVariable("trackId") int trackId) {
        log.info("get Track: {}",  JukeBoxStoreImpl.getInstance().getTracksById(trackId));

        return jukeBoxStore.getTracksById(trackId);
    }

}