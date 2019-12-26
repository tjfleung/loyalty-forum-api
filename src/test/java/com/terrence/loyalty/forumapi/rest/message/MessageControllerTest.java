package com.terrence.loyalty.forumapi.rest.message;

import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
public class MessageControllerTest {

    @InjectMocks
    private MessageController messageController;

    @Test
    public void testGetMessage() {
        String msg = "testing";
        String actual = messageController.getMessage(msg);
        Assertions.assertEquals(msg, actual);
    }

    @Test
    public void testGetMessageDoesNotReturnDifferentMessage() {
        String msg = "testing";
        String different = "different";
        String actual = messageController.getMessage(msg);
        Assertions.assertNotEquals(different, actual);
    }
}
