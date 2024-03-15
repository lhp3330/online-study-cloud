package com.xuecheng.content.feignclient.fallback;

/*
   @Class:SearchServiceFallBackFactory
   @Date:2024/3/15  23:58
*/

import com.xuecheng.content.feignclient.SearchService;
import feign.hystrix.FallbackFactory;

public class SearchServiceFallBackFactory implements FallbackFactory<SearchService> {
    @Override
    public SearchService create(Throwable throwable) {
        return null;
    }
}
