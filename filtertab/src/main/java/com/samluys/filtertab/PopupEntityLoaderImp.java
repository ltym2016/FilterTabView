package com.samluys.filtertab;

import android.content.Context;
import android.widget.PopupWindow;

import com.samluys.filtertab.listener.OnFilterToViewListener;
import com.samluys.filtertab.popupwindow.AreaSelectPopupWindow;
import com.samluys.filtertab.popupwindow.GridSelectPopupWindow;
import com.samluys.filtertab.popupwindow.MulSelectPopupwindow;
import com.samluys.filtertab.popupwindow.PriceSelectPopupWindow;
import com.samluys.filtertab.popupwindow.SingleSelectPopupWindow;

import java.util.List;


public class PopupEntityLoaderImp implements IPopupLoader {


    @Override
    public PopupWindow getPopupEntity(Context context, List data, int filterType, int position, OnFilterToViewListener onFilterToViewListener, FilterTabView view) {

        PopupWindow popupWindow = null;

        switch (filterType) {
            case FilterTabConfig.FILTER_TYPE_AREA:
                popupWindow = new AreaSelectPopupWindow(context,data,filterType,position, onFilterToViewListener, view);
                break;
            case FilterTabConfig.FILTER_TYPE_PRICE:
                popupWindow = new PriceSelectPopupWindow(context, data, filterType,position, onFilterToViewListener);
                break;
            case FilterTabConfig.FILTER_TYPE_SINGLE_SELECT:
                popupWindow = new SingleSelectPopupWindow(context,data,filterType,position, onFilterToViewListener);
                break;
            case FilterTabConfig.FILTER_TYPE_MUL_SELECT:
                popupWindow = new MulSelectPopupwindow(context,data,filterType,position, onFilterToViewListener);
                break;
            case FilterTabConfig.FILTER_TYPE_SINGLE_GIRD:
                popupWindow = new GridSelectPopupWindow(context,data,filterType,position, onFilterToViewListener);
                break;
            default:
                break;
        }

        return popupWindow;
    }
}
