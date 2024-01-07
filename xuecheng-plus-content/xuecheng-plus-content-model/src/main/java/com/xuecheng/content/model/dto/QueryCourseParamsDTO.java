package com.xuecheng.content.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @description 课程查询参数DTO
 */
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class QueryCourseParamsDTO {

    //审核状态
    private String auditStatus;
    //课程名称
    private String courseName;
    //发布状态
    private String publishStatus;

}

