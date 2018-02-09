package com.crypto.tweet.service;

import java.util.ArrayList;
import java.util.List;

import com.crypto.tweet.config.CouchbaseConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.social.twitter.api.FilterStreamParameters;
import org.springframework.social.twitter.api.HashTagEntity;
import org.springframework.social.twitter.api.Tweet;
import org.springframework.social.twitter.api.Twitter;
import org.springframework.social.twitter.api.Stream;
import org.springframework.social.twitter.api.StreamDeleteEvent;
import org.springframework.social.twitter.api.StreamListener;
import org.springframework.social.twitter.api.StreamWarningEvent;
import org.springframework.stereotype.Service;

@Service
public class TweetStreamService {

	private final Logger LOGGER = LoggerFactory.getLogger(TweetStreamService.class);

	@Value("${twitter.blockchain.keyword}")
	private String filter;

	@Autowired
	private Twitter twitter;

	@Autowired
	private CouchbaseConfiguration cb;

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
					LOGGER.info("User '{}', Tweeted : {}, from ; {}", tweet.getUser().getName() , tweet.getText(), tweet.getUser().getLocation());

					// save to couchbase
					cb.write(tweet);

                    StringBuilder hashTag = new StringBuilder();
                    List<HashTagEntity> hashTags = tweet.getEntities().getHashTags();
                    for (HashTagEntity hash : hashTags) {
                        hashTag.append("#"+hash.getText() + " ");
                    }

                    // send to subscribed users
                    messagingTemplate.convertAndSend("/topic/tweet/location", tweet.getUser().getLocation());
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
