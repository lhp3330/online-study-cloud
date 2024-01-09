package com.xuecheng.content.service;


import com.xuecheng.content.model.dto.CourseCategoryTreeDTO;

import java.util.List;

public interface CourseCategoryService {

    /**
     * 课程分类查询
     */
    public List<CourseCategoryTreeDTO> queryTreeNodes(String id);
}
