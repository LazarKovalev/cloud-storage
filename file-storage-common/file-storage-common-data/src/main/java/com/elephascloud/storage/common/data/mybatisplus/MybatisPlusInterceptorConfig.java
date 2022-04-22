package com.elephascloud.storage.common.data.mybatisplus;

import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * This class is used for the configuration of the MyBatis paging plugin.
 *
 * @author Rei
 */
@Configuration
public class MybatisPlusInterceptorConfig {

  /**
   * MyBatis paging plugin configuration.The paging plugin will automatically recognize the database
   * type.
   *
   * @return PaginationInterceptor
   */
  @Bean
  public MybatisPlusInterceptor mybatisPlusInterceptor() {
    MybatisPlusInterceptor mybatisPlusInterceptor = new MybatisPlusInterceptor();
    PaginationInnerInterceptor paginationInnerInterceptor = new PaginationInnerInterceptor();
    paginationInnerInterceptor.setOverflow(false);
    paginationInnerInterceptor.setMaxLimit(500L);
    mybatisPlusInterceptor.addInnerInterceptor(paginationInnerInterceptor);
    return mybatisPlusInterceptor;
  }
}
