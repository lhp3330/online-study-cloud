package com.xuecheng.content.controller;

/*
   @Class:CourseBaseInfoController
   @Date:2024/1/7  15:38
*/

import com.xuecheng.base.model.PageParams;
import com.xuecheng.base.model.PageResult;
import com.xuecheng.content.model.dto.QueryCourseParamsDTO;
import com.xuecheng.content.model.pojo.CourseBase;
import com.xuecheng.content.service.CourseBaseInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@Api(value = "课程信息管理接口", tags = "课程信息管理接口")
public class CourseBaseInfoController {

    @Resource
    private CourseBaseInfoService courseBaseInfoService;


    @ApiOperation("课程信息查询接口")
    @PostMapping("/course/list")
    public PageResult<CourseBase> list(PageParams pageParams, @RequestBody(required = false) QueryCourseParamsDTO queryCourseParamsDTO) {
        return courseBaseInfoService.QueryCourseBaseList(pageParams, queryCourseParamsDTO);
    }
}
