package com.terrence.loyalty.forumapi.rest.message;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/message")
public class MessageController {

    @GetMapping
    public String getMessage(@RequestParam String message) {
        return message;
    }
}
