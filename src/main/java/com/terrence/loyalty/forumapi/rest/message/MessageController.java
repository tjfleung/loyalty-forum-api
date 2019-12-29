package com.terrence.loyalty.forumapi.rest.message;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/message")
@Slf4j
public class MessageController {

    @CrossOrigin
    @GetMapping
    public String getMessage(@RequestParam String message) {
        log.info(message);
        return message;
    }
}
