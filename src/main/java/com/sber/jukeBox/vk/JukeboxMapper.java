package com.sber.jukeBox.vk;

import com.vk.api.sdk.objects.messages.Message;

import java.util.HashMap;
import java.util.Map;

public class JukeboxMapper {

    private Map<Integer, Integer> userMap = new HashMap<>();

    public boolean checkUser(Message message){
        if (userMap.containsKey(message.getUserId())) {
            return true;
        }

        try{
            userMap.put(message.getUserId(), Integer.parseInt(message.getBody()));
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

    public Integer getJukeboxIdByUser(Integer userId){
        return userMap.get(userId);
    }
}
