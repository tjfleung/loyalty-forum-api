package com.terrence.loyalty.forumapi.rest.message;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.terrence.loyalty.forumapi.domain.message.Message;
import com.terrence.loyalty.forumapi.domain.message.MessageService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class MessageControllerTest {

    @InjectMocks
    private MessageController messageController;

    @MockBean
    private MessageService messageService;

    @Autowired
    private MockMvc mockMvc;

    private List<Message> messages;

    @Before
    public void setUp() throws Exception {
        messages = Arrays.asList(
                new Message("Terrence", LocalDateTime.of(2019, 12, 25, 13, 33, 21), "Happy Holidays!"),
                new Message("Terrence", LocalDateTime.of(2020, 1, 1, 0, 0, 3), "Happy New Year!")
        );
    }

    @Test
    public void testGetAllMessages() throws Exception {
        when(messageService.getAllMessages()).thenReturn(messages);
        mockMvc.perform(get("/messages")).andExpect(status().isOk());
    }

    @Test
    public void testPostMessage() throws Exception {
        Message message = new Message("Test", LocalDateTime.of(2019, 12, 29, 20, 9, 22), "Testing");

        when(messageService.save(Mockito.any(Message.class))).thenReturn(message);

        mockMvc.perform(post("/messages")
                .content("{ \"userName\": \"Test\", \"message\": \"Testing\" }")
                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"));
    }
}
