package com.sber.jukeBox.vk;

import com.vk.api.sdk.client.TransportClient;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.GroupActor;
import com.vk.api.sdk.httpclient.HttpTransportClient;
import org.springframework.stereotype.Component;

/**
 * @author FORESTER21
 */

@Component
public class MessageSender {

    private static final String ACCESS_TOKEN = "8fa7502f32f2a12a42aa9c192b7eb075ac0341ca4ae5a12f61b4f51aed0d343f28aa5f8a1a2c62dbe532d";
//    private static final String KEYBOARD_PARAM = "keyboard";

    private static final String WELCOME_MESSAGE = "Добро пожаловать!";
    private static final String REQUEST_ID_MESSAGE = "Пожалуйста, перед отправкой аудиозаписей введите ваш идентификатор.";

    static final int GROUP_ID = 177315584;


    private VkApiClient vk;
    private GroupActor groupActor;

    public MessageSender() {
        TransportClient transportClient = HttpTransportClient.getInstance();
        vk = new VkApiClient(transportClient);
        groupActor = new GroupActor(GROUP_ID, ACCESS_TOKEN);
    }

    public void welcome() throws Exception {
        send(WELCOME_MESSAGE);
    }

    public void requestJukeboxId() throws Exception {
        send(REQUEST_ID_MESSAGE);
    }

    private void send(String message) throws Exception {
        vk.messages()
                .send(groupActor)
                .userId(58559317)
                .message(message)
                .execute();
    }

//    public void enableKeyboard() {
//        try {
//            vk.messages()
//                    .send(groupActor)
//                    .userId(58559317)
//                    .message("Welcome!")
//                    .unsafeParam(KEYBOARD_PARAM, getKeyboardJSON())
//                    .execute();
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//    private String getKeyboardJSON() {
//        try {
//            return IOUtils.toString(getClass().getResourceAsStream("/keyboard.json"), "UTF-8");
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//    }
}
