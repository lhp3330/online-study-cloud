package com.xuecheng.media.service;

/*
   @Class:MediaFileProcessService
   @Date:2024/1/28  22:07
*/

import com.xuecheng.media.model.pojo.MediaProcess;

import java.util.List;

public interface MediaFileProcessService {

    /**
     * query waiting task
     */
    List<MediaProcess> queryMediaProcess(int shardIndex,int shardTotal,int count);

    /**
     * start a task(a lock to process sync)
     */
    boolean startTask(long id);

    /**
     * save finish result status code
     */
    void saveProcessFinishStatus(Long taskId,String status,String fileId,String url,String errorMsg);
}
