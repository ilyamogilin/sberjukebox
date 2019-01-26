package com.sber.jukeBox.vk;

import com.sber.jukeBox.controller.MusicController;
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

import java.util.HashSet;
import java.util.Set;

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
    private JukeBoxStore jukeBoxStore;

    private JukeboxMapper jukeboxMapper;
    private Set<Integer> messageIds;

    private final Logger log = LoggerFactory.getLogger(getClass());


    private static final String BEGIN_KEYWORD = "Начать";
    private static boolean isConfirmation;

    public CallbackApiHandler() {
        jukeboxMapper = new JukeboxMapper();
        messageIds = new HashSet<>();
    }

    @Override
    public void messageNew(Integer groupId, Message message) {
        log.info("New message!!");
        Integer userId = message.getUserId();
        try {
            if (isMessageProcessed(message.getId())){
                return;
            }
            if (BEGIN_KEYWORD.equals(message.getBody())) {
                sender.welcome(userId);
                return;
            }
            if (!jukeboxMapper.checkUser(message)) {
                if (!jukeboxMapper.addUser(message)){
                    sender.requestJukeboxId(userId);
                    return;
                }
                sender.jukeboxIdConfirmed(userId);
            }
            if (!isEmpty(message.getAttachments())) {
                for (MessageAttachment attachment : message.getAttachments()) {
                    if (MessageAttachmentType.AUDIO == attachment.getType()) {
                        addTrack(userId, attachment);
                    }
                }
                refreshPlaylist(jukeboxMapper.getJukeboxIdByUser(userId));
            }
        } catch (Exception e) {
            log.error("Exception while handling new message", e);
        }
    }

    private synchronized boolean isMessageProcessed(Integer messageId){
        if (messageIds.contains(messageId)){
            return true;
        }
        messageIds.add(messageId);
        return false;
    }

    private void addTrack(Integer userId, MessageAttachment attachment) throws Exception {
        AudioFull audio = attachment.getAudio();
        TrackEntity track = TrackEntity.builder()
                .userId(userId)
                .artistName(audio.getArtist())
                .trackName(audio.getTitle())
                .trackUrl(audio.getUrl())
                .build();
        jukeBoxStore.addTrack(track);
        sender.audioAdded(userId, track.getFullName());
    }

    private void refreshPlaylist(Integer jukeboxId){
        //TODO update playlist
    };

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
