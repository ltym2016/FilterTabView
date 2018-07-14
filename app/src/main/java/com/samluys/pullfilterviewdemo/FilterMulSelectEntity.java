package com.samluys.pullfilterviewdemo;

import com.samluys.filtertab.base.BaseFilterBean;

import java.util.List;

/**
 * @author luys
 * @describe 筛选多选Entity
 * @date 2018/5/15
 * @email samluys@foxmail.com
 */
public class FilterMulSelectEntity extends BaseFilterBean {

    /**
     * 分类名称
     */
    private String sortname;
    /**
     * 分类Key
     */
    private String sortkey;
    /**
     * 分类数据
     */
    private List<FilterSelectedEntity> sortdata;

    public String getSortname() {
        return sortname;
    }

    public void setSortname(String sortname) {
        this.sortname = sortname;
    }

    public String getSortkey() {
        return sortkey;
    }

    public void setSortkey(String sortkey) {
        this.sortkey = sortkey;
    }

    public List<FilterSelectedEntity> getSortdata() {
        return sortdata;
    }

    public void setSortdata(List<FilterSelectedEntity> sortdata) {
        this.sortdata = sortdata;
    }

    @Override
    public String getItemName() {
        return null;
    }

    @Override
    public int getId() {
        return 0;
    }

    @Override
    public int getSelecteStatus() {
        return 0;
    }

    @Override
    public void setSelecteStatus(int status) {

    }

    @Override
    public String getSortTitle() {
        return sortname;
    }

    @Override
    public List getChildList() {
        return sortdata;
    }

    @Override
    public String getSortKey() {
        return sortkey;
    }
}
