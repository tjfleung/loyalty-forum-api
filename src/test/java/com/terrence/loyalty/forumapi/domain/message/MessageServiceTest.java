package com.terrence.loyalty.forumapi.domain.message;

import com.terrence.loyalty.forumapi.exception.ForumException;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MessageServiceTest {

    @InjectMocks
    private MessageService service;

    @Mock
    private MessageRepository repository;

    @Mock
    private ModelMapper modelMapper;

    private List<Message> messages;
    private List<MessageDto> messageDtoList;

    private final String username = "test";
    private final LocalDateTime time = LocalDateTime.of(2020, 1, 4, 17, 5, 3);
    private final String comment = "unit test";

    @Before
    public void setUp() {
        messages = Arrays.asList(
                new Message("Terrence", LocalDateTime.of(2019, 12, 25, 13, 33, 21), "Happy Holidays!"),
                new Message("tester", LocalDateTime.of(2020, 1, 1, 0, 0, 3), "Happy New Year!")
        );
        messageDtoList = Arrays.asList(
                new MessageDto("Terrence", LocalDateTime.of(2019, 12, 25, 13, 33, 21), "Happy Holidays!"),
                new MessageDto("tester", LocalDateTime.of(2020, 1, 1, 0, 0, 3), "Happy New Year!")
        );
    }

    @Test
    public void testGetAllMessages() {
        when(repository.findAll(Sort.by(Sort.Direction.DESC, "postedDate"))).thenReturn(messages);
        List<MessageDto> actual = service.getAllMessages();

        Assertions.assertEquals(messageDtoList.size(), actual.size());
    }

    @Test
    public void testGetMessageByUsername() {
        String username = "Terrence";
        List<Message> expected = new ArrayList<>();
        expected.add(messages.get(0));

        when(repository.findAllByUsernameOrderByPostedDateDesc(Mockito.anyString())).thenReturn(expected);
        List<MessageDto> actual = service.getMessagesByUsername(username);

        Assertions.assertEquals(expected.size(), actual.size());
    }

    @Test
    public void testSave() {
        MessageDto messageDto = new MessageDto(username, time, comment);
        Message message = new Message(username, time, comment);

        when(repository.save(Mockito.any())).thenReturn(message);

        Assertions.assertDoesNotThrow(() -> service.save(messageDto));
    }

    @Test
    public void testSaveInvalidUsername() {
        MessageDto messageDto = new MessageDto("", time, comment);

        try {
            service.save(messageDto);
        } catch (ForumException e) {
            Assertions.assertEquals(400, e.getHttpStatus().value());
        }
    }

    @Test
    public void testSaveInvalidComment() {
        MessageDto messageDto = new MessageDto(username, time, "");

        try {
            service.save(messageDto);
        } catch (ForumException e) {
            Assertions.assertEquals(400, e.getHttpStatus().value());
        }
    }
}
