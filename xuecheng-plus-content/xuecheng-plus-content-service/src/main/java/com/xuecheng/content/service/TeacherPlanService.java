package com.xuecheng.content.service;

import com.xuecheng.content.model.dto.UpdateOrCreateTeachPlanDTO;
import com.xuecheng.content.model.vo.TeachPlanVO;

import java.util.List;

public interface TeacherPlanService {

    /**
     * query teachPlanTreeNodes
     */
    List<TeachPlanVO> queryTeachPlanTreeNodes(Long id);

    /**
     * update or create teachPlan
     */
    void saveOrCreateTeachPlan(UpdateOrCreateTeachPlanDTO updateOrCreateTeachPlanDTO);
}
