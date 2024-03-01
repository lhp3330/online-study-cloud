package com.xuecheng.content.model.dto;

/*
   @Class:BindTeachPlanMediaFileDTO
   @Date:2024/1/29  16:28
*/

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BindTeachPlanMediaFileDTO {

    private String mediaId;

    private String fileName;

    private Long teachplanId;
}
