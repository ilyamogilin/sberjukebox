package com.sber.jukeBox.vk;

import com.sber.jukeBox.datastore.api.JukeBoxStore;
import com.sber.jukeBox.model.TrackEntity;
import com.vk.api.sdk.callback.CallbackApi;
import com.vk.api.sdk.objects.audio.AudioFull;
import com.vk.api.sdk.objects.messages.Message;
import com.vk.api.sdk.objects.messages.MessageAttachment;
import com.vk.api.sdk.objects.messages.MessageAttachmentType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.sber.jukeBox.vk.MessageSender.GROUP_ID;
import static org.springframework.util.CollectionUtils.isEmpty;

/**
 * @author FORESTER21
 */

@Component
public class CallbackApiHandler extends CallbackApi {

    @Autowired
    private MessageSender sender;

    @Autowired
    JukeBoxStore jukeBoxStore;

    private JukeboxMapper jukeboxMapper;

    private final Logger log = LoggerFactory.getLogger(getClass());


    private static final String BEGIN_KEYWORD = "Начать";
    private static boolean isConfirmation;

    public CallbackApiHandler() {
        jukeboxMapper = new JukeboxMapper();
    }

    //    private static final Logger log = LoggerFactory

    @Override
    public void messageNew(Integer groupId, Message message) {
        log.info("New message!!");
        try {
            if (BEGIN_KEYWORD.equals(message.getBody())) {
                sender.welcome();
                return;
            }
            if (!jukeboxMapper.checkUser(message)) {
                sender.requestJukeboxId();
                return;
            }
            if (!isEmpty(message.getAttachments())) {
                for (MessageAttachment attachment : message.getAttachments()) {
                    if (MessageAttachmentType.AUDIO == attachment.getType()) {
                        addTrack(message.getUserId(), attachment);
                    }
                }
            }
        } catch (Exception e) {
            log.error("Exception while handling new message", e);
        }
    }

    private void addTrack(Integer userId, MessageAttachment attachment) {
        AudioFull audio = attachment.getAudio();
        TrackEntity track = TrackEntity.builder()
                .userId(userId)
                .jukeboxId(jukeboxMapper.getJukeboxIdByUser(userId))
                .artistName(audio.getArtist())
                .trackName(audio.getTitle())
                .trackUrl(audio.getUrl())
                .build();
        jukeBoxStore.addTrack(track);
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
