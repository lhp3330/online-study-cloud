package com.xuecheng.media.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.j256.simplemagic.ContentInfo;
import com.j256.simplemagic.ContentInfoUtil;
import com.xuecheng.base.exception.BaseException;
import com.xuecheng.base.exception.DatabaseException;
import com.xuecheng.base.model.PageParams;
import com.xuecheng.base.model.PageResult;
import com.xuecheng.base.model.RestResponse;
import com.xuecheng.media.mapper.MediaFilesMapper;
import com.xuecheng.media.model.dto.QueryMediaParamsDTO;
import com.xuecheng.media.model.dto.UploadFileDTO;
import com.xuecheng.media.model.pojo.MediaFiles;
import com.xuecheng.media.model.vo.UploadFileVO;
import com.xuecheng.media.service.MediaFileService;
import io.minio.*;
import io.minio.errors.*;
import io.minio.messages.DeleteError;
import io.minio.messages.DeleteObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.compress.utils.IOUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.*;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Service
public class MediaFileServiceImpl implements MediaFileService {

    @Resource
    private MediaFileService currentProxy;

    @Resource
    private MediaFilesMapper mediaFilesMapper;

    @Resource
    private MinioClient minioClient;

    // minio bucket
    @Value("${minio.bucket.mediaFiles}")
    private String mediaFilesBucket;

    @Value("${minio.bucket.videoFiles}")
    private String videoBucket;

    /**
     * page query file list
     */
     @Override
     public PageResult<MediaFiles> queryMediaFiles(Long companyId,PageParams pageParams, QueryMediaParamsDTO queryMediaParamsDto) {
      LambdaQueryWrapper<MediaFiles> queryWrapper = new LambdaQueryWrapper<>();
      Page<MediaFiles> page = new Page<>(pageParams.getPageNo(), pageParams.getPageSize());
      Page<MediaFiles> pageResult = mediaFilesMapper.selectPage(page, queryWrapper);
      List<MediaFiles> list = pageResult.getRecords();
      long total = pageResult.getTotal();
      return new PageResult<>(list, total, pageParams.getPageNo(), pageParams.getPageSize());
    }

    /**
     * upload File(image)
     * @param companyId     机构Id
     * @param uploadFileDTO 上传参数
     * @param localFilePath 本地文件路径
     */
    @Override
    public UploadFileVO uploadFile(Long companyId, UploadFileDTO uploadFileDTO, String localFilePath) {
        // 1. get mimeType
        String filename = uploadFileDTO.getFilename();
        String fileExtensionName = filename.substring(filename.lastIndexOf("."));
        String mimeType = getMimeType(fileExtensionName);
        // 2. construst foldeerPaht
        String uploadFolderPath = getUploadFolderPath();
        // 3. get file's md5Value
        String fileMd5Value = getFileMd5Value(new File(localFilePath));
        // 4. construct objectName
        String objectName = uploadFolderPath + fileMd5Value + fileExtensionName;
        // 5. upload file to minio
        boolean uploadResult = uploadFileToMinio(localFilePath, mimeType, mediaFilesBucket, objectName);
        if (!uploadResult) {
            throw new BaseException("uoload file failed");
        }
        // db
        MediaFiles saveResult = currentProxy.saveUploadFileInfo(companyId, uploadFileDTO, objectName, mediaFilesBucket, fileMd5Value);
        if (saveResult == null) {
            throw new DatabaseException("Database error - save file info");
        }
        // result
        UploadFileVO uploadFileVO = new UploadFileVO();
        BeanUtils.copyProperties(saveResult, uploadFileVO);
        return uploadFileVO;
    }

    /**
     * check file is exist
     * @param fileMd5Value
     */
    @Override
    public RestResponse<Boolean> checkFileIsExist(String fileMd5Value) {
        // the condition to define 'files exist': exist in db and minio
        MediaFiles mediaFiles = mediaFilesMapper.selectById(fileMd5Value);
        if (mediaFiles != null) {
            // query minio
            GetObjectArgs getObjectArgs = GetObjectArgs.builder()
                    .bucket(mediaFiles.getBucket())
                    .object(mediaFiles.getFilePath())
                    .build();
            try {
                GetObjectResponse object = minioClient.getObject(getObjectArgs);
                if (object != null) {
                    // file exist
                    return RestResponse.success(true);
                }
            }catch (Exception e) {
                e.printStackTrace();
            }
        }
        // file unexist
        return RestResponse.success(false);
    }

    /**
     * check fike chunk is exist (usually video file will be divided into chunks)
     * @param fileMd5Value
     * @param chunk
     */
    @Override
    public RestResponse<Boolean> checkFileChunkIsExist(String fileMd5Value, int chunk) {
        // fileChunk's path in minio
        String fileChunkPath = getFileChunkFolderPath(fileMd5Value);
        String fileChunkObjectName = fileChunkPath + chunk;
        // query chunk exist in minio
        GetObjectArgs getObjectArgs = GetObjectArgs.builder()
                .bucket(videoBucket)
                .object(fileChunkObjectName)
                .build();
        try {
            GetObjectResponse object = minioClient.getObject(getObjectArgs);
            if (getObjectArgs != null) {
                return RestResponse.success(true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return RestResponse.success(false);
    }

    /**
     * upload file chunk
     * @param fileMd5Value
     * @param chunk
     * @param localFileChunkPath
     */
    @Override
    public RestResponse uploadFileChunk(String fileMd5Value, int chunk, String localFileChunkPath) {
        // construct file path in minio
        String fileChunkPath = getFileChunkFolderPath(fileMd5Value);
        String fileChunkObjectName = fileChunkPath + chunk;
        // get mimeType (usually we dont care of file chunk's mimeType)
        String mimeType = getMimeType(null);
        // uoload to minio
        boolean uploadResult = uploadFileToMinio(localFileChunkPath, mimeType, videoBucket, fileChunkObjectName);
        if (!uploadResult) {
            log.debug("upload file chunk failed - PATH: {}", localFileChunkPath);
            return RestResponse.validFail("upload file chunk failed");
        }
        log.debug("upload file chunk success - PATH: {}", localFileChunkPath);
        return RestResponse.success(true);
    }

    /**
     * merge file chunks in minio
     * @param companyId
     * @param fileMd5Value
     * @param chunkTotal
     * @param uploadFileDTO
     */
    @Override
    public RestResponse mergeFileChunks(Long companyId, String fileMd5Value, int chunkTotal, UploadFileDTO uploadFileDTO) {
        // get file chunks folder path to construct composeList
        String chunkFolderPath = getFileChunkFolderPath(fileMd5Value);
        List<ComposeSource> composeList = Stream.iterate(0, i -> ++i).limit(chunkTotal).map(i ->
            ComposeSource.builder()
                    .bucket(videoBucket)
                    .object(chunkFolderPath.concat(Integer.toString(i)))
                    .build()).collect(Collectors.toList());
        // merger chunks
        // get origin file's extendName
        String originFileExtendName = uploadFileDTO.getFilename().substring(uploadFileDTO.getFilename().lastIndexOf("."));
        // objectName
        String objectName = getMergeFileObjectName(fileMd5Value, originFileExtendName);
        // merge file
        ComposeObjectArgs composeObjectArgs = ComposeObjectArgs.builder()
                .bucket(videoBucket)
                .object(objectName)
                .sources(composeList)
                .build();
        try {
            minioClient.composeObject(composeObjectArgs);
            log.debug("merge file success, fileName: {}", objectName);
        } catch (Exception e) {
            log.debug("merge file failed, md5: {}", fileMd5Value);
            e.printStackTrace();
            return RestResponse.validFail("merge file failed");
        }
        // verify file consistency (compare fileMd5Value)
        // 1. downLoad new merge file and get it's md5Value
        File mergeFile = downLoadFileFromMinio(videoBucket, objectName);
        if (mergeFile == null) {
            log.debug("failed downLoad merge file ,objectName: {}",objectName);
            return RestResponse.validFail(false, "failed downLoad merge file");
        }
        try (FileInputStream fileInputStream = new FileInputStream(mergeFile)) {
            String mergeFileMd5Value = DigestUtils.md5Hex(fileInputStream);
            if (!fileMd5Value.equals(mergeFileMd5Value)) {
                return RestResponse.validFail(false, "verify file failed, so reject to upload");
            }
            // file size
            uploadFileDTO.setFileSize(mergeFile.length());
        }catch (Exception e) {
            log.debug("verify file failed,fileMd5: {},ex: {}",fileMd5Value,e.getMessage());
            return RestResponse.validFail(false, "verify file failed, so reject to upload");
        }finally {
            // delete temp downLoad file
            if (mergeFile != null) {
                mergeFile.delete();
            }
        }
        // save merge file info to db
        MediaFiles saveResult = currentProxy.saveUploadFileInfo(companyId, uploadFileDTO, objectName, videoBucket, fileMd5Value);
        if (saveResult == null) {
            return RestResponse.validFail("merge file failed - error in save file info to db");
        }
        // clear file chunks
        clearFileChunks(chunkFolderPath, chunkTotal);
        return RestResponse.success(true);
    }

    /**
     * downLoad file from minio
     * @param bucket
     * @param objectName
     * @return
     */
    private File downLoadFileFromMinio(String bucket, String objectName) {
        File minioFile = null;
        FileOutputStream outputStream = null;
        try {
            InputStream stream = minioClient.getObject(GetObjectArgs.builder()
                    .bucket(bucket)
                    .object(objectName)
                    .build());
            // 创建临时文件
            minioFile=File.createTempFile("minio", ".merge");
            outputStream = new FileOutputStream(minioFile);
            IOUtils.copy(stream,outputStream);
            return minioFile;
        }catch (Exception e) {
            e.printStackTrace();
        }finally {
            if(outputStream != null){
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    /**
     * get file's mimeType by extension.
     * example:(image -> image/jpg)
     */
    private String getMimeType(String fileExtensionName) {
        if (fileExtensionName == null) {
            fileExtensionName = "";
        }
        ContentInfo mimeTypeMatch = ContentInfoUtil.findMimeTypeMatch(fileExtensionName);
        // 通用mimeType, 先默认取字节流
        String mimeType = MediaType.APPLICATION_OCTET_STREAM_VALUE;

        if (mimeTypeMatch != null) {
            mimeType = mimeTypeMatch.getMimeType();
        }
        return mimeType;
    }

    /**
     * get file's md5Value
     */
    private String getFileMd5Value(File file) {
        try (FileInputStream fileInputStream = new FileInputStream(file)) {
            String fileMd5Value = DigestUtils.md5Hex(fileInputStream);
            return fileMd5Value;
        } catch (Exception e) {
            e.printStackTrace();
            log.error("fail to get file's md5Value");
            return null;
        }
    }

    /**
     * construct folderPath about file which upload to minio (according to date)
     */
    private String getUploadFolderPath() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String folder = sdf.format(new Date()).replace("-", "/")+"/";
        return folder;
    }

    /**
     * uploadFile to Minio
     */
    private boolean uploadFileToMinio(String localFilePath, String mimeType, String bucket, String objectName) {
        try {
            UploadObjectArgs uploadObjectArgs = UploadObjectArgs.builder()
                    .bucket(bucket)
                    .filename(localFilePath)
                    .object(objectName)
                    .contentType(mimeType)
                    .build();
            // upload
            minioClient.uploadObject(uploadObjectArgs);
            log.debug("uploadFile failed-, fileLocalPath: {}, bucket: {}, objectName: {}", localFilePath, bucket, objectName);
        }catch (Exception e) {
            e.printStackTrace();
            log.error("uploadFile failed-, fileLocalPath: {}, bucket: {}, objectName: {}", localFilePath, bucket, objectName);
            return false;
        }
        return true;
    }

    /**
     * save uploadFileInfo to db
     */
    @Override
    @Transactional
    public MediaFiles saveUploadFileInfo(Long companyId, UploadFileDTO uploadFileDTO, String objeectName, String bucket, String fileMd5Value) {
        // select mediaFile by fileMd5Value(actually is file's ID) to avoid duplicate upload
        MediaFiles existMediaFile = mediaFilesMapper.selectById(fileMd5Value);
        if (existMediaFile == null) {
            MediaFiles  mediaFile = new MediaFiles();
            BeanUtils.copyProperties(uploadFileDTO, mediaFile);
            // set file id and orther info
            mediaFile.setId(fileMd5Value);
            mediaFile.setFileId(fileMd5Value);
            mediaFile.setCompanyId(companyId);
            mediaFile.setBucket(bucket);
            mediaFile.setFileSize(uploadFileDTO.getFileSize());
            mediaFile.setFilePath(objeectName);
            mediaFile.setFileType(uploadFileDTO.getFileType());
            mediaFile.setCreateDate(LocalDateTime.now());
            mediaFile.setStatus("1");
            mediaFile.setAuditStatus("002003");
            //url
            String url = "/" + bucket + "/" + objeectName;
            mediaFile.setUrl(url);
            // db
            int insert = mediaFilesMapper.insert(mediaFile);
            if (insert <= 0) {
                log.error("failed to save uploadFile info to db, fileMd5Value: {}, bucket: {}, objectName: {}", fileMd5Value, bucket, objeectName);
                return null;
            }
            return mediaFile;
        }
        return existMediaFile;
    }

    /**
     * get file chunk's Folder Path
     */
    private String getFileChunkFolderPath(String fileMd5Value) {
        return fileMd5Value.substring(0, 1) + "/" + fileMd5Value.substring(1, 2) + "/" + fileMd5Value + "/" + "chunk" + "/";
    }

    /**
     * get file's chunks to construct merge file objectName
     */
    private String getMergeFileObjectName(String fileMd5Value, String extendName) {
        return fileMd5Value.substring(0, 1) + "/" + fileMd5Value.substring(1, 2) + "/" + fileMd5Value + "/" + fileMd5Value + "." + extendName;
    }

    /**
     * clear file chunk after merge
     */
    private void clearFileChunks(String filceChunkFolderPath, int chunkTotal) {
        try {
            List<DeleteObject> deleteObjects = Stream.iterate(0, i -> ++i)
                    .limit(chunkTotal)
                    .map(i -> new DeleteObject(filceChunkFolderPath.concat(Integer.toString(i))))
                    .collect(Collectors.toList());

            RemoveObjectsArgs removeObjectsArgs = RemoveObjectsArgs.builder()
                    .bucket(videoBucket)
                    .objects(deleteObjects)
                    .build();
            Iterable<Result<DeleteError>> results = minioClient.removeObjects(removeObjectsArgs);
            // delete
            results.forEach(r -> {
                DeleteError deleteError = null;
                try {
                    deleteError = r.get();
                } catch (Exception e) {
                    e.printStackTrace();
                    log.error("clear file chunk failed,objectname:{}", deleteError.objectName(),  e);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            log.error("clear file chunk failed,chunkFileFolderPath:{}", filceChunkFolderPath,  e);
        }
    }
}
