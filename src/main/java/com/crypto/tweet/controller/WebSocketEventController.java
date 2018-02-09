package com.crypto.tweet.controller;

import com.crypto.tweet.service.TweetStreamService;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

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