package com.elephascloud.storage.common.data.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ThrowableConfig {

  @Bean
  public Throwable throwable() {
    return new Throwable();
  }
}
