package com.xuecheng.content.controller;

/*
   @Class:CourseOpenController
   @Date:2024/3/1  23:31
*/

import com.xuecheng.content.model.vo.CoursePreviewVO;
import com.xuecheng.content.service.CourseBaseInfoService;
import com.xuecheng.content.service.CoursePreviewService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;


/**
 * interface for video brodcast page
 */
@Slf4j
@RestController
@RequestMapping("/open")
public class CourseOpenController {

    @Resource
    private CourseBaseInfoService courseBaseInfoService;

    @Resource
    private CoursePreviewService coursePreviewService;

    /***
     * get course info in video brodcast page
     * @param courseId
     * @return
     */
    @GetMapping("/course/whole/{courseId}")
    CoursePreviewVO coursePreviewInfo2(@PathVariable Long courseId) {
        return coursePreviewService.getCoursePreviewInfo(courseId);
    }

}
