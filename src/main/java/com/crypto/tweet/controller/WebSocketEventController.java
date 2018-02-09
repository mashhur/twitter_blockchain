package com.crypto.tweet.controller;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import com.crypto.tweet.service.TweetStreamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Controller
@RequestMapping("/")
public class WebSocketEventController {
	    
    private TweetStreamService tweetStreamService;
    
    private List<SseEmitter> emitters = new CopyOnWriteArrayList<>();

    public WebSocketEventController(TweetStreamService tweetStreamService) {
        this.tweetStreamService = tweetStreamService;
    }
    
    @RequestMapping("/")
    public String streamTweetAsEvents() throws InterruptedException {
        return "events";
    }

    @MessageMapping("/server.start")
    public void streamTweets() throws InterruptedException {
        tweetStreamService.streamTweetEvent();
    }

}