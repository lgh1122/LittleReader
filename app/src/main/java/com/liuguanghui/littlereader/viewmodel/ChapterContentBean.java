package com.liuguanghui.littlereader.viewmodel;

import com.liuguanghui.littlereader.entity.SpiderChapter;

/**
 * Created by liuguanghui on 2018/9/2.
 */

public class ChapterContentBean {
    // 响应业务状态
    private Integer status;

    // 响应消息
    private String msg;

    private SpiderChapter data;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public SpiderChapter getData() {
        return data;
    }

    public void setData(SpiderChapter data) {
        this.data = data;
    }
}
