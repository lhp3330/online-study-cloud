package com.xuecheng.content.controller;

/*
   @Class:TeacherPlanController
   @Date:2024/1/13  23:10
*/

import com.xuecheng.content.model.dto.AddCourseTeacherDTO;
import com.xuecheng.content.model.dto.UpdateOrCreateTeachPlanDTO;
import com.xuecheng.content.model.pojo.CourseTeacher;
import com.xuecheng.content.model.vo.DeleteTeachPlanVO;
import com.xuecheng.content.model.vo.TeachPlanVO;
import com.xuecheng.content.service.TeacherPlanService;
import org.springframework.http.HttpStatus;
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
     * @param courseId
     * @return
     */
    @GetMapping("/teachplan/{courseId}/tree-nodes")
    public List<TeachPlanVO> teachPlanTreeNodes(@PathVariable Long courseId) {
        return teacherPlanService.queryTeachPlanTreeNodes(courseId);
    }

    /**
     * update or create teachPlan
     */
    @PostMapping("/teachplan")
    @ResponseStatus(HttpStatus.OK)
    public void newTeachPlan(@RequestBody UpdateOrCreateTeachPlanDTO updateOrCreateTeachPlanDTO) {
        teacherPlanService.saveOrCreateTeachPlan(updateOrCreateTeachPlanDTO);
    }

    /**
     * delete teachPlan
     */
    @DeleteMapping("/teachplan/{id}")
    public DeleteTeachPlanVO deleteTeachPlan(@PathVariable Long id) {
        return teacherPlanService.deleteTeachPlan(id);
    }

    /**
     * teachPlan moveDown(change sort)
     */
    @PostMapping("/teachplan/movedown/{id}")
    public void teachPlanMoveDown(@PathVariable Long id) {
        teacherPlanService.teachPlanMoveDown(id);
    }

    /**
     * teachPlan moveup(change sort)
     */
    @PostMapping("/teachplan/moveup/{id}")
    public void teachPlanMoveUp(@PathVariable Long id) {
        teacherPlanService.teachPlanMoveUp(id);
    }

}
