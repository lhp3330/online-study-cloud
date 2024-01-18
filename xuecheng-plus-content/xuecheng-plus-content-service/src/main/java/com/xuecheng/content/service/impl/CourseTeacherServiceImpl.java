package com.xuecheng.content.service.impl;

/*
   @Class:CourseTeacherServiceImpl
   @Date:2024/1/18  22:37
*/

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xuecheng.content.mapper.CourseTeacherMapper;
import com.xuecheng.content.model.dto.AddCourseTeacherDTO;
import com.xuecheng.content.model.pojo.CourseTeacher;
import com.xuecheng.content.service.CourseTeacherService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;

@Slf4j
@Service
public class CourseTeacherServiceImpl implements CourseTeacherService {

    @Resource
    private CourseTeacherMapper courseTeacherMapper;


    /**
     * query course teacher by courseId
     * @param courseId
     */
    @Override
    public CourseTeacher queryCourseTeacherByCourseId(Long courseId) {
        LambdaQueryWrapper<CourseTeacher> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CourseTeacher::getCourseId, courseId);
        return courseTeacherMapper.selectOne(wrapper);
    }

    /**
     * Add courseTeacher info
     * @param addCourseTeacherDTO
     */
    @Override
    @Transactional
    public CourseTeacher addCourseTeacherInfo(AddCourseTeacherDTO addCourseTeacherDTO) {
        CourseTeacher courseTeacher = new CourseTeacher();
        BeanUtils.copyProperties(addCourseTeacherDTO, courseTeacher);
        courseTeacher.setCreateDate(LocalDateTime.now());
        // db
        courseTeacherMapper.insert(courseTeacher);
        CourseTeacher newCourseTeacher = courseTeacherMapper.selectById(courseTeacher.getId());
        return newCourseTeacher;
    }

    /**
     * update courseTeacher info
     * @param courseTeacher
     */
    @Override
    @Transactional
    public CourseTeacher updateCourseTeacherInfo(CourseTeacher courseTeacher) {
        CourseTeacher originCourseTeacher = new CourseTeacher();
        BeanUtils.copyProperties(courseTeacher, originCourseTeacher);
        // to db
        courseTeacherMapper.updateById(originCourseTeacher);
        CourseTeacher teacher = courseTeacherMapper.selectById(courseTeacher.getId());
        return teacher;
    }

    /**
     * delete a course teacher
     * @param id
     * @param courseId
     */
    @Override
    public void deleteCourseTeacher(Long id, Long courseId) {
        LambdaQueryWrapper<CourseTeacher> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CourseTeacher::getId, id)
                .eq(CourseTeacher::getCourseId, courseId);
        // db
        courseTeacherMapper.delete(wrapper);
    }

}
