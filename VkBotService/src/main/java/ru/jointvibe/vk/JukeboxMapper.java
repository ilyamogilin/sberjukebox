package ru.jointvibe.vk;

import com.vk.api.sdk.objects.messages.Message;

import java.util.HashMap;
import java.util.Map;

/**
 * @author FORESTER21
 */

public class JukeboxMapper {

    private Map<Integer, String> userMap = new HashMap<>();

    public boolean checkUser(Message message) {
        return userMap.containsKey(message.getUserId());
    }

    public boolean addUser(Message message) {
        try {
            userMap.put(message.getUserId(), message.getBody());
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

    public String getJukeboxIdByUser(Integer userId) {
        return userMap.get(userId);
    }
}
