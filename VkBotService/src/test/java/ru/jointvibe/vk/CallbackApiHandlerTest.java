package ru.jointvibe.vk;

import com.vk.api.sdk.objects.messages.Message;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class CallbackApiHandlerTest {

    @InjectMocks
    private CallbackApiHandler callbackHandler = Mockito.spy(CallbackApiHandler.class);

    @Mock
    private MessageSender sender;

    @Test
    public void testInitialMessage() throws Exception {
        Message message = mock(Message.class);
        when(message.getBody()).thenReturn("Начать").thenReturn("Оплатить");
        callbackHandler.messageNew(1, message);
        verify(sender).welcome(Matchers.any());
        callbackHandler.messageNew(2, message);
    }

    @Test(expected = RuntimeException.class)
    public void testExceptionThrowing() {
        Message message = mock(Message.class);
        RuntimeException exception = new RuntimeException("Failed to start messaging process");
//        when(message.getBody()).thenReturn("test");
        Mockito.doThrow(exception).when(callbackHandler).messageNew(Matchers.anyInt(), Matchers.any());
        callbackHandler.messageNew(0, message);
    }
}
