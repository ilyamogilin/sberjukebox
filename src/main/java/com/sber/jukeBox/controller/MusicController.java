package com.sber.jukeBox.controller;

import com.sber.jukeBox.datastore.JukeBoxStoreImpl;
import com.sber.jukeBox.model.TrackEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MusicController {

    @RequestMapping("/test")
    public String test() {
        return "123";
    }

//    @RequestMapping("/")
//    public TrackEntity viewAllTracks() {
//
//    }

}
