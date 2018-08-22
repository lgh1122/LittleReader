package com.liuguanghui.littlereader.viewmodel;

import com.liuguanghui.littlereader.db.entity.ChapterBean;

import java.util.List;

/**
 * Created by liuguanghui on 2018/8/22.
 */

public class BookChaptersBean {

    // 响应业务状态
    private Integer status;

    // 响应消息
    private String msg;

    List<ChapterBean> data;


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

    public List<ChapterBean> getData() {
        return data;
    }

    public void setData(List<ChapterBean> data) {
        this.data = data;
    }
}
