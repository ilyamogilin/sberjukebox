package com.sber.jukeBox.vk;

import com.sber.jukeBox.datastore.InvoiceList;
import com.vk.api.sdk.objects.messages.Message;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.*;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class CallbackApiHandlerTest {

    @InjectMocks
    private CallbackApiHandler callbackHandler = spy(CallbackApiHandler.class);

    @Mock
    private MessageSender sender;

    @Spy
    private InvoiceList invoiceList;

    @Test
    public void testInitialMessage() throws Exception {
        Message message = mock(Message.class);
        when(message.getBody()).thenReturn("Начать").thenReturn("Оплатить");
        callbackHandler.messageNew(1, message);
        verify(sender).welcome(any());
        callbackHandler.messageNew(2, message);
        verify(sender).getPaymentChoice(any());
    }

    @Test(expected = RuntimeException.class)
    public void testExceptionThrowing() {
        Message message = mock(Message.class);
        RuntimeException exception = new RuntimeException("Failed to start messaging process");
        when(message.getBody()).thenReturn("test");
        doThrow(exception).when(callbackHandler).messageNew(anyInt(), any());
        callbackHandler.messageNew(0, message);
    }

    @Test
    public void testPaymentChoice() {
        Message sberMessage = mock(Message.class);
        when(sberMessage.getBody()).thenReturn("Sberbank");
        callbackHandler.messageNew(1, sberMessage);
        assertEquals(1, invoiceList.getInvoiceSize());
    }

    @Test
    public void testMultiplePaymentChoice() {
        Message message = mock(Message.class);
        when(message.getBody()).thenReturn("Sberbank")
                               .thenReturn("Qiwi")
                               .thenReturn("Visa/Mastercard");
        callbackHandler.messageNew(1, message);
        assertEquals(1, invoiceList.getInvoiceSize());
        callbackHandler.messageNew(2, message);
        assertEquals(2, invoiceList.getInvoiceSize());
        callbackHandler.messageNew(3, message);
        assertEquals(3, invoiceList.getInvoiceSize());
    }
}
