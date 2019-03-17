package ru.jointvibe.vk;

import com.vk.api.sdk.callback.CallbackApi;
import com.vk.api.sdk.objects.audio.AudioFull;
import com.vk.api.sdk.objects.messages.Message;
import com.vk.api.sdk.objects.messages.MessageAttachment;
import com.vk.api.sdk.objects.messages.MessageAttachmentType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import ru.jointvibe.common.pojo.TrackEntity;
import ru.jointvibe.service.JukeboxStore;

import java.util.HashSet;
import java.util.Set;

import static ru.jointvibe.vk.MessageSender.GROUP_ID;

/**
 * @author FORESTER21
 */

@Component
@Slf4j
public class CallbackApiHandler extends CallbackApi {

    @Autowired
    private MessageSender sender;

    @Autowired
    private JukeboxStore jukeBoxStore;

    private JukeboxMapper jukeboxMapper;
    private Set<Integer> messageIds;

    private static final String BEGIN_KEYWORD = "Начать";

    private static boolean isConfirmation;

    public CallbackApiHandler() {
        jukeboxMapper = new JukeboxMapper();
        messageIds = new HashSet<>();
    }

    @Override
    public void confirmation(Integer groupId) {
        if (GROUP_ID == groupId) {
            setConfirmation(true);
        }
    }

    @Override
    public void messageNew(Integer groupId, Message message) {
        log.info("New message!!");
        Integer userId = message.getUserId();
        try {
            if (isMessageProcessed(message.getId())) {
                return;
            }
            if (BEGIN_KEYWORD.equals(message.getBody())) {
                sender.welcome(userId);
                return;
            }
            if (!jukeboxMapper.checkUser(message)) {
                if (!jukeboxMapper.addUser(message)) {
                    sender.requestJukeboxId(userId);
                    return;
                }
                sender.jukeboxIdConfirmed(userId);
            }
            processAttachments(message, userId);
        } catch (Exception e) {
            log.error("Exception while handling new message", e);
        }
    }

    private synchronized boolean isMessageProcessed(Integer messageId) {
        if (messageIds.contains(messageId)) {
            return true;
        }
        messageIds.add(messageId);
        return false;
    }

    private void processAttachments(Message message, int userId) throws Exception {
        if (!CollectionUtils.isEmpty(message.getAttachments())) {
            for (MessageAttachment attachment : message.getAttachments()) {
                if (MessageAttachmentType.AUDIO == attachment.getType()) {
                    addTrack(userId, attachment);
                }
            }
        }
    }

    private void addTrack(Integer userId, MessageAttachment attachment) throws Exception {
        AudioFull audio = attachment.getAudio();
        TrackEntity track = TrackEntity.builder()
                .userId(userId)
                .artistName(audio.getArtist())
                .trackName(audio.getTitle())
                .trackUrl(audio.getUrl())
                .jukeboxId(jukeboxMapper.getJukeboxIdByUser(userId))
                .build();
        jukeBoxStore.addTrack(track);
        sender.audioAdded(userId, track.getFullName());
    }


    public static boolean isConfirmation() {
        return isConfirmation;
    }

    public static void setConfirmation(boolean isConfirmation) {
        CallbackApiHandler.isConfirmation = isConfirmation;
    }
}
