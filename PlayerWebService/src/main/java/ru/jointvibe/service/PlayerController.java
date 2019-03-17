package ru.jointvibe.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.jointvibe.common.pojo.TrackList;
import ru.jointvibe.player.MusicController;

@SuppressWarnings("unused")
@RestController
public class PlayerController {

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @PostMapping("/refresh/{jukeboxId}")
    public void refreshTrackList(TrackList trackList, @PathVariable String jukeboxId) {
        simpMessagingTemplate.convertAndSend(MusicController.TOPIC_ENDPOINT + "/" + jukeboxId, trackList);
    }

}
