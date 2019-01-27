package com.sber.jukeBox.vk;

import com.vk.api.sdk.client.TransportClient;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.GroupActor;
import com.vk.api.sdk.httpclient.HttpTransportClient;
import org.springframework.stereotype.Component;

import static java.lang.String.format;

/**
 * @author FORESTER21
 */

@Component
public class MessageSender {

    private static final String ACCESS_TOKEN = "8fa7502f32f2a12a42aa9c192b7eb075ac0341ca4ae5a12f61b4f51aed0d343f28aa5f8a1a2c62dbe532d";
    private static final String WELCOME_MESSAGE = "Добро пожаловать!";
    private static final String REQUEST_ID_MESSAGE = "Пожалуйста, перед отправкой аудиозаписей введите ваш идентификатор.";
    private static final String CONFIRMATION_MESSAGE = "Теперь вы можете отправить аудио для добавление в очередь.";
    private static final String TRACK_ADDED_MESSAGE = "Аудиозапись \"%s\" добавлена в очередь!";
    private static final String KEYBOARD_JSON = "{\n" +
            "    \"one_time\": false, \n" +
            "    \"buttons\": [ \n" +
            "      [{ \n" +
            "        \"action\": { \n" +
            "          \"type\": \"text\", \n" +
            "          \"payload\": \"{\\\"button\\\": \\\"1\\\"}\", \n" +
            "          \"label\": \"Сбер\" \n" +
            "        }, \n" +
            "        \"color\": \"primary\" \n" +
            "      }, \n" +
            "     { \n" +
            "        \"action\": { \n" +
            "          \"type\": \"text\", \n" +
            "          \"payload\": \"{\\\"button\\\": \\\"2\\\"}\", \n" +
            "          \"label\": \"Qiwi\" \n" +
            "        }, \n" +
            "        \"color\": \"default\" \n" +
            "      }], \n" +
            "      [{ \n" +
            "        \"action\": { \n" +
            "          \"type\": \"text\", \n" +
            "          \"payload\": \"{\\\"button\\\": \\\"3\\\"}\", \n" +
            "          \"label\": \"VkPay\" \n" +
            "        }, \n" +
            "        \"color\": \"default\" \n" +
            "      }, \n" +
            "     { \n" +
            "        \"action\": { \n" +
            "          \"type\": \"text\", \n" +
            "          \"payload\": \"{\\\"button\\\": \\\"4\\\"}\", \n" +
            "          \"label\": \"Visa/Mastercard\" \n" +
            "        }, \n" +
            "        \"color\": \"default\" \n" +
            "      }] \n" +
            "    ] \n" +
            "  } ";

    static final int GROUP_ID = 177315584;


    private VkApiClient vk;
    private GroupActor groupActor;

    public MessageSender() {
        TransportClient transportClient = HttpTransportClient.getInstance();
        vk = new VkApiClient(transportClient);
        groupActor = new GroupActor(GROUP_ID, ACCESS_TOKEN);
    }

    public void welcome(Integer userId) throws Exception {
        send(userId, WELCOME_MESSAGE + "\n" + REQUEST_ID_MESSAGE);
    }

    public void requestJukeboxId(Integer userId) throws Exception {
        send(userId, REQUEST_ID_MESSAGE);
    }

    public void jukeboxIdConfirmed(Integer userId) throws Exception {
        send(userId, CONFIRMATION_MESSAGE);
    }

    public void audioAdded(Integer userId, String trackFullName) throws Exception {
        send(userId, format(TRACK_ADDED_MESSAGE, trackFullName));
    }

    private void send(Integer toUserId, String message) throws Exception {
        vk.messages()
                .send(groupActor)
                .userId(toUserId)
                .message(message)
                .execute();
    }

    private void sendKeyboard(Integer toUserId, String keyboardJson) throws Exception {
        vk.messages()
                .send(groupActor)
                .userId(toUserId)
                .unsafeParam("keyboard", keyboardJson)
                .execute();
    }

    public void getPaymentChoice(Integer userId) throws Exception {

        sendKeyboard(userId, KEYBOARD_JSON);
    }
}
