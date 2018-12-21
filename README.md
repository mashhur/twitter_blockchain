# Twitter-blockchain-spring [![Build Status](https://travis-ci.org/mashhur/twitter_blockchain.svg?branch=master)](https://travis-ci.org/mashhur/twitter_blockchain)

## Twitter & API info
If you don't have twitter account, visit https://apps.twitter.com and create an app.
API : https://developer.twitter.com/en/docs/tweets/filter-realtime/overview

Create application.properties file under resources folder and edit your properties file as follow:

    spring.social.twitter.appId=YOUR_APPID
    spring.social.twitter.appSecret=YOUR_APPSECRET
    twitter.access.token=YOUR_TOKEN
    twitter.access.token.secret=YOUR_TOKENSECRET
    twitter.crypto.keyword=YOUR_FILTER_KEYWORDS

Change Couchbase credentials of CouchbaseConfiguration.java undef config folder.

    private final String hostName = "localhost";
    private final String userName = "user";
    private final String password = "password";
    private final String bucketName = "bucket";


If you are running through console, type following commands:
	$mvn dependency:tree // to check or load dependecies. Make sure maven is installed before executing this command.
	$mvn spring-boot:run

If you want to run it on background:
	$mvn spring-boot:run & disown
