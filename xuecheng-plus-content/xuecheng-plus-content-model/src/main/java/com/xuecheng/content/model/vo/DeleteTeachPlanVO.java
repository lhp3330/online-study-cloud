package com.xuecheng.content.model.vo;

/*
   @Class:DeleteTeachPlanVO
   @Date:2024/1/15  23: 18
*/

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeleteTeachPlanVO {

    /**
     * result code
     */
    private String errCode;

    /**
     * error message
     */
    private String errMessage;
}
