package com.xuecheng.content.service.impl;

/*
   @Class:CourseBaseInfoServiceImpl
   @Date:2024/1/7  17:18
*/

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xuecheng.base.enums.CustomException;
import com.xuecheng.base.exception.ParamsIsNullException;
import com.xuecheng.base.model.PageParams;
import com.xuecheng.base.model.PageResult;
import com.xuecheng.content.mapper.CourseBaseMapper;
import com.xuecheng.content.mapper.CourseCategoryMapper;
import com.xuecheng.content.mapper.CourseMarketMapper;
import com.xuecheng.content.model.dto.AddCourseDTO;
import com.xuecheng.content.model.dto.QueryCourseParamsDTO;
import com.xuecheng.content.model.pojo.CourseBase;
import com.xuecheng.content.model.pojo.CourseCategory;
import com.xuecheng.content.model.pojo.CourseMarket;
import com.xuecheng.content.model.vo.CourseBaseInfoVO;
import com.xuecheng.content.service.CourseBaseInfoService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;

@Slf4j
@Service
public class CourseBaseInfoServiceImpl implements CourseBaseInfoService {

    @Resource
    private CourseBaseMapper courseBaseMapper;

    @Resource
    private CourseCategoryMapper courseCategoryMapper;

    @Resource
    private CourseMarketMapper courseMarketMapper;

    /**
     * 课程分页查询
     */
    public PageResult<CourseBase> queryCourseBaseList(PageParams pageParams, QueryCourseParamsDTO dto) {
       // 条件构造
        LambdaQueryWrapper<CourseBase> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.isNotEmpty(dto.getCourseName()), CourseBase::getName, dto.getCourseName());
        wrapper.eq(StringUtils.isNotEmpty(dto.getAuditStatus()), CourseBase::getAuditStatus, dto.getAuditStatus());
        wrapper.eq(StringUtils.isNotEmpty(dto.getPublishStatus()), CourseBase::getStatus, dto.getPublishStatus());
        // 分页
        Page<CourseBase> page = new Page<>(pageParams.getPageNo(), pageParams.getPageSize());
        Page<CourseBase> pageResult = courseBaseMapper.selectPage(page, wrapper);

        return new PageResult<>(pageResult.getRecords(),
                pageResult.getTotal(),
                pageParams.getPageNo(),
                pageParams.getPageSize());
    }

    /**
     * 新增课程
     */
    @Transactional
    public CourseBaseInfoVO saveCourseBaseInfo(Long companyId, AddCourseDTO addCourseDTO) {
        // 参数合法性校验
        // TODO: 2024/1/11

        // 保存课程基本信息
        CourseBase courseBase = new CourseBase();
        BeanUtils.copyProperties(addCourseDTO, courseBase);
        // 设置审核状态
        courseBase.setAuditStatus("202002");
        // 设置发布状态
        courseBase.setStatus("203001");
        // 设置机构id
        courseBase.setCompanyId(companyId);
        // 添加时间
        courseBase.setChangeDate(LocalDateTime.now());
        // insert in to db
        courseBaseMapper.insert(courseBase);

        // 向课程营销信息表内插入信息
        saveCourseMarket(courseBase.getId() ,addCourseDTO);

        // 查询新添加课程，进行信息回显
        return queryCourseById(courseBase.getId());
    }

    /**
     * 添加课程营销信息
     */
    private void saveCourseMarket(Long id, AddCourseDTO addCourseDTO) {
        // 参数合法性校验
        // TODO: 2024/1/11

        CourseMarket courseMarket = new CourseMarket();
        BeanUtils.copyProperties(addCourseDTO, courseMarket);
        courseMarket.setId(id);

        // 营销信息已存在则更新
        if (courseMarketMapper.selectById(id) != null) {
            courseMarketMapper.updateById(courseMarket);
        }else {
            // 营销信息不存在则添加
            courseMarketMapper.insert(courseMarket);
        }

    }

    /**
     * 通过课程id查询课程基本信息
     */
    private CourseBaseInfoVO queryCourseById(Long id) {
        // 课程base信息
        CourseBase courseBase = courseBaseMapper.selectById(id);
        // 课程所属分类信息
        // 1.大分类
        CourseCategory mtCategory = courseCategoryMapper.selectById(courseBase.getMt());
        // 2. 小分类
        CourseCategory stCategory = courseCategoryMapper.selectById(courseBase.getSt());
        // 课程营销信息
        CourseMarket courseMarket = courseMarketMapper.selectById(id);

        //
        CourseBaseInfoVO courseBaseInfoVO = new CourseBaseInfoVO();
        BeanUtils.copyProperties(courseBase, courseBaseInfoVO);
        BeanUtils.copyProperties(courseMarket, courseBaseInfoVO);
        courseBaseInfoVO.setMt(mtCategory.getName());
        courseBaseInfoVO.setSt(stCategory.getName());

        return courseBaseInfoVO;
    }
}
