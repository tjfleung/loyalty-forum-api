package com.terrence.loyalty.forumapi;

import com.terrence.loyalty.forumapi.domain.message.Message;
import com.terrence.loyalty.forumapi.domain.message.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Arrays;

/**
 * As I am using an in memory database, this class will assist in creating some existing records
 */
@Component
public class DataLoader implements ApplicationRunner {

    @Autowired
    private MessageRepository messageRepository;

    public DataLoader(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        createMessages();
    }

    private void createMessages() {
        messageRepository.saveAll(Arrays.asList(
                new Message("Terrence", LocalDateTime.of(2019, 12, 25, 13, 33, 21), "Happy Holidays!"),
                new Message("Terrence", LocalDateTime.of(2020, 1, 1, 0, 0, 3), "Happy New Year!")
        ));
    }
}
