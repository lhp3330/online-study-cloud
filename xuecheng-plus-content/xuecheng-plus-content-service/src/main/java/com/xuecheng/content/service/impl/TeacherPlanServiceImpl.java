package com.xuecheng.content.service.impl;

/*
   @Class:TeacherPlanServiceImpl
   @Date:2024/1/13  23:11
*/

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xuecheng.base.enums.CustomException;
import com.xuecheng.base.exception.BaseException;
import com.xuecheng.content.mapper.CourseTeacherMapper;
import com.xuecheng.content.mapper.TeachplanMapper;
import com.xuecheng.content.mapper.TeachplanMediaMapper;
import com.xuecheng.content.model.dto.AddCourseTeacherDTO;
import com.xuecheng.content.model.dto.UpdateOrCreateTeachPlanDTO;
import com.xuecheng.content.model.pojo.CourseTeacher;
import com.xuecheng.content.model.pojo.Teachplan;
import com.xuecheng.content.model.pojo.TeachplanMedia;
import com.xuecheng.content.model.vo.DeleteTeachPlanVO;
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

    @Resource
    private TeachplanMediaMapper teachplanMediaMapper;

    @Resource
    private CourseTeacherMapper courseTeacherMapper;

    /**
     * teachPlanTreeNodes
     * @param courseId
     * @return
     */
    @Override
    public List<TeachPlanVO> queryTeachPlanTreeNodes(Long courseId) {
        List<TeachPlanVO> teachPlanVOS = teachplanMapper.selectTreeNodes(courseId);
        return teachPlanVOS;
    }

    /**
     * update or create teachPlan
     * @param updateOrCreateTeachPlanDTO
     */
    @Transactional
    public void saveOrCreateTeachPlan(UpdateOrCreateTeachPlanDTO updateOrCreateTeachPlanDTO) {
        Long id = updateOrCreateTeachPlanDTO.getId();
        // id not null than do update
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

    /**
     *  create new sortNum for new teachPlan records
     * @param courseId
     * @param parentId
     * @return
     */
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

    /**
     * delete teachPlan
     * @param id
     */
    @Override
    @Transactional
    public DeleteTeachPlanVO deleteTeachPlan(Long id) {
        // is a grade 1 course?
        Teachplan teachplan = teachplanMapper.selectById(id);
        Integer grade = teachplan.getGrade();
        //
        if (grade == 1) {
            LambdaQueryWrapper<Teachplan> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(Teachplan::getParentid, id)
                    .eq(Teachplan::getGrade, 2);
            // judge have grade 2 teachPlan
            List<Teachplan> teachplanList = teachplanMapper.selectList(wrapper);
            if (teachplanList == null || teachplanList.size() == 0) {
                teachplanMapper.deleteById(id);
            }else {
                return new DeleteTeachPlanVO("120409", "this course hava grade 2 course cant be deleted");
            }
        }else {
            // delete grade 2 course and its teachMedia
            LambdaQueryWrapper<TeachplanMedia> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(TeachplanMedia::getTeachplanId, id);
            teachplanMapper.deleteById(id);
            teachplanMediaMapper.delete(wrapper);
        }
        return new DeleteTeachPlanVO("200", "");
    }

    /**
     * teachPlan moveDown(change sort)
     * @param id
     */
    @Override
    @Transactional
    public void teachPlanMoveDown(Long id) {
        // get params id teachPlans sort
        Teachplan originTeachPlan = teachplanMapper.selectById(id);
        Integer originSortNum = originTeachPlan.getOrderby();
        // get the teachPlan which sort gt 1 above params teachPlan
        LambdaQueryWrapper<Teachplan> wrapper = new LambdaQueryWrapper<Teachplan>()
                .eq(Teachplan::getGrade, originTeachPlan.getGrade())
                .eq(Teachplan::getCourseId , originTeachPlan.getCourseId())
                .eq(Teachplan::getParentid, originTeachPlan.getParentid())
                .eq(Teachplan::getOrderby, originSortNum + 1);
        Teachplan preTeachPlan = teachplanMapper.selectOne(wrapper);
        // if null that means the teachPlan now at the last position
        if (preTeachPlan == null) {
            throw new BaseException(CustomException.CANT_MOVE);
        }
        // swap sortNum
        originTeachPlan.setOrderby(preTeachPlan.getOrderby());
        preTeachPlan.setOrderby(originSortNum);
        // db
        teachplanMapper.updateById(originTeachPlan);
        teachplanMapper.updateById(preTeachPlan);
    }

    /**
     * teachPlan moveUp(change sort)
     * @param id
     */
    @Override
    public void teachPlanMoveUp(Long id) {
        // get params id teachPlans sort
        Teachplan originTeachPlan = teachplanMapper.selectById(id);
        Integer originSortNum = originTeachPlan.getOrderby();
        // get the teachPlan which sort gt 1 above params teachPlan
        LambdaQueryWrapper<Teachplan> wrapper = new LambdaQueryWrapper<Teachplan>()
                .eq(Teachplan::getGrade, originTeachPlan.getGrade())
                .eq(Teachplan::getCourseId, originTeachPlan.getCourseId())
                .eq(Teachplan::getParentid, originTeachPlan.getParentid())
                .eq(Teachplan::getOrderby, originSortNum - 1);
        Teachplan preTeachPlan = teachplanMapper.selectOne(wrapper);
        // if null that means the teachPlan now at the first position
        if (preTeachPlan == null) {
            throw new BaseException(CustomException.CANT_MOVE);
        }
        // swap sortNum
        originTeachPlan.setOrderby(preTeachPlan.getOrderby());
        preTeachPlan.setOrderby(originSortNum);
        // db
        teachplanMapper.updateById(originTeachPlan);
        teachplanMapper.updateById(preTeachPlan);
    }
}
