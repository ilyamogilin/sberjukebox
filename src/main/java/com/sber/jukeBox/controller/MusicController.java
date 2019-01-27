package com.sber.jukeBox.controller;

import com.sber.jukeBox.datastore.JukeBoxStoreImpl;
import com.sber.jukeBox.json.TrackList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class MusicController {

    public static final String TOPIC_ENDPOINT = "/topic/test";

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    private JukeBoxStoreImpl jukeBoxStore;

    private final Logger log = LoggerFactory.getLogger(getClass());

    @MessageMapping("/test/{jukeboxId}")
    public void getTracks(@DestinationVariable int jukeboxId) {
        log.info("getTracks() METHOD CALLED");
        TrackList trackList = jukeBoxStore.getTracksWithNowPlaying(jukeboxId);
        if (trackList.getNowPlaying() == null){
            jukeBoxStore.setPlayerEmpty(jukeboxId, true);
        }
        simpMessagingTemplate.convertAndSend(TOPIC_ENDPOINT + "/" + jukeboxId, trackList);
        return;
    }

    @MessageMapping("/test/handshake")
    public String doHandshake(){
        return "ok";
    }

}