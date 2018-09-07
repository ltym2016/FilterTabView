package com.samluys.filtertab.listener;

import com.samluys.filtertab.FilterResultBean;

import java.util.List;


public interface OnFilterToViewListener {

    /**
     * 筛选监听
     * @param resultBean
     */
    void onFilterToView(FilterResultBean resultBean);

    /**
     * 筛选集合监听
     * @param resultBean
     */
    void onFilterListToView(List<FilterResultBean> resultBean);

}
