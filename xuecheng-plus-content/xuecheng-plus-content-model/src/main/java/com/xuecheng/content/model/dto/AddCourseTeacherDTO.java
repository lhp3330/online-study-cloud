package com.xuecheng.content.model.dto;

/*
   @Class:AddCourseTeacherDTO
   @Date:2024/1/18  22:15
*/

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddCourseTeacherDTO {

    private Long courseId;

    private String teacherName;

    /**
     * teacher职位
     */
    private String position;

    private String introduction;

}
