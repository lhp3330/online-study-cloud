package com.xuecheng.content.model.vo;

/*
   @Class:TeachPlanVO
   @Date:2024/1/13  23:14
*/

import com.xuecheng.content.model.pojo.Teachplan;
import com.xuecheng.content.model.pojo.TeachplanMedia;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TeachPlanVO extends Teachplan {

    /**
     * 子节点
     */
    private List<TeachPlanVO> teachPlanTreeNodes;

    /**
     * media
     */
    private TeachplanMedia teachPlanMedia;
}
