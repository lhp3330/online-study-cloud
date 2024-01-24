package com.xuecheng.media.service;

import com.xuecheng.base.model.PageParams;
import com.xuecheng.base.model.PageResult;
import com.xuecheng.base.model.RestResponse;
import com.xuecheng.media.model.dto.QueryMediaParamsDTO;
import com.xuecheng.media.model.dto.UploadFileDTO;
import com.xuecheng.media.model.pojo.MediaFiles;
import com.xuecheng.media.model.vo.UploadFileVO;

/**
 * @description 媒资文件管理业务类,minio文件(video)目录结构：文件MD5值前两位/MD5/**
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

    /**
     * save upload file info to db
     */
    MediaFiles saveUploadFileInfo(Long companyId, UploadFileDTO uploadFileDTO, String objeectName, String bucket, String fileMd5Value);

    /**
     * check file is exist
     */
    RestResponse<Boolean> checkFileIsExist(String fileMd5Value);

    /**
     * check fike chunk is exist
     */
    RestResponse<Boolean> checkFileChunkIsExist(String fileMd5Value, int chunk);

    /**
     * upload file chunk
     */
    RestResponse uploadFileChunk(String fileMd5Value, int chunk, String localFileChunkPath);

    /**
     * merge file chunks in minio
     */
    RestResponse mergeFileChunks(Long companyId,String fileMd5,int chunkTotal,UploadFileDTO uploadFileDTO);
}
