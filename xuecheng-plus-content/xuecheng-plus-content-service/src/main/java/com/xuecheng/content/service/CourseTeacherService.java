package com.xuecheng.content.service;

import com.xuecheng.content.model.dto.AddCourseTeacherDTO;
import com.xuecheng.content.model.pojo.CourseTeacher;

public interface CourseTeacherService {

    /**
     * query course teacher by courseId
     */
    CourseTeacher queryCourseTeacherByCourseId(Long courseId);

    /**
     * Add courseTeacher info
     */
    CourseTeacher addCourseTeacherInfo(AddCourseTeacherDTO addCourseTeacherDTO);

    /**
     * update courseTeacher info
     */
    CourseTeacher updateCourseTeacherInfo(CourseTeacher courseTeacher);

    /**
     * delete a course teacher
     */
    void deleteCourseTeacher(Long id, Long courseId);
}
