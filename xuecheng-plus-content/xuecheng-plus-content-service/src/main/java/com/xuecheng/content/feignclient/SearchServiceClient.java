package com.xuecheng.content.feignclient;

import com.xuecheng.base.model.CourseIndex;
import com.xuecheng.content.feignclient.fallback.SearchServiceFallBackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@FeignClient(value = "search")
public interface SearchServiceClient {

    /**
     * create a new course index
     */
    @PostMapping("/search/index/course")
    Boolean add(@RequestBody CourseIndex courseIndex);
}
