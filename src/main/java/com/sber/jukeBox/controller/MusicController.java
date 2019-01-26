package com.sber.jukeBox.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MusicController {

    @RequestMapping("/test")
    public String test() {
        return "123";
    }

}
