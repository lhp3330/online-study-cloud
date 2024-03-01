package com.xuecheng.content.service.impl;

/*
   @Class:CoursePreviewServiceImpl
   @Date:2024/3/1  22:39
*/

import com.xuecheng.content.model.vo.CoursePreviewVO;
import com.xuecheng.content.model.vo.CourseBaseInfoVO;
import com.xuecheng.content.model.vo.TeachPlanVO;
import com.xuecheng.content.service.CourseBaseInfoService;
import com.xuecheng.content.service.CoursePreviewService;
import com.xuecheng.content.service.TeacherPlanService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Slf4j
@Service
public class CoursePreviewServiceImpl implements CoursePreviewService {

    @Resource
    private CourseBaseInfoService courseBaseInfoService;

    @Resource
    private TeacherPlanService teacherPlanService;

    /**
     * get course preview info
     */
    @Override
    public CoursePreviewVO getCoursePreviewInfo(Long courseId) {
        // course base info
        CourseBaseInfoVO courseBaseInfoVO = courseBaseInfoService.queryCourseById(courseId);
        // teach plan info
        List<TeachPlanVO> teachPlanVOS = teacherPlanService.queryTeachPlanTreeNodes(courseId);
        //
        return CoursePreviewVO.builder()
                .courseBase(courseBaseInfoVO)
                .teachplans(teachPlanVOS)
                .build();
    }
}
