package com.microservices.demo.config;

import java.util.List;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "twitter-to-kafka-service-v2")
public class TwitterV2ToKafkaServiceConfigData {
  private List<String> twitterKeywords;
  private String welcomeMessage;
  private String twitterV2BaseUrl;
  private String twitterV2RulesBaseUrl;
  private String twitterV2BearerToken;
}
