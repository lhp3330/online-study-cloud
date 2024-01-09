package com.xuecheng.content.model.dto;

import com.xuecheng.content.model.pojo.CourseCategory;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @description 课程分类树型结点dto
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CourseCategoryTreeDTO extends CourseCategory implements Serializable {

    // 课程分类子节点
    List<CourseCategoryTreeDTO> childrenTreeNodes;
}
