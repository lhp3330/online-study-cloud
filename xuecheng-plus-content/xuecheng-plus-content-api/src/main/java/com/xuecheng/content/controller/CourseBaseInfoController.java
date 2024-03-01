package com.xuecheng.content.controller;

/*
   @Class:CourseBaseInfoController
   @Date:2024/1/7  15:38
*/

import com.xuecheng.base.model.PageParams;
import com.xuecheng.base.model.PageResult;
import com.xuecheng.content.model.dto.AddCourseDTO;
import com.xuecheng.content.model.dto.QueryCourseParamsDTO;
import com.xuecheng.content.model.dto.UpdateCourseDTO;
import com.xuecheng.content.model.pojo.CourseBase;
import com.xuecheng.content.model.vo.CourseBaseInfoVO;
import com.xuecheng.content.service.CourseBaseInfoService;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;

@RestController
public class CourseBaseInfoController {

    @Resource
    private CourseBaseInfoService courseBaseInfoService;

    /**
     * query course list
     */
    @PostMapping("/course/list")
    public PageResult<CourseBase> list(PageParams pageParams, @RequestBody(required = false) QueryCourseParamsDTO queryCourseParamsDTO) {
        return courseBaseInfoService.queryCourseBaseList(pageParams, queryCourseParamsDTO);
    }

    /**
     * add a new course
     */
    @PostMapping("/course")
    public CourseBaseInfoVO createCourseBaseInfo(@RequestBody @Validated AddCourseDTO addCourseDTO) {
        return courseBaseInfoService.saveCourseBaseInfo(999L, addCourseDTO);
    }

    /**
     * query course by id
     */
    @GetMapping("/course/{id}")
    public CourseBaseInfoVO queryCourseById(@PathVariable Long id) {
        return courseBaseInfoService.queryCourseById(id);
    }

    /**
     * update course info
     */
    @PutMapping("/course")
    public CourseBaseInfoVO updateCourse(@RequestBody UpdateCourseDTO updateCourseDTO) {
        return courseBaseInfoService.updateCourseBaseInfo(updateCourseDTO);
    }

    /**
     * delete course by id
     */
    @DeleteMapping("/course/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteCourseById(@PathVariable Long id) {
        courseBaseInfoService.deleteCourseById(id);
    }
}
