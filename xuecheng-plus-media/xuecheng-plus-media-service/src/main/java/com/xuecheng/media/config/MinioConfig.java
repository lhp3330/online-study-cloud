package com.xuecheng.media.config;

/*
   @Class:MinioConfig
   @Date:2024/1/21  18:32
*/

import io.minio.MinioClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * minio file system config
 */

@Configuration
public class MinioConfig {

    @Value("${minio.endpoint}")
    private String endPoint;

    @Value("${minio.accessKey}")
    private String accessKey;

    @Value("${minio.secretKey}")
    private String secretKey;

    @Bean
    public MinioClient minioClient() {
        return MinioClient.builder()
                .endpoint(endPoint)
                .credentials(accessKey, secretKey)
                .build();
    }
}
