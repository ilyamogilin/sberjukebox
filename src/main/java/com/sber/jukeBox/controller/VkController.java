package com.sber.jukeBox.controller;

import com.sber.jukeBox.vk.CallbackApiHandler;
import com.vk.api.sdk.callback.CallbackApi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author FORESTER21
 */

@Controller
@RequestMapping("/olol")
public class VkController {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private CallbackApi callback;

    private static final String CONFIRMATION_CODE = "a54ed201";

    @PostMapping
    @ResponseBody
    public String verify(@RequestBody String request) {

        log.info(request);

        callback.parse(request);
        if (CallbackApiHandler.isConfirmation()) {
            CallbackApiHandler.setConfirmation(false);
            return CONFIRMATION_CODE;
        }
        return "ok";
    }
}
