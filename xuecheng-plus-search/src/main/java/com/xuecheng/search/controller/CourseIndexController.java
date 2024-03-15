package com.xuecheng.search.controller;

import com.xuecheng.base.exception.BaseException;
import com.xuecheng.search.po.CourseIndex;
import com.xuecheng.search.service.IndexService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * course index interface
 */
@RestController
@RequestMapping("/index")
public class CourseIndexController {

    @Value("${elasticsearch.course.index}")
    private String courseIndexStore;

    @Resource
    IndexService indexService;

    /**
     * create a new course index
     * @param courseIndex
     * @return
     */
    @PostMapping("/course")
    public Boolean add(@RequestBody CourseIndex courseIndex) {
        Long id = courseIndex.getId();
        if(id == null){
            throw new BaseException("course id is blank!");
        }
        Boolean result = indexService.addCourseIndex(courseIndexStore, String.valueOf(id), courseIndex);
        if(!result){
           throw new BaseException("failed to create a course index");
        }
        return result;

    }
}
