package com.xuecheng.media.controller;



import com.xuecheng.base.model.RestResponse;
import com.xuecheng.media.model.dto.UploadFileDTO;
import com.xuecheng.media.service.MediaFileService;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;

/**
 * &#064;description  大文件上传接口(video)
 */
@RestController
public class BigFilesController {

    @Resource
    private MediaFileService mediaFileService;

    /**
     * check file is exist before uolpad
     * @param fileMd5Value
     * @return
     * @throws Exception
     */
    @PostMapping("/upload/checkfile")
    public RestResponse<Boolean> checkFileIsExist(
            @RequestParam("fileMd5") String fileMd5Value) throws Exception {
        return mediaFileService.checkFileIsExist(fileMd5Value);
    }

    /**
     * check file chunk is exist
     * @param fileMd5Value
     * @param chunk
     * @return
     * @throws Exception
     */
    @PostMapping("/upload/checkchunk")
    public RestResponse<Boolean> checkFileChunkIsExist(@RequestParam("fileMd5") String fileMd5Value,
                                            @RequestParam("chunk") int chunk) throws Exception {
        return mediaFileService.checkFileChunkIsExist(fileMd5Value, chunk);
    }

    /**
     * upload file chunks
     * @param file
     * @param fileMd5Value
     * @param chunk
     * @return
     * @throws Exception
     */
    @PostMapping("/upload/uploadchunk")
    public RestResponse uploadFileChunk(@RequestParam("file") MultipartFile file,
                                    @RequestParam("fileMd5") String fileMd5Value,
                                    @RequestParam("chunk") int chunk) throws Exception {
        // create a temp file to get the local file chunk path
        File tempFile = File.createTempFile("minio", ".temp");
        file.transferTo(tempFile);
        String localFileChunkPath = tempFile.getAbsolutePath();
        return mediaFileService.uploadFileChunk(fileMd5Value, chunk, localFileChunkPath);
    }

    /**
     * merge file chunks in minio
     * @param fileMd5Value
     * @param fileName
     * @param chunkTotal
     * @return
     * @throws Exception
     */
    @PostMapping("/upload/mergechunks")
    public RestResponse mergeFileChunks(@RequestParam("fileMd5") String fileMd5Value,
                                    @RequestParam("fileName") String fileName,
                                    @RequestParam("chunkTotal") int chunkTotal) throws Exception {
        Long companyId = 1232141425L;
        UploadFileDTO uploadFileDTO = UploadFileDTO.builder()
                .filename(fileName)
                .fileType("001002")
                .tags("course video")
                .remark("test")
                .build();
        return mediaFileService.mergeFileChunks(companyId, fileMd5Value, chunkTotal, uploadFileDTO);
    }

}
