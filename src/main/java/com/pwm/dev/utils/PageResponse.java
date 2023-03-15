package com.pwm.dev.utils;

import lombok.Data;

import java.util.List;


/**
 * 查询分页返回对象
 */

@Data
public class PageResponse<T> {

    /**
     * 每页条数
     */
    private int pageSize;

    /**
     * 当前页
     */
    private int currentPage;

    /**
     * 总页数
     */
    private int totalPages;

    /**
     * 总记录数
     */
    private int totalRecords;

    /**
     * 数据集
     */
    private List<T> data;

}
