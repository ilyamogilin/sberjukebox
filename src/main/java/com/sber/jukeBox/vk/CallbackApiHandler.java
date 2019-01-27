package com.sber.jukeBox.vk;

import com.sber.jukeBox.datastore.InvoiceList;
import com.sber.jukeBox.datastore.JukeBoxStoreImpl;
import com.sber.jukeBox.json.TrackList;
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

    private HashMap<String, Integer> paymentChoiceCollection;

    private void initMapPayment() {
        paymentChoiceCollection = new HashMap<>();

        paymentChoiceCollection.put("Сбер", 1);
        paymentChoiceCollection.put("Qiwi", 2);
        paymentChoiceCollection.put("Visa/Mastercard", 3);
        paymentChoiceCollection.put("VkPay", 4);
    }

    private static boolean isConfirmation;

    public CallbackApiHandler() {
        jukeboxMapper = new JukeboxMapper();
        messageIds = new HashSet<>();
        initMapPayment();
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
            if (PAYMENT_KEYWORD.equals(message.getBody())) {
                askPaymentCode(userId);
                return;
            }
            int invoiceId = 0;
            if (paymentChoiceCollection.containsKey(message.getBody())) {
                int paymentCode = paymentChoiceCollection.get(message.getBody());

                Invoice invoice = new Invoice(message.getActionEmail(), message.getUserId(), Invoice.Status.Wait, paymentCode);
                invoiceId = invoiceList.addInvoice(invoice);

                sender.sendInvoice(invoice, userId, invoiceId);



            }
            if (!jukeboxMapper.checkUser(message)) {
                if (!jukeboxMapper.addUser(message)) {
                    sender.requestJukeboxId(userId);
                    return;
                }
                sender.jukeboxIdConfirmed(userId);
            }
            if (!isEmpty(message.getAttachments())) {
                for (MessageAttachment attachment : message.getAttachments()) {
                    if (MessageAttachmentType.AUDIO == attachment.getType()
                            && invoiceList.getInvoice(invoiceId).getPaymentStatus() == Invoice.Status.Success) {
                        addTrack(userId, attachment);
                    } else {
                        sender.sendRemindInvoice(userId, invoiceId);
                    }
                }
                refreshPlaylist(jukeboxMapper.getJukeboxIdByUser(userId));
                sender.askPayment(userId);
            }
        } catch (Exception e) {
            log.error("Exception while handling new message", e);
        }
    }

    private void askPaymentCode(int userId) throws Exception {

        sender.getPaymentChoice(userId);
    }

    private synchronized boolean isMessageProcessed(Integer messageId) {
        if (messageIds.contains(messageId)) {
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
                .jukeboxId(jukeboxMapper.getJukeboxIdByUser(userId))
                .build();
        jukeBoxStore.addTrack(track);
        sender.audioAdded(userId, track.getFullName());
    }

    private void refreshPlaylist(Integer jukeboxId) {
        TrackList trackList = jukeBoxStore.isPlayerEmpty(jukeboxId) ?
                jukeBoxStore.getTracksWithNowPlaying(jukeboxId) : jukeBoxStore.getAllTracks(jukeboxId);
        if (trackList.getNowPlaying() != null){
            jukeBoxStore.setPlayerEmpty(jukeboxId, false);
        }
        simpMessagingTemplate.convertAndSend(TOPIC_ENDPOINT + "/" + jukeboxId, trackList);
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
