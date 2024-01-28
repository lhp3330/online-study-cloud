package com.xuecheng.media.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xuecheng.media.model.pojo.MediaProcess;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import javax.swing.*;
import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author itcast
 */
public interface MediaProcessMapper extends BaseMapper<MediaProcess> {

    /**
     * query by xxljob index
     */
    List<MediaProcess> selectByShardIndex(int shardTotal, int shardIndex, int count);

    /**
     * start a task
     * @param id 任务id
     * @return 更新记录数
     */
    int startTask(long id);

}
