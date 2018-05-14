package pasaranprimbon.bot.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import advprog.example.bot.EventTestUtil;

import com.linecorp.bot.model.event.Event;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.TextMessage;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@SpringBootTest(properties = "line.bot.handler.enabled=false")
@ExtendWith(SpringExtension.class)
public class PrimbonControllerTest {

    static {
        System.setProperty("line.bot.channelSecret", "SECRET");
        System.setProperty("line.bot.channelToken", "TOKEN");
    }

    @Autowired
    private PrimbonController primbonController;

    @Test
    void testContextLoads() {
        assertNotNull(primbonController);
    }

    @Test
    void testIncorrectInput() {
        MessageEvent<TextMessageContent> event =
                EventTestUtil.createDummyTextMessage("/primbon jodoh dan pekerjaan");

        TextMessage reply = primbonController.handleTextMessageEvent(event);

        assertEquals("Please insert the correct date format (yyyy-MM-dd)", reply.getText());
    }

    @Test
    void testHandlePastTextMessageEvent() {
        MessageEvent<TextMessageContent> event =
                EventTestUtil.createDummyTextMessage("/primbon 1000-01-26");

        TextMessage reply = primbonController.handleTextMessageEvent(event);

        assertEquals("Minggu Wage", reply.getText());
    }

    @Test
    void testHandlePresentTextMessageEvent() {
        MessageEvent<TextMessageContent> event =
                EventTestUtil.createDummyTextMessage("/primbon 1981-09-12");

        TextMessage reply = primbonController.handleTextMessageEvent(event);

        assertEquals("Sabtu Legi", reply.getText());
    }

    @Test
    void testHandleFutureTextMessageEvent() {
        MessageEvent<TextMessageContent> event =
                EventTestUtil.createDummyTextMessage("/primbon 3000-02-12");

        TextMessage reply = primbonController.handleTextMessageEvent(event);

        assertEquals("Rabu Legi", reply.getText());
    }

    @Test
    void testHandleDefaultMessage() {
        Event event = mock(Event.class);

        primbonController.handleDefaultMessage(event);

        verify(event, atLeastOnce()).getSource();
        verify(event, atLeastOnce()).getTimestamp();
    }
}