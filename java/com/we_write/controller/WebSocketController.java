package com.we_write.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;

import com.we_write.entity.Blog;
import com.we_write.repository.BlogRepository;
import com.we_write.repository.BlogVersionRepository;
import com.we_write.service.BlogService;

@Controller
public class WebSocketController {
    @Autowired
    private SimpMessagingTemplate messagingTemplate;
    @Autowired
    private BlogRepository blogRepository;


    @MessageMapping("/update/{id}")
    public void update(@PathVariable Long id, String content) {
        // Update the content in the database
        Optional<Blog> optionalPage = blogRepository.findById(id);
        optionalPage.ifPresent(page -> {
            page.setContent(content);
            blogRepository.save(page);
        });

        // Notify subscribers about the update
        messagingTemplate.convertAndSend("/topic/update/" + id, content);
    }
}
