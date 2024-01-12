package com.xuecheng.content.model.vo;

/*
   @Class:CourseBaseInfoVO
   @Date:2024/1/11  21:50
*/

import com.xuecheng.content.model.dto.AddCourseDTO;
import com.xuecheng.content.model.pojo.CourseBase;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 *  前端课程信息展示VO
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CourseBaseInfoVO extends CourseBase {

    /**
     * 收费规则，对应数据字典
     */
    private String charge;

    /**
     * 价格
     */
    private Float price;


    /**
     * 原价
     */
    private Float originalPrice;

    /**
     * 咨询qq
     */
    private String qq;

    /**
     * 微信
     */
    private String wechat;

    /**
     * 电话
     */
    private String phone;

    /**
     * 有效期天数
     */
    private Integer validDays;

    /**
     * 大分类名称
     */
    private String mtName;

    /**
     * 小分类名称
     */
    private String stName;


}
