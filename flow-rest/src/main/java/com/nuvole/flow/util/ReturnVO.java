package com.nuvole.flow.util;

import lombok.Data;

/**
 * @Description: ReturnVO返回结果封装类
 * @Author: LiuJun
 * @date: 2018/12/04 16:29:49
 */
@Data
public class ReturnVO {

    private int code;

    private String msg;

    private Object content;

    public ReturnVO() {
    }

    public ReturnVO(Boolean b) {
        if (!b) {
            this.code = 0;
            this.msg = "操作失败";
        } else {
            this.code = 1;
            this.msg = "操作成功";
        }
    }

    public ReturnVO(Object content) {
        this.code = 1;
        this.msg = "操作成功";
        this.content = content;
    }

    public ReturnVO(Boolean b, Object content) {
        if (!b) {
            this.code = 0;
            this.msg = "操作失败";
        } else {
            this.code = 1;
            this.msg = "操作成功";
        }
        this.content = content;
    }

    public ReturnVO(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public ReturnVO(int code, String msg, Object content) {
        this.code = code;
        this.msg = msg;
        this.content = content;
    }
}
