package com.xuecheng.content.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xuecheng.content.model.pojo.Teachplan;
import com.xuecheng.content.model.vo.TeachPlanVO;

import java.util.List;

/**
 * <p>
 * 课程计划 Mapper 接口
 * </p>
 *
 * @author itcast
 */
public interface TeachplanMapper extends BaseMapper<Teachplan> {

    /**
     * get teachPlan Tree Nodes
     */
    List<TeachPlanVO> selectTreeNodes(Long courseId);

}
