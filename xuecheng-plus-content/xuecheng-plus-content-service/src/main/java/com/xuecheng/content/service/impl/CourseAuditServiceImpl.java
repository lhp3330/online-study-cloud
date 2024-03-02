package com.xuecheng.content.service.impl;

/*
   @Class:CourseAuditServiceImpl
   @Date:2024/3/2  23:23
*/

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xuecheng.base.exception.BaseException;
import com.xuecheng.content.mapper.CourseBaseMapper;
import com.xuecheng.content.mapper.CourseMarketMapper;
import com.xuecheng.content.mapper.CoursePublishPreMapper;
import com.xuecheng.content.mapper.CourseTeacherMapper;
import com.xuecheng.content.model.pojo.CourseBase;
import com.xuecheng.content.model.pojo.CourseMarket;
import com.xuecheng.content.model.pojo.CoursePublishPre;
import com.xuecheng.content.model.pojo.CourseTeacher;
import com.xuecheng.content.model.vo.CourseBaseInfoVO;
import com.xuecheng.content.model.vo.TeachPlanVO;
import com.xuecheng.content.service.CourseAuditService;
import com.xuecheng.content.service.CourseBaseInfoService;
import com.xuecheng.content.service.TeacherPlanService;
import jdk.jfr.Threshold;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class CourseAuditServiceImpl implements CourseAuditService {

    @Resource
    private TeacherPlanService teacherPlanService;

    @Resource
    private CourseBaseMapper courseBaseMapper;

    @Resource
    private CourseMarketMapper courseMarketMapper;


    @Resource
    private CourseTeacherMapper courseTeacherMapper;

    @Resource
    private CoursePublishPreMapper coursePublishPreMapper;

    @Resource
    private CourseBaseInfoService courseBaseInfoService;

    /**
     * audit course by id
     * @param courseId
     */
    @Override
    @Transactional
    public void courseAudit(Long courseId) {
        CourseBase courseBase = courseBaseMapper.selectById(courseId);
        String auditStatus = courseBase.getAuditStatus();
        if (auditStatus.equals("202003")) {
            throw new BaseException("course was in auditing status now");
        }
        //base date
        CourseBaseInfoVO courseBaseInfo = courseBaseInfoService.queryCourseById(courseId);
        //JSON data
        CourseMarket courseMarket = courseMarketMapper.selectById(courseId);
        String courseMarketJson = JSON.toJSONString(courseMarket);
        List<TeachPlanVO> teachPlan = teacherPlanService.queryTeachPlanTreeNodes(courseId);
        if (teachPlan == null ||teachPlan.size() == 0) {
            throw new BaseException("course plan is blank");
        }
        String teacherPlanJson = JSON.toJSONString(teachPlan);
        LambdaQueryWrapper<CourseTeacher> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CourseTeacher::getCourseId, courseMarketJson);
        List<CourseTeacher> courseTeachers = courseTeacherMapper.selectList(wrapper);
        String courseTeachersJson = JSON.toJSONString(courseTeachers);
        //
        CoursePublishPre coursePublishPre = new CoursePublishPre();
        BeanUtils.copyProperties(courseBaseInfo, coursePublishPre);
        coursePublishPre.setId(courseId);
        coursePublishPre.setMarket(courseMarketJson);
        coursePublishPre.setTeachers(courseTeachersJson);
        coursePublishPre.setTeachplan(teacherPlanJson);
        coursePublishPre.setAuditDate(LocalDateTime.now());
        coursePublishPre.setCompanyId(11111L);
        coursePublishPre.setStatus("202003");
        //sometimes may be just update
        CoursePublishPre existCoursePublishPre = coursePublishPreMapper.selectById(courseId);
        if (existCoursePublishPre != null) {
            coursePublishPreMapper.updateById(coursePublishPre);
        }else {
            coursePublishPreMapper.insert(coursePublishPre);
        }
        // update course base
        courseBase.setAuditStatus("202003");
        courseBaseMapper.updateById(courseBase);
    }
}
