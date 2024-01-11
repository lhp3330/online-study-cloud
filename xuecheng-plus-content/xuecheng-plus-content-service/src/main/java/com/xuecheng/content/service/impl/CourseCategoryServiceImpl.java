package com.xuecheng.content.service.impl;

/*
   @Class:CourseCategoryServiceImpl
   @Date:2024/1/9  17:08
*/

import com.xuecheng.content.mapper.CourseCategoryMapper;
import com.xuecheng.content.model.dto.CourseCategoryTreeDTO;
import com.xuecheng.content.model.pojo.CourseCategory;
import com.xuecheng.content.service.CourseCategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
public class CourseCategoryServiceImpl implements CourseCategoryService {

    @Resource
    private CourseCategoryMapper courseCategoryMapper;


    /**
     * 课程分类查询
     */
    @Override
    public List<CourseCategoryTreeDTO> queryTreeNodes(String id) {

        List<CourseCategoryTreeDTO> courseCategoryTreeDTOs = courseCategoryMapper.selectTreeNodes(id);
        //将list转map,以备使用,排除根节点
        Map<String, CourseCategoryTreeDTO> mapTemp = courseCategoryTreeDTOs.stream()
                .filter(item -> !id.equals(item.getId()))
                .collect(Collectors.toMap(CourseCategory::getId, value -> value, (key1, key2) -> key2));

        //最终返回的list
        List<CourseCategoryTreeDTO> categoryTreeDTOs = new ArrayList<>();
        //依次遍历每个元素,排除根节点
        courseCategoryTreeDTOs.stream()
                .filter(item -> !id.equals(item.getId()))
                .forEach(item -> {
            if (item.getParentid().equals(id)){
                categoryTreeDTOs.add(item);
            }
            //找到当前节点的父节点
            CourseCategoryTreeDTO courseCategoryTreeDTO = mapTemp.get(item.getParentid());
            if (courseCategoryTreeDTO!=null){
                if (courseCategoryTreeDTO.getChildrenTreeNodes() == null){
                    courseCategoryTreeDTO.setChildrenTreeNodes(new ArrayList<CourseCategoryTreeDTO>());
                }
                //下边开始往ChildrenTreeNodes属性中放子节点
                courseCategoryTreeDTO.getChildrenTreeNodes().add(item);
            }
        });
        return categoryTreeDTOs;
    }


}
