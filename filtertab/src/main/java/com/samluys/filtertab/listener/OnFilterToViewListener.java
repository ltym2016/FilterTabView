package com.samluys.filtertab.listener;

import com.samluys.filtertab.FilterResultBean;


public interface OnFilterToViewListener {

    /**
     * 筛选监听
     * @param resultBean
     */
    void onFilterToView(FilterResultBean resultBean);

}
