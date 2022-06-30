package com.microservices.demo.twitter.to.kafka.service.runner.impl;

import com.microservices.demo.config.TwitterV2ToKafkaServiceConfigData;
import com.microservices.demo.twitter.to.kafka.service.listener.TwitterKafkaStatusListener;
import com.microservices.demo.twitter.to.kafka.service.runner.StreamRunner;
import java.util.Arrays;
import javax.annotation.PreDestroy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Component;
import twitter4j.FilterQuery;
import twitter4j.TwitterException;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;

@Component
@ConditionalOnExpression("${twitter-to-kafka-service-v2.enable-v2-tweets} && not ${twitter-to-kafka-service-v2.enable-mock-tweets}")
public class TwitterV2KafkaStreamRunner implements StreamRunner {

  private static final Logger LOG = LoggerFactory.getLogger(TwitterKafkaStreamRunner.class);

  private final TwitterV2ToKafkaServiceConfigData twitterToKafkaServiceConfigData;

  private final TwitterKafkaStatusListener twitterKafkaStatusListener;

  private TwitterStream twitterStream;

  public TwitterV2KafkaStreamRunner(
      TwitterV2ToKafkaServiceConfigData twitterToKafkaServiceConfigData,
      TwitterKafkaStatusListener twitterKafkaStatusListener) {
    this.twitterToKafkaServiceConfigData = twitterToKafkaServiceConfigData;
    this.twitterKafkaStatusListener = twitterKafkaStatusListener;
  }

  @Override
  public void start() throws TwitterException {
    twitterStream = new TwitterStreamFactory().getInstance();
    twitterStream.addListener(twitterKafkaStatusListener);
    addFilter();
  }

  @PreDestroy
  public void shutdown() {
    if (twitterStream != null) {
      LOG.info("Closing twitter stream!");
      twitterStream.shutdown();
    }
  }

  private void addFilter() {
    String[] keywords = twitterToKafkaServiceConfigData.getTwitterKeywords().toArray(new String[0]);
    FilterQuery filterQuery = new FilterQuery(keywords);
    twitterStream.filter(filterQuery);
    LOG.info("Started filtering twitter stream for keywords {}", Arrays.toString(keywords));
  }
}
