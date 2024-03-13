package com.xuecheng.content.controller;

/*
   @Class:CourseAuditController
   @Date:2024/3/2  23:22
*/

import com.xuecheng.content.config.MultipartSupportConfig;
import com.xuecheng.content.feignclient.MediaServiceClient;
import com.xuecheng.content.service.CourseAuditService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;

@Slf4j
@RestController
public class CourseAuditController {

    @Resource
    private CourseAuditService courseAuditService;

    @Resource
    private MediaServiceClient mediaServiceClient;

    /**
     * commit course to audit
     * @param courseId
     */
    @PostMapping("/courseaudit/commit/{courseId}")
    public void courseAuditCommit(@PathVariable Long courseId) {
        courseAuditService.courseAudit(courseId);
    }

}
