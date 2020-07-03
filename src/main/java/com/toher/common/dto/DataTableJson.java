/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.toher.common.dto;

import com.github.pagehelper.Page;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 *
 * @author Administrator
 */
public class DataTableJson<T> {


    private int draw;
    @ApiModelProperty("总条数")
    private long recordsTotal;
    private long recordsFiltered;
    private List<T> data;
    private String error;

    public DataTableJson() {}

    /**
     * 包装Page对象
     * @param list
     */
    public DataTableJson(List<T> list) {
        if (list instanceof Page) {
            Page page = (Page) list;
            this.data = page;
            this.recordsTotal = page.getTotal();
            this.recordsFiltered = page.getTotal();
        }else{ //没有开启分页直接返回Json
            this.data = list;
        }


    }

    public int getDraw() {
        return draw;
    }

    public void setDraw(int draw) {
        this.draw = draw;
    }

    public long getRecordsTotal() {
        return recordsTotal;
    }

    public void setRecordsTotal(long recordsTotal) {
        this.recordsTotal = recordsTotal;
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public long getRecordsFiltered() {
        return recordsFiltered;
    }

    public void setRecordsFiltered(long recordsFiltered) {
        this.recordsFiltered = recordsFiltered;
    }

}
