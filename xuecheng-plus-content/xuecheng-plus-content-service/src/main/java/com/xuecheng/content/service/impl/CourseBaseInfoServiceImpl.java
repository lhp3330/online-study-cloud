package com.xuecheng.content.service.impl;

/*
   @Class:CourseBaseInfoServiceImpl
   @Date:2024/1/7  17:18
*/

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xuecheng.base.model.PageParams;
import com.xuecheng.base.model.PageResult;
import com.xuecheng.content.mapper.CourseBaseMapper;
import com.xuecheng.content.model.dto.QueryCourseParamsDTO;
import com.xuecheng.content.model.pojo.CourseBase;
import com.xuecheng.content.service.CourseBaseInfoService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Slf4j
@Service
public class CourseBaseInfoServiceImpl implements CourseBaseInfoService {

    @Resource
    private CourseBaseMapper courseBaseMapper;

    /**
     * 课程分页查询
     */
    @Override
    public PageResult<CourseBase> QueryCourseBaseList(PageParams pageParams, QueryCourseParamsDTO dto) {
       // 条件构造
        LambdaQueryWrapper<CourseBase> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.isEmpty(dto.getCourseName()), CourseBase::getName, dto.getCourseName());
        wrapper.eq(StringUtils.isEmpty(dto.getAuditStatus()), CourseBase::getAuditStatus, dto.getAuditStatus());
        wrapper.eq(StringUtils.isEmpty(dto.getPublishStatus()), CourseBase::getStatus, dto.getPublishStatus());
        // 分页
        Page<CourseBase> page = new Page<>(pageParams.getPageNo(), pageParams.getPageSize());
        Page<CourseBase> pageResult = courseBaseMapper.selectPage(page, wrapper);

        return new PageResult<>(pageResult.getRecords(),
                pageResult.getTotal(),
                pageParams.getPageNo(),
                pageParams.getPageSize());
    }
}
