package com.samluys.filtertab.popupwindow;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import com.samluys.filtertab.FilterResultBean;
import com.samluys.filtertab.FilterTabView;
import com.samluys.filtertab.R;
import com.samluys.filtertab.adapter.AreaChildAdapter;
import com.samluys.filtertab.adapter.AreaParentAdapter;
import com.samluys.filtertab.base.BaseFilterBean;
import com.samluys.filtertab.base.BasePopupWindow;
import com.samluys.filtertab.listener.OnAdapterRefreshListener;
import com.samluys.filtertab.listener.OnFilterToViewListener;

import java.util.List;


public class AreaSelectPopupWindow extends BasePopupWindow implements OnAdapterRefreshListener{


    private RecyclerView rv_parent;
    private RecyclerView rv_child;
    private AreaParentAdapter mParentAdapter;
    private AreaChildAdapter mChildAdapter;
    /**
     * 一级数据
     */
    private List<BaseFilterBean> mParentList;
    /**
     * 当前点击的一级分类数据
     */
    private BaseFilterBean mCurrentClickParentBean;
    /**
     * 当前点击的一级Position
     */
    private int mCurrentClickParentPosition;


    public AreaSelectPopupWindow(Context context, List data, int filterType, int position, OnFilterToViewListener onFilterToViewListener, FilterTabView view) {
        super(context, data, filterType, position, onFilterToViewListener);
        view.setOnAdapterRefreshListener(this);
    }

    @Override
    public View initView() {
        View rootView = LayoutInflater.from(getContext()).inflate(R.layout.popup_area_select, null, false);
        rv_parent = rootView.findViewById(R.id.rv_parent);
        mParentList = getData();
        Handler mHandler = new Handler() {
            @Override
            public void dispatchMessage(Message msg) {
                if (msg.what == 1) {
                    int position = (int) msg.obj;
                    if (position != -1) {
                        BaseFilterBean clickParentBean = mParentList.get(position);
                        if (clickParentBean.getChildList() != null && clickParentBean.getChildList().size() > 0) {
                            mChildAdapter.addData(clickParentBean.getChildList());
                        }
                    }
                }
            }
        };

        // 获取默认显示的值
        if (mParentList != null && mParentList.size() > 0) {
            int size = mParentList.size();
            for (int i = 0; i < size; i++) {
                BaseFilterBean bean = mParentList.get(i);
                if (bean.getSelecteStatus() == 1 && bean.getId() != -1) {
                    mCurrentClickParentBean = bean;
                    mCurrentClickParentPosition = i;
                    break;
                }
            }
        }

        mParentAdapter = new AreaParentAdapter(getContext(), mParentList, mHandler);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        rv_parent.setLayoutManager(linearLayoutManager);
        rv_parent.setAdapter(mParentAdapter);

        rv_child = rootView.findViewById(R.id.rv_child);
        mChildAdapter = new AreaChildAdapter(getContext());
        LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(getContext());
        rv_child.setLayoutManager(linearLayoutManager1);
        rv_child.setAdapter(mChildAdapter);


        return rootView;
    }

    @Override
    public void initSelectData() {
        mParentAdapter.setOnItemClickListener(new AreaParentAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                try {
                    mCurrentClickParentBean = mParentList.get(position);
                    mCurrentClickParentPosition = position;
                    List<BaseFilterBean> childList = mCurrentClickParentBean.getChildList();
                    if (childList != null && childList.size() > 0) {
                        mChildAdapter.addData(childList);
                    } else {
                        mChildAdapter.cleanData();
                    }

                    // -1 即为点击“不限”
                    if (mCurrentClickParentBean.getId() == -1) {
                        FilterResultBean resultBean = new FilterResultBean();
                        resultBean.setPopupType(getFilterType());
                        resultBean.setPopupIndex(getPosition());
                        getOnFilterToViewListener().onFilterToView(resultBean);
                        dismiss();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        mChildAdapter.setOnItemClickListener(new AreaChildAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {

                try {
                    if (mCurrentClickParentBean != null) {

                        // 点击之前需要清除其他一级分类下选择的二级分类
                        int size = mParentList.size();
                        for (int i = 0; i < size; i++) {
                            if (i != mCurrentClickParentPosition) {
                                BaseFilterBean bean = mParentList.get(i);
                                List<BaseFilterBean> childList = bean.getChildList();
                                if (childList != null && childList.size() > 0) {
                                    int childSize = childList.size();
                                    for (int j = 0; j < childSize; j++) {
                                        BaseFilterBean childBean = childList.get(j);
                                        childBean.setSelecteStatus(0);
                                    }
                                }
                            }
                        }


                        // 二级数据
                        List<BaseFilterBean> childList = mCurrentClickParentBean.getChildList();
                        BaseFilterBean childBean = childList.get(position);

                        FilterResultBean resultBean = new FilterResultBean();
                        resultBean.setItemId(mCurrentClickParentBean.getId());
                        resultBean.setPopupType(getFilterType());
                        resultBean.setChildId(childBean.getId());
                        resultBean.setPopupIndex(getPosition());
                        if (childBean.getId() == -1) {
                            resultBean.setName(mCurrentClickParentBean.getItemName());
                        } else {
                            resultBean.setName(childBean.getItemName());
                        }
                        getOnFilterToViewListener().onFilterToView(resultBean);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                dismiss();
            }
        });
    }

    @Override
    public void onRefresh(BaseFilterBean selectBean) {
        mCurrentClickParentBean = selectBean;
        mParentAdapter.notifyDataSetChanged();
    }
}
