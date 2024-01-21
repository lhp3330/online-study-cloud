package com.xuecheng.media.service;

import com.xuecheng.base.model.PageParams;
import com.xuecheng.base.model.PageResult;
import com.xuecheng.media.model.dto.QueryMediaParamsDTO;
import com.xuecheng.media.model.dto.UploadFileDTO;
import com.xuecheng.media.model.pojo.MediaFiles;
import com.xuecheng.media.model.vo.UploadFileVO;

/**
 * @description 媒资文件管理业务类
 * @author Mr.M
 * @date 2022/9/10 8:55
 * @version 1.0
 */
public interface MediaFileService {

    /**
    * @description 媒资文件查询方法
    * @param pageParams 分页参数
    * @param queryMediaParamsDto 查询条件
    */
    PageResult<MediaFiles> queryMediaFiles(Long companyId,PageParams pageParams, QueryMediaParamsDTO queryMediaParamsDto);

    /**
     * @param companyId 机构Id
     * @param uploadFileDTO 上传参数
     * @param localFilePath 本地文件路径
     */
    UploadFileVO uploadFile(Long companyId, UploadFileDTO uploadFileDTO, String localFilePath);

}
