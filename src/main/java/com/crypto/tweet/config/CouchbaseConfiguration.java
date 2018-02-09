package com.crypto.tweet.config;

import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.Cluster;
import com.couchbase.client.java.CouchbaseCluster;
import com.couchbase.client.java.document.RawJsonDocument;

import com.google.gson.Gson;

import org.springframework.social.twitter.api.Tweet;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class CouchbaseConfiguration {

    private final String hostName = "localhosr";
    private final String userName = "user";
    private final String password = "pwd";
    private final String bucketName = "twitter-blockchain";

    private Cluster cluster;
    private Bucket bucket;
    private Gson gson = new Gson();

    public boolean connect() {
        boolean ret = false;
        try {
            this.cluster = CouchbaseCluster.create(this.hostName);
            this.cluster.authenticate(this.userName, this.password);
            this.bucket = this.cluster.openBucket(this.bucketName, 10000, TimeUnit.MILLISECONDS);
            ret = true;
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        return ret;
    }

    public void write(Tweet tweet) {

        String jsonString = gson.toJson(tweet);
        RawJsonDocument jsonDoc = RawJsonDocument.create(tweet.getIdStr(), jsonString);
        this.bucket.insert(jsonDoc);
    }

    public void terminate() {
        this.bucket.close();
        this.cluster.disconnect();
    }

}
