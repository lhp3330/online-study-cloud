package com.xuecheng.media.controller;

import com.xuecheng.base.model.PageParams;
import com.xuecheng.base.model.PageResult;
import com.xuecheng.media.model.dto.QueryMediaParamsDTO;
import com.xuecheng.media.model.dto.UploadFileDTO;
import com.xuecheng.media.model.pojo.MediaFiles;
import com.xuecheng.media.model.vo.UploadFileVO;
import com.xuecheng.media.service.MediaFileService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;

/**
 * @description 媒资文件管理接口
 */
@RestController
public class MediaFilesController {

    @Resource
    MediaFileService mediaFileService;

    @PostMapping("/files")
    public PageResult<MediaFiles> list(PageParams pageParams, @RequestBody QueryMediaParamsDTO queryMediaParamsDTO) {
        Long companyId = 1232141425L;
        return mediaFileService.queryMediaFiles(companyId, pageParams, queryMediaParamsDTO);
    }

    /**
     * uploadFile (image)
     */
    @RequestMapping(value = "/upload/coursefile", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public UploadFileVO uploadFile(@RequestPart("filedata") MultipartFile upLoadFile) throws IOException {
        // prepare params
        Long companyId = 1232141425L;
        // construct a temp file
        File tempFile = File.createTempFile("minio", ".temp");
        upLoadFile.transferTo(tempFile);
        // DTO params
        UploadFileDTO uploadFileDTO = UploadFileDTO.builder()
                .fileSize(upLoadFile.getSize())
                .fileType("001001")
                .filename(upLoadFile.getOriginalFilename())
                .build();
        // localFilePaht
        String localFilePath = tempFile.getAbsolutePath();
        return mediaFileService.uploadFile(companyId, uploadFileDTO, localFilePath);
    }



}
