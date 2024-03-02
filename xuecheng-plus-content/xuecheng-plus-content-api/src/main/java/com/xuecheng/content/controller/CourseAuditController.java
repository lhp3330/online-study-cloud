package com.xuecheng.content.controller;

/*
   @Class:CourseAuditController
   @Date:2024/3/2  23:22
*/

import com.xuecheng.content.service.CourseAuditService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@Slf4j
@RestController
public class CourseAuditController {

    @Resource
    private CourseAuditService courseAuditService;

    /**
     * commit course to audit
     * @param courseId
     */
    @PostMapping("/courseaudit/commit/{courseId}")
    public void courseAuditCommit(@PathVariable Long courseId) {
        courseAuditService.courseAudit(courseId);
    }

}
