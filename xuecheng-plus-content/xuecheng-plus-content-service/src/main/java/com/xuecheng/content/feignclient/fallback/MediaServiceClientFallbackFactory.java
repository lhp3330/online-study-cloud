package com.xuecheng.content.feignclient.fallback;

/*
   @Class:MediaServiceClientFallbackFactory
   @Date:2024/3/13  23:23
*/

import com.xuecheng.content.feignclient.MediaServiceClient;
import com.xuecheng.media.model.vo.UploadFileVO;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

/**
 * 降级处理工厂类，可捕获exception
 */
@Slf4j
@Component
public class MediaServiceClientFallbackFactory implements FallbackFactory<MediaServiceClient> {


    @Override
    public MediaServiceClient create(Throwable throwable) {
        return new MediaServiceClient() {
            @Override
            public UploadFileVO uploadFile(MultipartFile upLoadFile, String objectName) {
                log.error(throwable.getMessage());
                return null;
            }
        };
    }
}
