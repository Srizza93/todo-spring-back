package com.todo.back.payload.response;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class MessageResponseTest {

    MessageResponse messageResponse = new MessageResponse("Message Test");

    @Test
    public void shouldGetMessageResponse() {
        String message = messageResponse.getMessage();

        assertEquals(message, "Message Test");
    }

    @Test
    public void shouldSetMessageResponse() {
        messageResponse.setMessage("mock");

        String message = messageResponse.getMessage();

        assertEquals(message, "mock");
    }

}
