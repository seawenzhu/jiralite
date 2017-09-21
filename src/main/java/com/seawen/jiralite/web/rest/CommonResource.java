package com.seawen.jiralite.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.qiniu.storage.model.DefaultPutRet;
import com.seawen.jiralite.service.dto.FileResultDTO;
import com.seawen.jiralite.service.util.QiniuyunUtil;
import com.seawen.jiralite.web.rest.util.HeaderUtil;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Iterator;

/**
 * Common API Here, 文件上传...
 */
@RestController
@RequestMapping("/api/common")
public class CommonResource {
    private final Logger log = LoggerFactory.getLogger(CommonResource.class);

    @PostMapping("/file/upload")
    @Timed
    public ResponseEntity<FileResultDTO> uploadFile(HttpServletRequest request, HttpServletResponse response){
        try {
            //MultipartResolver resolver = new CommonsMultipartResolver(request.getSession().getServletContext());
            MultipartHttpServletRequest mRequest = (MultipartHttpServletRequest) request;
            Iterator<String> iter = mRequest.getFileNames();
            while (iter.hasNext()) {
                FileResultDTO result = QiniuyunUtil.uploadInputStream(mRequest.getFile(iter.next()).getBytes());
                return ResponseEntity.ok().body(result);
            }
            return ResponseEntity.ok(null);
        } catch (Exception e) {
            log.error(">>>common>>>image>>>exception:",e);
            return ResponseEntity.ok(null);
        }
    }

}
