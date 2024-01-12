package com.xuecheng.content.model.dto;

/*
   @Class:UpdateCourseDTO
   @Date:2024/1/13  0:18
*/

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *  课程修改模型类
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateCourseDTO extends AddCourseDTO {

    private Long id;

}
