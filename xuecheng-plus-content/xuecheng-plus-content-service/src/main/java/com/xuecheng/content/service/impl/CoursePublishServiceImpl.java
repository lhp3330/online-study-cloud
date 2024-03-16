package com.xuecheng.content.service.impl;

/*
   @Class:CoursePublishServiceImpl
   @Date:2024/3/9  22:19
*/

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xuecheng.base.exception.BaseException;
import com.xuecheng.base.exception.DatabaseException;
import com.xuecheng.content.config.MultipartSupportConfig;
import com.xuecheng.content.feignclient.MediaServiceClient;
import com.xuecheng.content.mapper.CourseBaseMapper;
import com.xuecheng.content.mapper.CoursePublishMapper;
import com.xuecheng.content.mapper.CoursePublishPreMapper;
import com.xuecheng.content.model.pojo.CourseBase;
import com.xuecheng.content.model.pojo.CoursePublish;
import com.xuecheng.content.model.pojo.CoursePublishPre;
import com.xuecheng.content.model.vo.CoursePreviewVO;
import com.xuecheng.content.service.CoursePreviewService;
import com.xuecheng.content.service.CoursePublishService;
import com.xuecheng.media.model.dto.UploadFileDTO;
import com.xuecheng.media.model.vo.UploadFileVO;
import com.xuecheng.messagesdk.service.MqMessageService;
import freemarker.template.Configuration;
import freemarker.template.Template;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.*;

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

    @Resource
    private MediaServiceClient mediaServiceClient;

    @Resource
    private CoursePreviewService coursePreviewService;


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
     * upload course static page html
     * @param courseId
     * @param file
     */
    @Override
    public void uploadCourseHtml(Long courseId, File file) {
        MultipartFile multipartFile = MultipartSupportConfig.getMultipartFile(file);
        UploadFileVO course = mediaServiceClient.uploadFile(multipartFile, "course/" + courseId + ".html");
        if(course == null){
            throw new BaseException("upload static page failed!");
        }
    }

    /**
     * generate course static page html
     * @param courseId
     * @return
     */
    @Override
    public File generateCourseHtml(Long courseId) {
        File htmlFile  = null;
        try {
            //配置freemarker
            Configuration configuration = new Configuration(Configuration.getVersion());
            //选指定模板路径,classpath下templates下
            //得到classpath路径
            String classpath = Objects.requireNonNull(this.getClass().getResource("/")).getPath();
            configuration.setDirectoryForTemplateLoading(new File(classpath + "/templates/"));
            //设置字符编码
            configuration.setDefaultEncoding("utf-8");
            //指定模板文件名称
            Template template = configuration.getTemplate("course_template.ftl");
            //准备数据
            CoursePreviewVO coursePreviewInfo = coursePreviewService.getCoursePreviewInfo(courseId);
            Map<String, Object> map = new HashMap<>();
            map.put("model", coursePreviewInfo);
            //静态化 参数1：模板，参数2：数据模型
            String content = FreeMarkerTemplateUtils.processTemplateIntoString(template, map);
            //将静态化内容输出到文件中
            InputStream inputStream = IOUtils.toInputStream(content);
            //创建静态化文件
            htmlFile = File.createTempFile("course", ".html");
            log.debug("generate course static page: {}", htmlFile.getAbsolutePath());
            //输出流
            FileOutputStream outputStream = new FileOutputStream(htmlFile);
            IOUtils.copy(inputStream, outputStream);
        } catch (Exception e) {
            log.error("upload course static page failed!");
            throw new BaseException("upload course static page failed!");
        }
        return htmlFile;
    }

    /**
     * course depublish
     */
    @Override
    public void dePublishCourse(Long courseId) {
        CourseBase courseBase = CourseBase.builder()
                .auditStatus("202002")
                .status("203003")
                .build();
        LambdaQueryWrapper<CourseBase> wrapper = new LambdaQueryWrapper<CourseBase>().eq(CourseBase::getId, courseId);
        courseBaseMapper.update(courseBase, wrapper);
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
        courseBase.setAuditStatus("202004");
        courseBase.setStatus("203002");
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
