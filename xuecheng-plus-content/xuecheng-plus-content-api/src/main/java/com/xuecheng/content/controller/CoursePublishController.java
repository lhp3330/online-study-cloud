package com.xuecheng.content.controller;

/*
   @Class:CoursePublishController
   @Date:2024/3/1  22:10
*/

import com.xuecheng.content.service.CoursePublishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;

@Slf4j
@RestController
public class CoursePublishController {

    @Resource
    private CoursePublishService coursePublishService;

    /**
     * course publish
     * @param courseId
     */
    @PostMapping ("/coursepublish/{courseId}")
    public void coursePublish(@PathVariable("courseId") Long courseId) {
        Long companyId = 123456L;
        coursePublishService.publishCourse(courseId, companyId);
    }

    /**
     * course dePublish
     */
    @GetMapping("/courseoffline/{courseId}")
    public void courseDePublish(@PathVariable("courseId") Long courseId) {
        coursePublishService.dePublishCourse(courseId);
    }

}
