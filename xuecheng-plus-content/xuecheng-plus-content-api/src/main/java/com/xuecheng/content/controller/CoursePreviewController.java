package com.xuecheng.content.controller;

/*
   @Class:CoursePreviewController
   @Date:2024/3/9  22:20
*/

import com.xuecheng.content.model.vo.CoursePreviewVO;
import com.xuecheng.content.service.CoursePreviewService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;

@Controller
public class CoursePreviewController {

    @Resource
    private CoursePreviewService coursePreviewService;

    /**
     * course preview info
     * @param courseId
     * @return
     */
    @GetMapping("/coursepreview/{courseId}")
    ModelAndView coursePreviewInfo(@PathVariable Long courseId) {
        CoursePreviewVO coursePreviewInfo = coursePreviewService.getCoursePreviewInfo(courseId);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("model", coursePreviewInfo);
        modelAndView.setViewName("course_template");
        return modelAndView;
    }
}
