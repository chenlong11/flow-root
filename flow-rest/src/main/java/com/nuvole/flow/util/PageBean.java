package com.nuvole.flow.util;

import com.github.pagehelper.Page;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @Description: bootstrap-talbe 返回结果列表封装
 * @Author: LiuJun
 * @date: 2018/12/04 16:29:49
 */
@Data
public class PageBean<T> implements Serializable {

    private static final long serialVersionUID = 1000L;

    /**
     * 总记录数
     */
    private long total;

    /**
     * 结果集
     */
    private List<T> rows;

    /**
     * 第几页
     */
    private int pageNumber;

    /**
     * 每页记录数
     */
    private int pageSize;

    /**
     * 总页数
     */
    private int pages;

    /**
     * 当前页的数量<=pageSize
     */
    private int size;

    public PageBean(List<T> list) {
        if (list instanceof Page) {
            Page<T> page = (Page<T>) list;
            this.pageNumber = page.getPageNum();
            this.pageSize = page.getPageSize();
            this.total = page.getTotal();
            this.pages = page.getPages();
            this.rows = page;
            this.size = page.size();
        }
    }

    public PageBean(List<T> list, int pageSize, int pageNumber, int total, int pages) {
        this.pageNumber = pageNumber;
        this.pageSize = pageSize;
        this.total = total;
        this.pages = pages;
        this.rows = list;
        this.size = pageSize;
    }

    public PageBean() {
        this.pageNumber = 1;
        this.pageSize = 10;
        this.total = 0;
        this.pages = 0;
        this.rows = new ArrayList<>();
        this.size = 0;
    }
}
