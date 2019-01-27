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

    //TODO change to interface
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

    private HashMap<String, Integer> paymentChoiceCollection = createMapPayment();

    private static HashMap<String, Integer> createMapPayment() {
        HashMap<String, Integer> paymentMap = new HashMap<>();

        paymentMap.put("Сбер", 1);
        paymentMap.put("Qiwi", 2);
        paymentMap.put("Visa/Mastercard", 3);
        paymentMap.put("VkPay", 4);

        return paymentMap;
    }

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
            if (isMessageProcessed(message.getId())) {
                return;
            }
            if (BEGIN_KEYWORD.equals(message.getBody())) {
                sender.welcome(userId);
                return;
            }
            sender.askPayment(userId);
            if (PAYMENT_KEYWORD.equals(message.getBody())) {
                askPaymentCode(userId);
                return;
            }
            if (paymentChoiceCollection.containsValue(message.getBody())) {
                int paymentCode = paymentChoiceCollection.get(message.getBody());

                Invoice invoice = new Invoice(message.getActionEmail(), message.getUserId(), Invoice.Status.Wait, paymentCode);
                invoiceList.addInvoice(invoice);

                // wait some time

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

    private void processingInvoice(Invoice invoice) throws Exception {
        // after some time
        invoice.setPaymentStatus(Invoice.Status.Success);
        if (invoice.getPaymentStatus() == Invoice.Status.Success) {
            sender.sendSuccessPayment(invoice.getUserId());
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
                .build();
        jukeBoxStore.addTrack(track);
        sender.audioAdded(userId, track.getFullName());
    }

    private void refreshPlaylist(Integer jukeboxId) {
        simpMessagingTemplate.convertAndSend("/topic/test", jukeBoxStore.getTracksListMock());
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
