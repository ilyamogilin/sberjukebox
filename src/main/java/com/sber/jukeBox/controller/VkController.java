package com.sber.jukeBox.controller;

import com.sber.jukeBox.vk.CallbackApiHandler;
import com.vk.api.sdk.callback.CallbackApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/")
public class VkController {

    @Autowired
    private CallbackApi callback;

    private static final String CONFIRMATION_CODE = "a54ed201";

//    @GetMapping
//    public String getStartPage(){
//        System.out.println("income!");
//        return "index.html";
//    }

    @PostMapping
    @ResponseBody
    public String verify(@RequestBody String request) {
        System.out.println(request);
        callback.parse(request);
        if (CallbackApiHandler.isConfirmation()){
            CallbackApiHandler.setConfirmation(false);
            return CONFIRMATION_CODE;
        }
        return "ok";
    }
}
