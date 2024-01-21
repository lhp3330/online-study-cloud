package com.xuecheng;


import com.spring4all.swagger.EnableSwagger2Doc;
import io.minio.MinioClient;
import io.minio.UploadObjectArgs;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;

@EnableSwagger2Doc
@SpringBootApplication
public class MediaApplication {
	public static void main(String[] args) {
		SpringApplication.run(MediaApplication.class, args);
	}
}
