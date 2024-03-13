package com.xuecheng.content.feignclient.fallback;

/*
   @Class:MediaServiceClientFallback
   @Date:2024/3/13  23:21
*/

import com.xuecheng.content.feignclient.MediaServiceClient;
import com.xuecheng.media.model.vo.UploadFileVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

/**
 * 降级处理类，不可捕获exception
 */
@Slf4j
@Component
public class MediaServiceClientFallback implements MediaServiceClient {
    @Override
    public UploadFileVO uploadFile(MultipartFile upLoadFile, String objectName) {
        log.error("降级>>>>>>>>>>>>>>>>");
        return null;
    }
}
