package com.xuecheng.content.feignclient.fallback;

/*
   @Class:SearchServiceFallBackFactory
   @Date:2024/3/15  23:58
*/

import com.xuecheng.content.feignclient.SearchServiceClient;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SearchServiceFallBackFactory implements FallbackFactory<SearchServiceClient> {
    @Override
    public SearchServiceClient create(Throwable throwable) {
        log.error("添加index失败, 熔断>>>>>>>");
        return null;
    }
}
