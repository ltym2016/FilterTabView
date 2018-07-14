package com.samluys.filtertab.popupwindow;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import com.samluys.filtertab.FilterResultBean;
import com.samluys.filtertab.R;
import com.samluys.filtertab.adapter.PopupMulAdapter;
import com.samluys.filtertab.base.BaseFilterBean;
import com.samluys.filtertab.base.BasePopupWindow;
import com.samluys.filtertab.listener.OnFilterToViewListener;

import java.util.ArrayList;
import java.util.List;


public class MulSelectPopupwindow extends BasePopupWindow {

    private RecyclerView rv_content;
//    private View v_outside;
    private Button btn_reset;
    private Button btn_confirm;
    private PopupMulAdapter mAdapter;
    private List<FilterResultBean.MulTypeBean> mSelectList;


    public MulSelectPopupwindow(Context context, List data, int filterType, int position, OnFilterToViewListener onFilterToViewListener) {
        super(context, data, filterType, position, onFilterToViewListener);
    }

    @Override
    public View initView() {
        View rootView = LayoutInflater.from(getContext()).inflate(R.layout.popup_mul_select, null, false);
        rv_content = rootView.findViewById(R.id.rv_content);
//        v_outside = rootView.findViewById(R.id.v_outside);
        btn_reset = rootView.findViewById(R.id.btn_reset);
        btn_confirm = rootView.findViewById(R.id.btn_confirm);
        mAdapter = new PopupMulAdapter(getContext(), getData());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        rv_content.setLayoutManager(linearLayoutManager);
        rv_content.setAdapter(mAdapter);

        mSelectList = new ArrayList<>();
        return rootView;
    }

    @Override
    public void initSelectData() {
//        v_outside.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                dismiss();
//            }
//        });

        // 重置
        btn_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    mSelectList.clear();
                    List<BaseFilterBean> list = getData();
                    int size = list.size();
                    for (int i = 0; i < size; i++) {
                        BaseFilterBean parentBean = list.get(i);
                        List<BaseFilterBean> childList = parentBean.getChildList();
                        if (childList != null && childList.size() > 0) {
                            int childSize = childList.size();
                            for (int j = 0; j < childSize; j++) {
                                BaseFilterBean childBean = childList.get(j);
                                if (childBean.getSelecteStatus() == 1) {
                                    childBean.setSelecteStatus(0);
                                }
                            }
                        }
                    }

                    mAdapter.notifyDataSetChanged();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        // 确定
        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    FilterResultBean resultBean = new FilterResultBean();
                    resultBean.setPopupIndex(getPosition());
                    resultBean.setPopupType(getFilterType());

                    mSelectList.clear();
                    List<BaseFilterBean> list = getData();
                    int size = list.size();
                    for (int i = 0; i < size; i++) {
                        BaseFilterBean parentBean = list.get(i);
                        List<BaseFilterBean> childList = parentBean.getChildList();
                        if (childList != null && childList.size() > 0) {
                            int childSize = childList.size();
                            for (int j = 0; j < childSize; j++) {
                                BaseFilterBean childBean = childList.get(j);
                                if (childBean.getSelecteStatus() == 1 && childBean.getId() != -1) {
                                    FilterResultBean.MulTypeBean bean = new FilterResultBean.MulTypeBean();
                                    bean.setItemId(childBean.getId());
                                    bean.setTypeKey(parentBean.getSortKey());
                                    bean.setItemName(childBean.getItemName());

                                    mSelectList.add(bean);
                                }
                            }
                        }
                    }
                    resultBean.setSelectList(mSelectList);
                    getOnFilterToViewListener().onFilterToView(resultBean);
                    dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
