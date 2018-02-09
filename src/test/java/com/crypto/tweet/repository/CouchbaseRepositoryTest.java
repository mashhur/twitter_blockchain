package com.crypto.tweet.repository;

import com.crypto.tweet.model.BlockchainTweet;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CouchbaseRepositoryTest {

    @Test
    public void contextLoads() {
    }

    @Test
    public void save() {
        BlockchainTweet tweet = new BlockchainTweet();
        tweet.setId("1");
        tweet.setText("Sample text 1");
    }

    @Test
    public void couchbaseTest(){
    }
}