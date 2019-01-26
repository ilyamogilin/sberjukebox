package com.sber.jukeBox.vk;

import com.vk.api.sdk.callback.CallbackApi;
import com.vk.api.sdk.objects.messages.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.sber.jukeBox.vk.MessageSender.GROUP_ID;

/**
 * @author FORESTER21
 */

@Component
public class CallbackApiHandler extends CallbackApi {

    @Autowired
    private MessageSender sender;

    private JukeboxMapper jukeboxMapper;

    private static final String BEGIN_KEYWORD = "Начать";
    private static boolean isConfirmation;

    public CallbackApiHandler() {
        jukeboxMapper = new JukeboxMapper();
    }

    //    private static final Logger log = LoggerFactory

    @Override
    public void messageNew(Integer groupId, Message message) {
        System.out.println("NEW MESSAGE!!!");
        try {
            if (BEGIN_KEYWORD.equals(message.getBody())) {
                sender.welcome();
                return;
            }
            if (!jukeboxMapper.checkUser(message)) {
                sender.requestJukeboxId();
                return;
            }
//            if (!isEmpty(message.getAttachments())) {
//                queueService.addTracks(message.getUserId(), message.getAttachments());
//            }
        } catch (Exception e) {
            // add logging
        }
    }

    @Override
    public void confirmation(Integer groupId) {
        if (GROUP_ID == groupId) {
            setConfirmation(true);
        }
    }

    public static boolean isConfirmation() {
        return isConfirmation;
    }

    public static void setConfirmation(boolean isConfirmation) {
        CallbackApiHandler.isConfirmation = isConfirmation;
    }
}
