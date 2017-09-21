package com.seawen.jiralite.service.dto;

import com.qiniu.storage.model.DefaultPutRet;

import java.io.Serializable;

/**
 *
 */
public class FileResultDTO implements Serializable {

    private String link;

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

}
