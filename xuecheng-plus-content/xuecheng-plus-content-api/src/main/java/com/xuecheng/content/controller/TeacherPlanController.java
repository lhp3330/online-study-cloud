package com.xuecheng.content.controller;

/*
   @Class:TeacherPlanController
   @Date:2024/1/13  23:10
*/

import com.xuecheng.content.model.dto.UpdateOrCreateTeachPlanDTO;
import com.xuecheng.content.model.vo.TeachPlanVO;
import com.xuecheng.content.service.TeacherPlanService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 教学计划相关接口
 */
@RestController
public class TeacherPlanController {

    @Resource
    private TeacherPlanService teacherPlanService;

    /**
     * teachPlanTreeNodes query
     * @param id
     * @return
     */
    @GetMapping("/teachplan/{id}/tree-nodes")
    public List<TeachPlanVO> teachPlanTreeNodes(@PathVariable Long id) {
        return teacherPlanService.queryTeachPlanTreeNodes(id);
    }

    /**
     * update or create teachPlan
     */
    @PostMapping("/teachplan")
    public void newTeachPlan(@RequestBody UpdateOrCreateTeachPlanDTO updateOrCreateTeachPlanDTO) {
        teacherPlanService.saveOrCreateTeachPlan(updateOrCreateTeachPlanDTO);
    }
}
