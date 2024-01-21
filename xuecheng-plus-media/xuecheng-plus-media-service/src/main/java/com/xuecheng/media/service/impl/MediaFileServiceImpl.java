package com.xuecheng.media.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.j256.simplemagic.ContentInfo;
import com.j256.simplemagic.ContentInfoUtil;
import com.xuecheng.base.exception.BaseException;
import com.xuecheng.base.exception.DatabaseException;
import com.xuecheng.base.model.PageParams;
import com.xuecheng.base.model.PageResult;
import com.xuecheng.media.mapper.MediaFilesMapper;
import com.xuecheng.media.model.dto.QueryMediaParamsDTO;
import com.xuecheng.media.model.dto.UploadFileDTO;
import com.xuecheng.media.model.pojo.MediaFiles;
import com.xuecheng.media.model.vo.UploadFileVO;
import com.xuecheng.media.service.MediaFileService;
import io.minio.MinioClient;
import io.minio.UploadObjectArgs;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Slf4j
@Service
public class MediaFileServiceImpl implements MediaFileService {

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
    @Transactional
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
        MediaFiles saveResult = saveUploadFileInfo(companyId, uploadFileDTO, objectName, mediaFilesBucket, fileMd5Value);
        if (saveResult == null) {
            throw new DatabaseException("Database error-");
        }
        // result
        UploadFileVO uploadFileVO = new UploadFileVO();
        BeanUtils.copyProperties(saveResult, uploadFileVO);
        return uploadFileVO;
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
    private MediaFiles saveUploadFileInfo(Long companyId, UploadFileDTO uploadFileDTO, String objeectName, String bucket, String fileMd5Value) {
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
}

