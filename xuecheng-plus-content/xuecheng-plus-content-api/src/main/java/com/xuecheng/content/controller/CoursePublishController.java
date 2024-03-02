package com.xuecheng.content.controller;

/*
   @Class:CoursePublishController
   @Date:2024/3/1  22:10
*/

import com.xuecheng.content.model.vo.CoursePreviewVO;
import com.xuecheng.content.service.CoursePreviewService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;

@Controller
public class CoursePublishController {

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
