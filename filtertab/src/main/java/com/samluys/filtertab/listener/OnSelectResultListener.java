package com.samluys.filtertab.listener;

import com.samluys.filtertab.FilterResultBean;

import java.util.List;


public interface OnSelectResultListener {

    void onSelectResult(FilterResultBean resultBean);

    void onSelectResultList(List<FilterResultBean> resultBeans);
}
