package com.xuecheng.content.service;

import com.xuecheng.base.model.PageParams;
import com.xuecheng.base.model.PageResult;
import com.xuecheng.content.model.dto.AddCourseDTO;
import com.xuecheng.content.model.dto.QueryCourseParamsDTO;
import com.xuecheng.content.model.dto.UpdateCourseDTO;
import com.xuecheng.content.model.pojo.CourseBase;
import com.xuecheng.content.model.vo.CourseBaseInfoVO;
import org.springframework.web.bind.annotation.RequestBody;

public interface CourseBaseInfoService {

    /**
     * 课程分页查询
     *
     * @param pageParams
     * @param queryCourseParamsDTO
     */
    public PageResult<CourseBase> queryCourseBaseList(PageParams pageParams, @RequestBody(required = false) QueryCourseParamsDTO queryCourseParamsDTO);

    /**
     * 新增课程
     *
     * @param companyId    机构id
     * @param addCourseDTO
     * @return
     */
    public CourseBaseInfoVO saveCourseBaseInfo(Long companyId, AddCourseDTO addCourseDTO);

    /**
     * 通过id查询课程信息
     *
     * @param id
     * @return
     */
    public CourseBaseInfoVO queryCourseById(Long id);

    /**
     * 修改课程信息
     *
     * @param updateCourseDTO
     * @return
     */
    CourseBaseInfoVO updateCourseBaseInfo(UpdateCourseDTO updateCourseDTO);

}
