package com.hummerrisk.controller.request.fs;

import com.hummerrisk.base.domain.FileSystem;

import java.util.Map;

/**
 * harris
 */
public class FsRequest extends FileSystem {

    private String userName;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    private Map<String, Object> combine;

    public Map<String, Object> getCombine() {
        return combine;
    }

    public void setCombine(Map<String, Object> combine) {
        this.combine = combine;
    }

}
