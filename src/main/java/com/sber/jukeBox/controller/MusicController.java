package com.sber.jukeBox.controller;

import com.sber.jukeBox.datastore.JukeBoxStoreImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class MusicController {

    public static final String TOPIC_ENDPOINT = "/topic/test";

    @Bean
    public JukeBoxStoreImpl jukeBoxStore() {
        return JukeBoxStoreImpl.getInstance();
    }

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    private JukeBoxStoreImpl jukeBoxStore;

    private final Logger log = LoggerFactory.getLogger(getClass());

    @MessageMapping("/test/{jukeboxId}")
    @SendTo(TOPIC_ENDPOINT)
    public void getTracks(@DestinationVariable int jukeboxId) {
        log.info("getTracks() METHOD CALLED");
        simpMessagingTemplate.convertAndSend(TOPIC_ENDPOINT + "/" + jukeboxId, jukeBoxStore.getTracksWithNowPlaying(jukeboxId));
        return;
    }

}