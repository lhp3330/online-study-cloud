package com.xuecheng.content.controller;

/*
   @Class:CourseTeacherController
   @Date:2024/1/18  22:36
*/

import com.xuecheng.content.model.dto.AddCourseTeacherDTO;
import com.xuecheng.content.model.pojo.CourseTeacher;
import com.xuecheng.content.service.CourseTeacherService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@Slf4j
@RestController
public class CourseTeacherController {

    @Resource
    private CourseTeacherService courseTeacherService;

    /**
     * get courseTeacher List by courseId
     */
    @GetMapping("/courseTeacher/list/{courseId}")
    public CourseTeacher getCourseTeacher(@PathVariable Long courseId) {
        return courseTeacherService.queryCourseTeacherByCourseId(courseId);
    }

    /**
     * add courseTeacher info
     */
    @PostMapping("/courseTeacher")
    public CourseTeacher addCourseTeacherInfo(@RequestBody AddCourseTeacherDTO addCourseTeacherDTO) {
        return courseTeacherService.addCourseTeacherInfo(addCourseTeacherDTO);
    }

    /**
     * update courseTeacher info
     */
    @PutMapping("/courseTeacher")
    public CourseTeacher updateCourseTeacherInfo(@RequestBody CourseTeacher courseTeacher) {
        return courseTeacherService.updateCourseTeacherInfo(courseTeacher);
    }

    /**
     * delete a course teacher
     */
    @DeleteMapping("/courseTeacher/course/{id}/{courseId}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteCourseTeacher(@PathVariable("id") Long id, @PathVariable("courseId") Long courseId) {
        courseTeacherService.deleteCourseTeacher(id, courseId);
    }
}
