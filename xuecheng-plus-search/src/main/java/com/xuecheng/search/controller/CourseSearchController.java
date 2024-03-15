package com.xuecheng.search.controller;

import com.xuecheng.base.model.PageParams;
import com.xuecheng.base.model.PageResult;
import com.xuecheng.search.dto.SearchCourseParamDto;
import com.xuecheng.search.dto.SearchPageResultDto;
import com.xuecheng.search.po.CourseIndex;
import com.xuecheng.search.service.CourseSearchService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @description 课程搜索接口
 * @author Mr.M
 * @date 2022/9/24 22:31
 * @version 1.0
 */
@RestController
@RequestMapping("/course")
public class CourseSearchController {

    @Resource
    CourseSearchService courseSearchService;

    /**
     * search course list
     * @param pageParams
     * @param searchCourseParamDto
     * @return
     */
    @GetMapping("/list")
    public SearchPageResultDto<CourseIndex> list(PageParams pageParams, SearchCourseParamDto searchCourseParamDto){
        return courseSearchService.queryCoursePubIndex(pageParams,searchCourseParamDto);
    }
}
