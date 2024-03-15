package com.xuecheng.content.feignclient;

import com.xuecheng.content.feignclient.fallback.SearchServiceFallBackFactory;
import com.xuecheng.search.po.CourseIndex;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(value = "search", fallbackFactory = SearchServiceFallBackFactory.class)
public interface SearchService {

    /**
     * create a new course index
     */
    @PostMapping("/search/course")
    Boolean add(@RequestBody CourseIndex courseIndex);
}
