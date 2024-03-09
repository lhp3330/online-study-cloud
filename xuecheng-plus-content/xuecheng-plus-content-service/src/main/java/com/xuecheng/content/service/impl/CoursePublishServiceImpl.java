package com.xuecheng.content.service.impl;

/*
   @Class:CoursePublishServiceImpl
   @Date:2024/3/9  22:19
*/

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xuecheng.base.exception.DatabaseException;
import com.xuecheng.content.mapper.CourseBaseMapper;
import com.xuecheng.content.mapper.CoursePublishMapper;
import com.xuecheng.content.mapper.CoursePublishPreMapper;
import com.xuecheng.content.model.pojo.CourseBase;
import com.xuecheng.content.model.pojo.CoursePublish;
import com.xuecheng.content.model.pojo.CoursePublishPre;
import com.xuecheng.content.service.CoursePreviewService;
import com.xuecheng.content.service.CoursePublishService;
import com.xuecheng.messagesdk.service.MqMessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Slf4j
@Service
public class CoursePublishServiceImpl implements CoursePublishService {

    @Resource
    private CoursePublishPreMapper coursePublishPreMapper;

    @Resource
    private CourseBaseMapper courseBaseMapper;

    @Resource
    private CoursePublishMapper coursePublishMapper;

    @Resource
    private MqMessageService mqMessageService;


    /**
     * publish course
     * @param courseId
     * @param companyId
     */
    @Override
    @Transactional
    public void publishCourse(Long courseId, Long companyId) {
        CoursePublishPre coursePublishPre = coursePublishPreMapper.selectById(courseId);
        if (coursePublishPre == null) {
            throw new DatabaseException("none course will be published");
        }
        // update course base
        updateCourseBaseStatus(courseId);
        // new record to course_publish
        saveCoursePublishRecord(coursePublishPre, courseId);
        // mq_message
        saveCoursePublishMessage(courseId);
        // delete record in course_publish_pre
        coursePublishPreMapper.deleteById(coursePublishPre);
    }

    /**
     * save course publish mq
     */
    private void saveCoursePublishMessage(Long courseId) {
        mqMessageService.addMessage("course_publish", String.valueOf(courseId), null, null);
    }

    /**
     * update course_base status
     */
    private void updateCourseBaseStatus(Long courseId) {
        CourseBase courseBase = new CourseBase();
        courseBase.setAuditStatus("203002");
        courseBase.setStatus("202004");
        LambdaQueryWrapper<CourseBase> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CourseBase::getId, courseId);
        courseBaseMapper.update(courseBase, wrapper);
    }

    /**
     * save record course_publish
     */
    private void saveCoursePublishRecord(CoursePublishPre coursePublishPre ,Long courseId) {
        CoursePublish coursePublish = coursePublishMapper.selectById(courseId);
        CoursePublish publish = new CoursePublish();
        BeanUtils.copyProperties(coursePublishPre, publish);
        publish.setStatus("202004");
        if (coursePublish == null) {
            coursePublishMapper.insert(publish);
        }else {
            coursePublishMapper.updateById(publish);
        }
    }
}
