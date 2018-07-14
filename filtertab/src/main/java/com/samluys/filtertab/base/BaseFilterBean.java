package com.samluys.filtertab.base;

import java.util.List;


public abstract class BaseFilterBean<T extends BaseFilterBean> {


    /**
     * item名称
     */
    public abstract String getItemName();

    /**
     * item对应的ID
     * @return
     */
    public abstract int getId();

    /**
     * 选择状态
     * @return
     */
    public abstract int getSelecteStatus();

    /**
     * 设置选择状态
     * @param status
     */
    public abstract void setSelecteStatus(int status);

    /**
     * 多个来源选择的title 例如：面积，朝向，楼龄，装修等
     * @return
     */
    public abstract String getSortTitle();

    /**
     * 多个来源选择的key 对应sorttitle
     * @return
     */
    public abstract String getSortKey();

    /**
     * 二级分类数据
     * @return
     */
    public abstract List<T> getChildList();
}
