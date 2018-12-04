package com.samluys.filtertab.popupwindow;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.samluys.filtertab.FilterResultBean;
import com.samluys.filtertab.R;
import com.samluys.filtertab.adapter.ItemSelectAdapter;
import com.samluys.filtertab.adapter.PopupSingleAdapter;
import com.samluys.filtertab.base.BaseFilterBean;
import com.samluys.filtertab.base.BasePopupWindow;
import com.samluys.filtertab.listener.OnFilterToViewListener;
import com.samluys.filtertab.util.SpUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 单个Gird样式的多选
 */
public class GridSelectPopupWindow extends BasePopupWindow {

    private RecyclerView rv_content;
    private ItemSelectAdapter mAdapter;
    private TextView tv_bottom;
    private List<FilterResultBean> mSelectList;

    public GridSelectPopupWindow(Context context, List<BaseFilterBean> data, int filterType, int position, OnFilterToViewListener onFilterToViewListener) {
        super(context, data, filterType,position,onFilterToViewListener);
    }

    @Override
    public View initView() {
        View rootView = LayoutInflater.from(getContext()).inflate(R.layout.popup_grid_select, null,false);
        rv_content = rootView.findViewById(R.id.rv_content);
        tv_bottom = rootView.findViewById(R.id.tv_bottom);
        boolean isCanMulSelect = getData().get(0).isCanMulSelect();
        mAdapter = new ItemSelectAdapter(getContext(), getData(),true);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(),3);
        rv_content.setLayoutManager(gridLayoutManager);
        rv_content.setAdapter(mAdapter);
        mSelectList = new ArrayList<>();

        tv_bottom.setBackgroundColor(SpUtils.getInstance(mContext).getColorMain());

        View v_outside = rootView.findViewById(R.id.v_outside);
        v_outside.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isShowing()) {
                    dismiss();
                }
            }
        });
        return rootView;
    }

    @Override
    public void initSelectData() {
        tv_bottom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    mSelectList.clear();
                    List<BaseFilterBean> list = getData();
                    if (list != null && list.size() > 0) {
                        for (int i = 0; i < list.size(); i++) {
                            BaseFilterBean bean = list.get(i);
                            if (bean.getSelecteStatus() == 1) {
                                int itemId = bean.getId();
                                String itemName = bean.getItemName();
                                FilterResultBean resultBean = new FilterResultBean();
                                resultBean.setPopupIndex(getPosition());
                                resultBean.setPopupType(getFilterType());
                                resultBean.setItemId(itemId);
                                resultBean.setName(itemName);

                                mSelectList.add(resultBean);
                            }
                        }
                    }

                    getOnFilterToViewListener().onFilterListToView(mSelectList);
                    dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void refreshData() {
        if (mAdapter != null) {
            mAdapter.notifyDataSetChanged();
        }
    }
}
