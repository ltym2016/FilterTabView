package com.samluys.filtertab;

import com.samluys.filtertab.base.BaseFilterBean;

import java.util.List;


public class FilterInfoBean {

    /**
     * tab名称
     */
    private String tabName;

    /**
     * 对应的Popupwindow类型
     */
    private int popupType;

    /**
     * 对应的Popupwindow数据
     */
    private List<BaseFilterBean> filterData;


    public FilterInfoBean(String tabName, int popupType, List filterData) {
        this.tabName = tabName;
        this.popupType = popupType;
        this.filterData = filterData;
    }

    public String getTabName() {
        return tabName;
    }

    public void setTabName(String tabName) {
        this.tabName = tabName;
    }

    public int getPopupType() {
        return popupType;
    }

    public void setPopupType(int popupType) {
        this.popupType = popupType;
    }

    public List getFilterData() {
        return filterData;
    }

    public void setFilterData(List filterData) {
        this.filterData = filterData;
    }
}
