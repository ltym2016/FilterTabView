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
    public String getSortTitle() {
        return null;
    }

    /**
     * 多个来源选择的key 对应sorttitle
     * @return
     */
    public String getSortKey(){
        return null;
    }

    /**
     * 二级分类数据
     * @return
     */
    public List<T> getChildList() {
       return null;
    }

    /**
     * 对于Grid样式的是否支持多选
     * @return
     */
    public boolean isCanMulSelect() {
        return false;
    }
}
