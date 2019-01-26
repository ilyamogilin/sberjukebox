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
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;

@Controller
public class MusicController {

    @Bean
    public JukeBoxStoreImpl jukeBoxStore() {
        return JukeBoxStoreImpl.getInstance();
    }

    @Autowired
    private JukeBoxStoreImpl jukeBoxStore;

    private final Logger log = LoggerFactory.getLogger(getClass());

    @MessageMapping("/test")
    @SendTo("/topic/test")
    public @ResponseBody TrackList getTracks(){
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

        // jukeBoxStore.getTracksById()
        // or jukeBoxStore.getAllTracks()
        return trackList;
    }

}