package com.samluys.filtertab.popupwindow;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.samluys.filtertab.FilterResultBean;
import com.samluys.filtertab.util.KeyboardUtils;
import com.samluys.filtertab.R;
import com.samluys.filtertab.util.SpUtils;
import com.samluys.filtertab.util.Utils;
import com.samluys.filtertab.adapter.PopupSingleAdapter;
import com.samluys.filtertab.base.BaseFilterBean;
import com.samluys.filtertab.base.BasePopupWindow;
import com.samluys.filtertab.listener.OnFilterToViewListener;

import java.util.List;

/**
 * 竖直可以输入价格的单选样式
 */
public class PriceSelectPopupWindow extends BasePopupWindow {

    private RecyclerView rv_content;
    private Button btn_price_confirm;
    private EditText et_min_price;
    private EditText et_max_price;
    private int mHeight;
    private View rootView;
    private View mAnchor;
    private ConstraintLayout ll_input;
    /**
     * toolbar高度
     */
    int toolbarHeight;
    /**
     * 状态栏的高度
     */
    int statusBarHeight;
    /**
     * RecyclerView 高度
     */
    int recyclerHeight;

    /**
     *  手机屏幕高度
     */
    int mScreenHeight;

    public PriceSelectPopupWindow(Context context, List data, int filterType,int position,
                                  OnFilterToViewListener onFilterToViewListener) {
        super(context, data, filterType, position,onFilterToViewListener);
    }

    @Override
    public View initView() {
        rootView = LayoutInflater.from(getContext()).inflate(R.layout.popup_price_select, null, false);
        rv_content = rootView.findViewById(R.id.rv_content);
        btn_price_confirm = rootView.findViewById(R.id.btn_price_confirm);
        et_min_price = rootView.findViewById(R.id.et_min_price);
        et_max_price = rootView.findViewById(R.id.et_max_price);
        ll_input = rootView.findViewById(R.id.bottom);
        // toolbar高度
        toolbarHeight = mContext.getResources().getDimensionPixelSize(R.dimen.tool_bar);
        // 状态栏的高度
        statusBarHeight = Utils.getStatusBarHeight(mContext);
        // RecyclerView 高度
        recyclerHeight = mContext.getResources().getDimensionPixelSize(R.dimen.rv_height);
        // 手机高度
        mScreenHeight = Utils.getScreenHeight(mContext);
        View v_outside = rootView.findViewById(R.id.v_outside);
        v_outside.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isShowing()) {
                    dismiss();
                }
            }
        });
        return rootView;
    }

    @Override
    public void initSelectData() {
        final PopupSingleAdapter adapter = new PopupSingleAdapter(getContext(), getData());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        rv_content.setLayoutManager(linearLayoutManager);
        rv_content.setAdapter(adapter);

        adapter.setOnItemClickListener(new PopupSingleAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                try {
                    int itemId = getData().get(position).getId();
                    String itemName = getData().get(position).getItemName();
                    FilterResultBean resultBean = new FilterResultBean();
                    resultBean.setPopupType(getFilterType());
                    resultBean.setPopupIndex(getPosition());
                    resultBean.setItemId(itemId);
                    resultBean.setName(itemName);
                    getOnFilterToViewListener().onFilterToView(resultBean);

                    int filterHeight = mAnchor.getHeight();
                    RelativeLayout.LayoutParams layoutParams
                            = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    layoutParams.topMargin = recyclerHeight;
                    ll_input.setLayoutParams(layoutParams);
                    int h = mScreenHeight - filterHeight - toolbarHeight - statusBarHeight;
                    update(mAnchor, WindowManager.LayoutParams.MATCH_PARENT, h);

                    et_min_price.setText("");
                    et_max_price.setText("");
                    dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        GradientDrawable myGrad = (GradientDrawable) btn_price_confirm.getBackground();
        myGrad.setColor(SpUtils.getInstance(mContext).getColorMain());

        // 确定按钮
        btn_price_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    // 最小金额
                    String minPrice = et_min_price.getText().toString().trim();
                    // 最大金额
                    String maxPrice = et_max_price.getText().toString().trim();
                    String message = null;

                    if (TextUtils.isEmpty(minPrice) && TextUtils.isEmpty(maxPrice)) {
                        message = mContext.getResources().getString(R.string.all_empty);
                        Toast.makeText(mContext, message, Toast.LENGTH_LONG).show();
                        return;
                    }

                    if (TextUtils.isEmpty(minPrice)) {
                        message = mContext.getResources().getString(R.string.min_empty);
                        Toast.makeText(mContext, message, Toast.LENGTH_LONG).show();
                        return;
                    }

                    if (TextUtils.isEmpty(maxPrice)) {
                        message = mContext.getResources().getString(R.string.max_empty);
                        Toast.makeText(mContext, message, Toast.LENGTH_LONG).show();
                        return;
                    }

                    int min = TextUtils.isEmpty(minPrice) ? 0 : Integer.valueOf(minPrice);
                    int max = TextUtils.isEmpty(maxPrice) ? 0 : Integer.valueOf(maxPrice);

                    if (min > max) {
                        message = mContext.getResources().getString(R.string.min_max);
                        Toast.makeText(mContext, message, Toast.LENGTH_LONG).show();
                        return;
                    }

                    String name = minPrice + "-" + maxPrice;

                    FilterResultBean resultBean = new FilterResultBean();
                    resultBean.setPopupType(getFilterType());
                    resultBean.setPopupIndex(getPosition());
                    resultBean.setItemId(-2);
                    resultBean.setName(name);
                    // -2 是用来区分手动输入价格
                    getOnFilterToViewListener().onFilterToView(resultBean);

                    // 重置list信息
                    List<BaseFilterBean> list = getData();
                    for (int i = 0; i < list.size(); i++) {
                        BaseFilterBean bean = list.get(i);
                        if (i == 0) {
                            bean.setSelecteStatus(1);
                        } else {
                            bean.setSelecteStatus(0);
                        }
                    }
                    adapter.notifyDataSetChanged();

                    int filterHeight = mAnchor.getHeight();


                    RelativeLayout.LayoutParams layoutParams
                            = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    layoutParams.topMargin = recyclerHeight;
                    ll_input.setLayoutParams(layoutParams);
                    int h = mScreenHeight - filterHeight - toolbarHeight - statusBarHeight;
                    update(mAnchor, WindowManager.LayoutParams.MATCH_PARENT, h);
                    dismiss();
                }catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void show(final View anchor) {
        super.show(anchor);
        mAnchor = anchor;


        KeyboardUtils.registerSoftInputChangedListener(mActivity, new KeyboardUtils.OnSoftInputChangedListener() {
            @Override
            public void onSoftInputChanged(int height) {
                int filterHeight = anchor.getHeight();
                // 输入栏的高度
                int inputHeight = ll_input.getHeight();

                if (height > 0) {
                    RelativeLayout.LayoutParams layoutParams
                            = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    int topMargin = mScreenHeight - height - inputHeight - statusBarHeight - filterHeight - toolbarHeight;
                    layoutParams.topMargin = topMargin;
                    ll_input.setLayoutParams(layoutParams);
                    // FilterTabView高度
                    int popHeight = topMargin + inputHeight;
                    update(anchor, WindowManager.LayoutParams.MATCH_PARENT, popHeight);
                } else {
                    RelativeLayout.LayoutParams layoutParams
                            = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    layoutParams.topMargin = recyclerHeight;
                    ll_input.setLayoutParams(layoutParams);
                    int h = mScreenHeight - filterHeight - toolbarHeight - statusBarHeight;
                    update(anchor, WindowManager.LayoutParams.MATCH_PARENT, h);
                }
            }
        });
    }

    @Override
    public void refreshData() {

    }
}
