package com.xuecheng.content.service;

import com.xuecheng.content.model.dto.AddCourseTeacherDTO;
import com.xuecheng.content.model.dto.UpdateOrCreateTeachPlanDTO;
import com.xuecheng.content.model.pojo.CourseTeacher;
import com.xuecheng.content.model.vo.DeleteTeachPlanVO;
import com.xuecheng.content.model.vo.TeachPlanVO;

import java.util.List;

public interface TeacherPlanService {

    /**
     * query teachPlanTreeNodes
     */
    List<TeachPlanVO> queryTeachPlanTreeNodes(Long courseId);

    /**
     * update or create teachPlan
     */
    void saveOrCreateTeachPlan(UpdateOrCreateTeachPlanDTO updateOrCreateTeachPlanDTO);

    /**
     * delete teachPlan
     */
    DeleteTeachPlanVO deleteTeachPlan(Long id);

    /**
     * teachPlan moveDown(change sort)
     */
    void teachPlanMoveDown(Long id);

    /**
     * teachPlan moveUp(change sort)
     */
    void teachPlanMoveUp(Long id);


}
