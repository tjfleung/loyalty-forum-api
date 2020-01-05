package com.terrence.loyalty.forumapi.controller;

import com.terrence.loyalty.forumapi.domain.message.MessageDto;
import com.terrence.loyalty.forumapi.domain.message.MessageService;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
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
import org.springframework.test.web.servlet.MvcResult;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;
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

    private List<MessageDto> messageDtoList;

    @Before
    public void setUp() {
        messageDtoList = Arrays.asList(
                new MessageDto("Terrence", LocalDateTime.of(2019, 12, 25, 13, 33, 21), "Happy Holidays!"),
                new MessageDto("tester", LocalDateTime.of(2020, 1, 1, 0, 0, 3), "Happy New Year!")
        );
    }

    @Test
    public void testGetAllMessages() throws Exception {
        when(messageService.getAllMessages()).thenReturn(messageDtoList);
        MvcResult result = mockMvc.perform(get("/messages"))
                .andExpect(status().isOk())
                .andReturn();

        Assertions.assertTrue(result.getResponse().getContentAsString().contains("Terrence"));
    }

    @Test
    public void testPostMessage() throws Exception {
        MessageDto messageDto = new MessageDto("Test", LocalDateTime.of(2019, 12, 29, 20, 9, 22), "Testing");

        when(messageService.save(Mockito.any(MessageDto.class))).thenReturn(messageDto);

        MvcResult result = mockMvc.perform(post("/messages")
                .content("{ \"userName\": \"Test\", \"message\": \"Testing\" }")
                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andReturn();

        Assertions.assertTrue(result.getResponse().getContentAsString().contains("Testing"));
    }

    @Test
    public void testGetMessageByUsername() throws Exception {
        String testUsername = "tester";
        List<MessageDto> expected = new ArrayList<>();
        expected.add(messageDtoList.get(1));
        when(messageService.getMessagesByUsername(anyString())).thenReturn(expected);

        MvcResult result = mockMvc.perform(get("/messages/" + testUsername))
                .andExpect(status().isOk())
                .andReturn();

        Assertions.assertTrue(result.getResponse().getContentAsString().contains("tester"));
    }
}
