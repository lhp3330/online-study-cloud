package com.xuecheng.content.service;

import com.xuecheng.content.model.vo.CoursePreviewVO;

public interface CoursePreviewService {

    /**
     * get course preview info
     */
    CoursePreviewVO getCoursePreviewInfo(Long courseId);
}
