package com.xuecheng.content.service.impl;

/*
   @Class:TeacherPlanServiceImpl
   @Date:2024/1/13  23:11
*/

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xuecheng.content.mapper.TeachplanMapper;
import com.xuecheng.content.model.dto.UpdateOrCreateTeachPlanDTO;
import com.xuecheng.content.model.pojo.Teachplan;
import com.xuecheng.content.model.vo.TeachPlanVO;
import com.xuecheng.content.service.TeacherPlanService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class TeacherPlanServiceImpl implements TeacherPlanService {

    @Resource
    private TeachplanMapper teachplanMapper;

    /**
     * teachPlanTreeNodes
     * @param id
     * @return
     */
    @Override
    public List<TeachPlanVO> queryTeachPlanTreeNodes(Long id) {
        return teachplanMapper.selectTreeNodes(id);
    }

    /**
     * update or create teachPlan
     * @param updateOrCreateTeachPlanDTO
     */
    @Transactional
    public void saveOrCreateTeachPlan(UpdateOrCreateTeachPlanDTO updateOrCreateTeachPlanDTO) {
        Long id = updateOrCreateTeachPlanDTO.getId();
        // id不为空则更新
        if (id != null) {
            Teachplan teachplan = teachplanMapper.selectById(id);
            BeanUtils.copyProperties(updateOrCreateTeachPlanDTO, teachplan);
            teachplan.setChangeDate(LocalDateTime.now());
            teachplanMapper.updateById(teachplan);
        }else {
            // sort num
            int sortNum = getSortNum(updateOrCreateTeachPlanDTO.getCourseId(), updateOrCreateTeachPlanDTO.getParentid());
            Teachplan teachplan = new Teachplan();
            BeanUtils.copyProperties(updateOrCreateTeachPlanDTO, teachplan);
            teachplan.setOrderby(sortNum);
            teachplan.setCreateDate(LocalDateTime.now());
            teachplanMapper.insert(teachplan);
        }
    }

    private int getSortNum(Long courseId, Long parentId) {
        // 取出兄弟关系课程,构造新纪录排序数
        LambdaQueryWrapper<Teachplan> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Teachplan::getCourseId, courseId)
                .eq(Teachplan::getParentid, parentId);
        int sort = 0;
        List<Teachplan> teachplans = teachplanMapper.selectList(wrapper);
        // 取当前最大排序数
        List<Integer> sorts = teachplans.stream().map(Teachplan::getOrderby).collect(Collectors.toList());
        for (int i : sorts) {
            if (i > sort) {
                sort = i;
            }
        }
        return sort + 1;
    }
}
