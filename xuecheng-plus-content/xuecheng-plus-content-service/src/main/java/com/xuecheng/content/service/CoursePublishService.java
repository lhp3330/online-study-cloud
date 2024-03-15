package com.xuecheng.content.service;

import java.io.File;

public interface CoursePublishService {

    /**
     * publish course
     * @param courseId
     * @param companyId
     */
    void publishCourse(Long courseId, Long companyId);

    /**
     * upload course static page html
     * @param courseId
     * @param file
     */
    void uploadCourseHtml(Long courseId, File file);

    /**
     * generate course static page html
     * @param courseId
     * @return
     */
    File generateCourseHtml(Long courseId);

    /**
     * create a new course index
     */

}
