package com.hummerrisk.controller.request.webMsg;

import com.hummerrisk.base.domain.WebMsg;

import java.util.Map;

/**
 * harris
 */
public class WebMsgRequest extends WebMsg {

    private Map<String, Object> combine;

    public Map<String, Object> getCombine() {
        return combine;
    }

    public void setCombine(Map<String, Object> combine) {
        this.combine = combine;
    }

}
