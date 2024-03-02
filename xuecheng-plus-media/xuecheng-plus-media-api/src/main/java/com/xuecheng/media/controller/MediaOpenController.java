package com.xuecheng.media.controller;

/*
   @Class:MediaOpenController
   @Date:2024/3/1  23:38
*/

import com.xuecheng.base.model.RestResponse;
import com.xuecheng.media.model.pojo.MediaFiles;
import com.xuecheng.media.service.MediaFileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@Slf4j
@RestController
@RequestMapping("/open")
public class MediaOpenController {

    @Resource
    private MediaFileService mediaFileService;

    /**
     * get course video's url which brodcasting now
     */
    @GetMapping("/preview/{mediaId}")
    public RestResponse getVideoUrl(@PathVariable String mediaId) {
        RestResponse<MediaFiles> mediaFilesRestResponse = mediaFileService.queryMediaFileById(mediaId);
        String url = mediaFilesRestResponse.getResult().getUrl();
        return RestResponse.success(url);
    }
}
