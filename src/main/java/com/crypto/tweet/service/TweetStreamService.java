package com.crypto.tweet.service;

import com.crypto.tweet.config.CouchbaseConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.social.twitter.api.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TweetStreamService {

    private final Logger LOGGER = LoggerFactory.getLogger(TweetStreamService.class);

    @Value("${twitter.blockchain.keyword}")
    private String filter;

    @Autowired
    private Twitter twitter;

    @Autowired
    private SimpMessageSendingOperations messagingTemplate;

    private Stream userStream;

    public void streamTweetEvent() throws InterruptedException {

        List<StreamListener> listeners = new ArrayList<>();
        CouchbaseConfiguration cb = new CouchbaseConfiguration();
        cb.connect();

        StreamListener streamListener = new StreamListener() {
            @Override
            public void onWarning(StreamWarningEvent warningEvent) {
            }

            @Override
            public void onTweet(Tweet tweet) {
                LOGGER.info("User '{}', Tweeted : {}, from ; {}", tweet.getUser().getName(), tweet.getText(), tweet.getUser().getLocation());

                // save to couchbase
                cb.write(tweet);

                StringBuilder hashTag = new StringBuilder();
                List<HashTagEntity> hashTags = tweet.getEntities().getHashTags();
                for (HashTagEntity hash : hashTags) {
                    hashTag.append("#" + hash.getText() + " ");
                }

                // send to subscribed users
                if (tweet.getUser().getLocation().isEmpty() == false)
                    messagingTemplate.convertAndSend("/topic/tweet/location", tweet.getUser().getLocation());
                if (hashTag.length() > 0)
                    messagingTemplate.convertAndSend("/topic/tweet/hashtags", hashTag);
            }

            @Override
            public void onLimit(int numberOfLimitedTweets) {
            }

            @Override
            public void onDelete(StreamDeleteEvent deleteEvent) {
            }
        };

        listeners.add(streamListener);

        FilterStreamParameters filterStreamParameters = new FilterStreamParameters();
        filterStreamParameters.track(filter);
        userStream = twitter.streamingOperations().filter(filterStreamParameters, listeners);

    }
}
