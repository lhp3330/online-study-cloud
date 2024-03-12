package com.xuecheng.content.service.jobhandler;

/*
   @Class:CoursePublishTask
   @Date:2024/3/12  21:34
*/

import com.xuecheng.messagesdk.model.po.MqMessage;
import com.xuecheng.messagesdk.service.MessageProcessAbstract;
import com.xuecheng.messagesdk.service.MqMessageService;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CoursePublishTask extends MessageProcessAbstract {


    @XxlJob("coursePublishHandler")
    public void coursePublishHandler() {
        System.out.println(XxlJobHelper.getShardIndex());
        log.info("xxljob>>>>>>>>>>");
    }




    /**
     * @param mqMessage execute task content
     * @return boolean
     */
    @Override
    public boolean execute(MqMessage mqMessage) {
        //
        Long courseId = Long.valueOf(mqMessage.getBusinessKey1());

        // es
        saveCourseIndex(mqMessage, courseId);
        // redis
        saveCourseCache(mqMessage, courseId);
        // minio
        generateCourseHtml(mqMessage, courseId);
        return false;
    }

    /**
     * generate Static page (table filed: stage 1)
     */
    private void generateCourseHtml(MqMessage mqMessage, Long courseId) {
        //常规幂等性处理
        Long taskId = mqMessage.getId();
        MqMessageService mqMessageService = this.getMqMessageService();
        int stageOne = mqMessageService.getStageOne(taskId);
        if (stageOne == 1) {
            log.info("task has done");
            return;
        }
        // TODO: 2024/3/12 .....

        //
        mqMessageService.completedStageOne(taskId);
    }

    /**
     * save course index (table filed: stage 2)
     */
    private void saveCourseIndex(MqMessage mqMessage, Long courseId) {
        Long taskId = mqMessage.getId();
        MqMessageService mqMessageService = this.getMqMessageService();
        int stageTwo = mqMessageService.getStageTwo(taskId);
        if (stageTwo == 1) {
            log.info("task has done");
            return;
        }
        // TODO: 2024/3/12 .....

        //
        mqMessageService.completedStageTwo(taskId);
    }

    /**
     * save course cache (table filed: stage 3)
     */
    private void saveCourseCache(MqMessage mqMessage, Long courseId) {
        Long taskId = mqMessage.getId();
        MqMessageService mqMessageService = this.getMqMessageService();
        int stageThree = mqMessageService.getStageThree(taskId);
        if (stageThree == 1) {
            log.info("task has done");
            return;
        }
        // TODO: 2024/3/12 .....

        //
        mqMessageService.completedStageThree(taskId);
    }

}
