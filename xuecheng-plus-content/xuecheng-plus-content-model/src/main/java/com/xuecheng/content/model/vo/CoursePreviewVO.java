package com.xuecheng.content.model.vo;

/*
   @Class:CoursePreviewVO
   @Date:2024/3/1  22:33
*/

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CoursePreviewVO {
    // course base info and course market info
    private CourseBaseInfoVO courseBase;

    // course teacher info
    private List<TeachPlanVO> teachplans;
}
