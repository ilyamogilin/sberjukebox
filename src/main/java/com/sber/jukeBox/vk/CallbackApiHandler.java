package com.sber.jukeBox.vk;

import com.sber.jukeBox.datastore.InvoiceList;
import com.sber.jukeBox.datastore.JukeBoxStoreImpl;
import com.sber.jukeBox.model.Invoice;
import com.sber.jukeBox.model.TrackEntity;
import com.vk.api.sdk.callback.CallbackApi;
import com.vk.api.sdk.objects.audio.AudioFull;
import com.vk.api.sdk.objects.messages.Message;
import com.vk.api.sdk.objects.messages.MessageAttachment;
import com.vk.api.sdk.objects.messages.MessageAttachmentType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static com.sber.jukeBox.controller.MusicController.TOPIC_ENDPOINT;
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
    private JukeBoxStoreImpl jukeBoxStore;

    @Autowired
    private InvoiceList invoiceList;

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    private JukeboxMapper jukeboxMapper;
    private Set<Integer> messageIds;

    private final Logger log = LoggerFactory.getLogger(getClass());


    private static final String BEGIN_KEYWORD = "Начать";
    private static final String PAYMENT_KEYWORD = "Оплатить";

    private static final Map<String, Integer> PAYMENT_CHOICE = new HashMap<String, Integer>() {{
        put("Sberbank", 1);
        put("Qiwi", 2);
        put("Visa/Mastercard", 3);
        put("VkPay", 4);
    }};

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

//        if (isMessageProcessed(message.getId())) {
//            return;
//        }

        try {
            switch (message.getBody()) {
                case BEGIN_KEYWORD:
                    sender.welcome(userId);
                    return;
                case PAYMENT_KEYWORD:
                    askPaymentCode(userId);
                    return;
            }
        } catch (Exception e) {
            log.debug("Exception while handling new message", e);
            throw new RuntimeException("Failed to start messaging process", e);
        }

        PAYMENT_CHOICE.computeIfPresent(message.getBody(), (key, value) -> {
            Invoice invoice = new Invoice(message.getActionEmail(),
                    message.getUserId(),
                    Invoice.Status.Wait, value);
            int invoiceId = invoiceList.addInvoice(invoice);
            try {
                sender.sendInvoice(invoice, userId, invoiceId);
            } catch (Exception e) {
                log.debug("Exception while handling new message", e);
                throw new RuntimeException("User cannot be reached.", e);
            }
            return value;
        });

        try {
            if (!jukeboxMapper.checkUser(message)) {
                if (!jukeboxMapper.addUser(message)) {
                    sender.requestJukeboxId(userId);
                    return;
                }
                sender.jukeboxIdConfirmed(userId);
            }
            processAttachments(message, userId);
        } catch (Exception e) {
            log.debug("Exception while handling new message", e);
        }
    }

    private void askPaymentCode(int userId) throws Exception {

        sender.getPaymentChoice(userId);
    }

    //TODO think about is this method so necessary
    private synchronized boolean isMessageProcessed(Integer messageId) {
        if (messageIds.contains(messageId)) {
            return true;
        }
        messageIds.add(messageId);
        return false;
    }

    private void processAttachments(Message message, Integer userId) throws Exception{
        if (!isEmpty(message.getAttachments())) {
            for (MessageAttachment attachment : message.getAttachments()) {
                if (MessageAttachmentType.AUDIO == attachment.getType())
                {
                    addTrack(userId, attachment);
                }
            }
            sendTrackList(jukeboxMapper.getJukeboxIdByUser(userId));
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

    private void sendTrackList(String jukeBoxId){
        simpMessagingTemplate.convertAndSend(TOPIC_ENDPOINT + "/" + jukeBoxId,
                jukeBoxStore.getTrackListOrPopIfEmpty(jukeBoxId));
    }

    public static boolean isConfirmation() {
        return isConfirmation;
    }

    public static void setConfirmation(boolean isConfirmation) {
        CallbackApiHandler.isConfirmation = isConfirmation;
    }
}
