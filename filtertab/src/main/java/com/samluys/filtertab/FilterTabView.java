package com.samluys.filtertab;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseArray;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.samluys.filtertab.base.BaseFilterBean;
import com.samluys.filtertab.base.BasePopupWindow;
import com.samluys.filtertab.listener.OnAdapterRefreshListener;
import com.samluys.filtertab.listener.OnFilterToViewListener;
import com.samluys.filtertab.listener.OnPopupDismissListener;
import com.samluys.filtertab.listener.OnSelectFilterNameListener;
import com.samluys.filtertab.listener.OnSelectResultListener;
import com.samluys.filtertab.util.SpUtils;
import com.samluys.filtertab.util.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class FilterTabView extends LinearLayout implements OnFilterToViewListener {


    private Context mContext;
    /**
     * 记录Tab的位置
     */
    private int mTabPostion = -1;
    /**
     * 当前点击的下标
     */
    private int currentIndex = -1;
    /**
     * popwindow缓存集合
     */
    private ArrayList<BasePopupWindow> mPopupWs = new ArrayList<>();
    private ArrayList<TextView> mTextViewLists = new ArrayList<>();
    private List<List<BaseFilterBean>> mDataList = new ArrayList<>();
    /**
     * 初期进来Tab名称
     */
    private ArrayList<String> mTextContents = new ArrayList<>();
    private ArrayList<View> mView = new ArrayList<>();
    private IPopupLoader mPopupLoader;

    /**
     * 箭头图片
     */
    private int tab_arrow_select;
    private int tab_arrow_unselect;
    /**
     * 主色
     */
    private int colorMain;
    /**
     * 筛选Tab字体的样式：0为normal 1为bold
     */
    private int tab_text_style;
    /**
     * 多选情况下选中和非选中的样式
     */
    private int btnStrokeSelect;
    private int btnStrokeUnselect;
    private int btnSolidSelect;
    private int btnSolidUnselect;
    private float btnCornerRadius;
    private int btnTextSelect;
    private int btnTextUnSelect;
    /**
     * 多选item的列数
     */
    private int columnNum;
    /**
     * FilterTabView和activity的回调
     */
    private OnSelectResultListener onSelectResultListener;
    /**
     * popupwindow dismiss 监听
     */
    private OnPopupDismissListener onPopupDismissListener;
    /**
     * 将选择的结果暴露出去
     */
    private OnSelectFilterNameListener onSelectFilterNameListener;

    /**
     * adapter 刷新回调
     */
    private OnAdapterRefreshListener onAdapterRefreshListener;

    private SparseArray mHasSelected;

    public FilterTabView(Context context) {
        super(context);
        mContext = context;
        init(context, null);
    }

    public FilterTabView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init(context, attrs);
    }

    public FilterTabView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {

        TypedArray a = null;

        try {
            a = context.obtainStyledAttributes(attrs, R.styleable.FilterTabView);
            tab_arrow_select = a.getResourceId(R.styleable.FilterTabView_tab_arrow_select_color, R.drawable.icon_slat_up);
            tab_arrow_unselect = a.getResourceId(R.styleable.FilterTabView_tab_arrow_unselect_color, R.drawable.icon_slat_down);
            tab_text_style = a.getInteger(R.styleable.FilterTabView_tab_text_style, 0);
            colorMain = a.getColor(R.styleable.FilterTabView_color_main, mContext.getResources().getColor(R.color.color_main));
            btnStrokeSelect = a.getColor(R.styleable.FilterTabView_btn_stroke_select_color, mContext.getResources().getColor(R.color.color_main));
            btnStrokeUnselect = a.getColor(R.styleable.FilterTabView_btn_stroke_unselect_color, mContext.getResources().getColor(R.color.color_dfdfdf));
            btnSolidSelect = a.getColor(R.styleable.FilterTabView_btn_solid_select_color, 0);
            btnSolidUnselect = a.getColor(R.styleable.FilterTabView_btn_solid_unselect_color, 0);
            btnCornerRadius = a.getDimension(R.styleable.FilterTabView_btn_corner_radius, mContext.getResources().getDimensionPixelSize(R.dimen.btn_corner_radius));
            btnTextSelect = a.getColor(R.styleable.FilterTabView_btn_text_select_color, mContext.getResources().getColor(R.color.color_main));
            btnTextUnSelect = a.getColor(R.styleable.FilterTabView_btn_text_unselect_color, mContext.getResources().getColor(R.color.color_666666));
            columnNum = a.getInteger(R.styleable.FilterTabView_column_num, 3);
            // 选中字体为粗体
            SpUtils.getInstance(context).putTextStyle(tab_text_style);
            // 主题色
            SpUtils.getInstance(context).putColorMain(colorMain);
            // button边框的颜色
            SpUtils.getInstance(context).putStrokeSelectColor(btnStrokeSelect);
            SpUtils.getInstance(context).putStrokeUnSelectColor(btnStrokeUnselect);
            // button填充的颜色
            SpUtils.getInstance(context).putSolidSelectColor(btnSolidSelect);
            SpUtils.getInstance(context).putSolidUnSelectColor(btnSolidUnselect);
            // button圆角的大小
            SpUtils.getInstance(context).putCornerRadius(btnCornerRadius);
            // button 字体颜色
            SpUtils.getInstance(context).putTextSelect(btnTextSelect);
            SpUtils.getInstance(context).putTextUnSelect(btnTextUnSelect);

            SpUtils.getInstance(context).putColumnNum(columnNum);
            mHasSelected = new SparseArray();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (a != null) {
                a.recycle();
            }
        }
    }

    /**
     * @param tabName
     * @param data
     * @param filterType
     * @return
     */
    public FilterTabView addFilterItem(String tabName, List<BaseFilterBean> data, final int filterType, int popupIndex) {
        View tabView = inflate(getContext(), R.layout.item_tab_filter, null);
        final TextView tv_tab_name = tabView.findViewById(R.id.tv_tab_name);
        LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 1.0f);
        tabView.setLayoutParams(params);

        setArrowDirection(tv_tab_name, false);

        if (mPopupLoader == null) {
            mPopupLoader = new PopupEntityLoaderImp();
        }

        BasePopupWindow basePopupWindow =
                (BasePopupWindow) mPopupLoader.getPopupEntity(mContext, data, filterType, popupIndex, this, this);
        mPopupWs.add(basePopupWindow);

        basePopupWindow.setTouchInterceptor(new OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                calculatePosition(motionEvent);

                return false;
            }
        });

        basePopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                setArrowDirection(tv_tab_name, false);
                if (onPopupDismissListener != null) {
                    onPopupDismissListener.onDismiss();
                }
            }
        });

        // 将筛选项布局加入view
        addView(tabView);
        //对筛选项控件进行设置
        String tabSelectName = setFilterTabName(filterType, data, tabName);
        if (TextUtils.isEmpty(tabSelectName)) {
            tv_tab_name.setText(tabName);
        } else {
            tv_tab_name.setText(tabSelectName);
        }
        tabView.setTag(++mTabPostion);
        tabView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                //当点击时,设置当前选中状态
                currentIndex = (int) view.getTag();

                //弹出当前页pop,或者回收pop
                showPopView(currentIndex, filterType);
            }
        });

        //进行缓存
        mTextViewLists.add(tv_tab_name);
        mView.add(tabView);
        mTextContents.add(tabName);
        mDataList.add(data);
        return this;
    }

    public void setOnPopupDismissListener(OnPopupDismissListener onPopupDismissListener) {
        this.onPopupDismissListener = onPopupDismissListener;
    }

    private String setFilterTabName(int filterType, List data, String tabFixName) {
        String tabName = "";
        List<BaseFilterBean> list = (List<BaseFilterBean>) data;
        if (list != null && list.size() > 0) {
            int size = list.size();
            // 单行只有一个层级的点击
            if (filterType == FilterTabConfig.FILTER_TYPE_SINGLE_SELECT
                    || filterType == FilterTabConfig.FILTER_TYPE_PRICE) {

                for (int i = 0; i < size; i++) {
                    BaseFilterBean bean = list.get(i);
                    if (bean.getSelecteStatus() == 1 && bean.getId() != -1) {
                        tabName = bean.getItemName();
                        break;
                    }
                }
                // Gird型多选/单选
            } else if (filterType == FilterTabConfig.FILTER_TYPE_SINGLE_GIRD) {
                List<BaseFilterBean> selectList = new ArrayList<>();
                for (int i = 0; i < size; i++) {
                    BaseFilterBean resultBean = list.get(i);
                    if (resultBean.getSelecteStatus() == 1 && resultBean.getId() != -1) {
                        selectList.add(resultBean);
                    }
                }


                for (int i = 0; i < selectList.size(); i++) {
                    BaseFilterBean bean = selectList.get(i);
                    if (i == selectList.size() - 1) {
                        tabName = tabName + bean.getItemName();
                    } else {
                        tabName = tabName + bean.getItemName() + ",";
                    }
                }

                // 区域选择
            } else if (filterType == FilterTabConfig.FILTER_TYPE_AREA) {
                // 先看二级分类有没有选择的
                for (int i = 0; i < size; i++) {
                    BaseFilterBean parentBean = list.get(i);
                    List<BaseFilterBean> childList = parentBean.getChildList();
                    if (childList != null && childList.size() > 0) {
                        for (int j = 0; j < childList.size(); j++) {
                            BaseFilterBean childBean = childList.get(j);
                            if (childBean.getSelecteStatus() == 1 && childBean.getId() != -1) {
                                tabName = childBean.getItemName();
                                break;
                            }
                        }
                    }
                }


                // 再看一级分类有没有选择的
                if (TextUtils.isEmpty(tabName)) {
                    for (int i = 0; i < size; i++) {
                        BaseFilterBean parentBean = list.get(i);
                        if (parentBean.getSelecteStatus() == 1 && (parentBean.getId() != -1 && parentBean.getId() != 0)) {
                            tabName = parentBean.getItemName();
                            break;
                        }
                    }
                }

                // 多分类选择
            } else if (filterType == FilterTabConfig.FILTER_TYPE_MUL_SELECT) {
                int count = 0;
                for (int i = 0; i < size; i++) {
                    BaseFilterBean parentBean = list.get(i);
                    List<BaseFilterBean> childList = parentBean.getChildList();
                    if (childList != null && childList.size() > 0) {
                        for (int j = 0; j < childList.size(); j++) {
                            BaseFilterBean childBean = childList.get(j);
                            if (childBean.getSelecteStatus() == 1 && childBean.getId() != -1) {
                                count++;
                                break;
                            }
                        }
                    }
                }

                if (count > 0) {
                    tabName = tabFixName + "(" + count + ")";
                }
            }
        }

        return tabName;
    }

    /**
     * 计算tabView的坐标位置，确定点击是在其范围内
     *
     * @param event
     */
    private void calculatePosition(MotionEvent event) {

        try {
            int touchX = (int) event.getRawX();
            int touchY = (int) event.getRawY();
            // 获取纵坐标的位置信息
            if (mView.size() > 0) {
                for (int i = 0; i < mView.size(); i++) {
                    View tabView = mView.get(i);
                    int[] localPosition = new int[2];
                    tabView.getLocationOnScreen(localPosition);
                    int y = localPosition[1] + Utils.dp2px(mContext, 50);
                    int x = localPosition[0] + Utils.getScreenWidth(mContext) / mView.size();

                    boolean scopeX = touchX > localPosition[0] && touchX < x;
                    boolean scopeY = touchY > localPosition[1] && touchY < y;
                    if (scopeX && scopeY) {
                        mView.get(i).performClick();
                        break;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setClickFilter(int tabPosition) {
        mView.get(tabPosition).performClick();
    }

    /**
     * 设置箭头方向
     */
    private void setArrowDirection(TextView tv_tab, boolean isUp) {
        TextPaint textPaint = tv_tab.getPaint();
        if (isUp) {
            if (tab_text_style == 1) {
                textPaint.setFakeBoldText(true);
            }

            tv_tab.setTextColor(btnTextSelect);
            tv_tab.setCompoundDrawablesWithIntrinsicBounds(0, 0, tab_arrow_select, 0);

        } else {
            textPaint.setFakeBoldText(false);
            tv_tab.setTextColor(btnTextUnSelect);
            tv_tab.setCompoundDrawablesWithIntrinsicBounds(0, 0, tab_arrow_unselect, 0);
        }
    }

    private void showPopView(int currentIndex, int filterType) {
        if (mPopupWs.size() > currentIndex && mPopupWs.get(currentIndex) != null) {

            // 重置选择的值，只会显示点击确定后的值
            try {
                resetSelectData(currentIndex, filterType);
            } catch (Exception e) {
                e.printStackTrace();
            }


            //遍历, 将 不是该位置的window消失
            for (int i = 0; i < mPopupWs.size(); i++) {
                if (i != currentIndex) {
                    mPopupWs.get(i).dismiss();
                    setArrowDirection(mTextViewLists.get(i), false);
                } else {
                    setArrowDirection(mTextViewLists.get(i), true);
                }
            }
            //如果该位置正在展示,就让他消失.如果没有,就展示
            if (mPopupWs.get(currentIndex).isShowing()) {
                mPopupWs.get(currentIndex).dismiss();
            } else {

                mPopupWs.get(currentIndex).show(this);
            }
        }
    }

    private void resetSelectData(int currentIndex, int filterType) {
        List<BaseFilterBean> datas = mDataList.get(currentIndex);

        if (FilterTabConfig.FILTER_TYPE_MUL_SELECT == filterType) {
            FilterResultBean bean = (FilterResultBean) mHasSelected.get(currentIndex);
            if (bean != null) {
                List<FilterResultBean.MulTypeBean> hasSelectList = bean.getSelectList();

                HashMap<String, List<Integer>> map = new HashMap<>();

                for (int i = 0; i < hasSelectList.size(); i++) {
                    FilterResultBean.MulTypeBean hasSelectBean = hasSelectList.get(i);
                    String key = hasSelectBean.getTypeKey();
                    if (map.get(key) == null) {
                        List<Integer> ids = new ArrayList<>();
                        ids.add(hasSelectBean.getItemId());
                        map.put(key, ids);
                    } else {
                        map.get(key).add(hasSelectBean.getItemId());
                    }
                }

                List<String> keyList = new ArrayList<>();
                for (String key : map.keySet()) {
                    keyList.add(key);
                }

                for (String key : map.keySet()) {
                    for (int i = 0; i < datas.size(); i++) {
                        BaseFilterBean data = datas.get(i);

                        if (keyList.contains(data.getSortKey())) {
                            if (key.equals(data.getSortKey())) {
                                List<Integer> ids = map.get(key);

                                List<BaseFilterBean> childList = data.getChildList();
                                for (int j = 0; j < childList.size(); j++) {
                                    BaseFilterBean baseFilterBean = childList.get(j);

                                    if (!ids.contains(baseFilterBean.getId())) {
                                        baseFilterBean.setSelecteStatus(0);
                                    } else {
                                        baseFilterBean.setSelecteStatus(1);
                                    }
                                }
                            }
                        } else {
                            List<BaseFilterBean> childList = data.getChildList();
                            for (int j = 0; j < childList.size(); j++) {
                                BaseFilterBean baseFilterBean = childList.get(j);
                                baseFilterBean.setSelecteStatus(0);
                            }
                        }
                    }
                }
            }
        } else if (FilterTabConfig.FILTER_TYPE_SINGLE_GIRD == filterType) {
            List<FilterResultBean> list = (List<FilterResultBean>) mHasSelected.get(currentIndex);

            if (list != null && list.size() > 0) {
                List<Integer> ids = new ArrayList<>();
                for (int i = 0; i < list.size(); i++) {
                    ids.add(list.get(i).getItemId());
                }

                for (int i = 0; i < datas.size(); i++) {
                    BaseFilterBean bean = datas.get(i);
                    if (!ids.contains(bean.getId())) {
                        bean.setSelecteStatus(0);
                    } else {
                        bean.setSelecteStatus(1);
                    }
                }
            }
        }
    }


    public void setOnSelectResultListener(OnSelectResultListener onSelectResultListener) {
        this.onSelectResultListener = onSelectResultListener;
    }

    public void setOnSelectFilterNameListener(OnSelectFilterNameListener onSelectFilterNameListener) {
        this.onSelectFilterNameListener = onSelectFilterNameListener;
    }

    public void setOnAdapterRefreshListener(OnAdapterRefreshListener onAdapterRefreshListener) {
        this.onAdapterRefreshListener = onAdapterRefreshListener;
    }

    @Override
    public void onFilterToView(FilterResultBean resultBean) {
        int popupIndex = resultBean.getPopupIndex();

        // 单行只有一个层级的点击
        if (resultBean.getPopupType() == FilterTabConfig.FILTER_TYPE_SINGLE_SELECT
                || resultBean.getPopupType() == FilterTabConfig.FILTER_TYPE_PRICE
                || resultBean.getPopupType() == FilterTabConfig.FILTER_TYPE_SINGLE_GIRD) {

            int itemId = resultBean.getItemId();
            String itemName = resultBean.getName();
            if (itemId != -1) {
                // itemId = -2 即为 手动输入价格范围
                if (itemId == -2) {
                    String name = itemName + mContext.getResources().getString(R.string.wan);
                    mTextViewLists.get(popupIndex).setText(name);
                } else {
                    mTextViewLists.get(popupIndex).setText(itemName);
                }
            } else {
                // itemId = -1 即为点击“不限” FilterTabView名称不变
                mTextViewLists.get(popupIndex).setText(mTextContents.get(popupIndex));
            }
            onSelectResultListener.onSelectResult(resultBean);

            // 区域选择
        } else if (resultBean.getPopupType() == FilterTabConfig.FILTER_TYPE_AREA) {
            // itemid = -1 即为点击“不限” FilterTabView名称不变
            if (resultBean.getItemId() == -1) {
                mTextViewLists.get(popupIndex).setText(mTextContents.get(popupIndex));
            } else {
                mTextViewLists.get(popupIndex).setText(resultBean.getName());
            }

            onSelectResultListener.onSelectResult(resultBean);

            // 多分类选择
        } else if (resultBean.getPopupType() == FilterTabConfig.FILTER_TYPE_MUL_SELECT) {
            // 选择的集合
            List<FilterResultBean.MulTypeBean> selectList = resultBean.getSelectList();
            if (selectList.size() == 0) {
                mTextViewLists.get(popupIndex).setText(mTextContents.get(popupIndex));
            } else {
                mTextViewLists.get(popupIndex).setText(mTextContents.get(popupIndex) + "(" + selectList.size() + ")");
            }
            mHasSelected.put(popupIndex, resultBean);
            onSelectResultListener.onSelectResult(resultBean);
        }

        // 将Tab name 暴露出去
        if (onSelectFilterNameListener != null) {
            onSelectFilterNameListener.onSelectFilterName(mTextViewLists.get(popupIndex).getText().toString(), popupIndex);
        }
    }

    @Override
    public void onFilterListToView(List<FilterResultBean> resultBeans) {

        int popupIndex = resultBeans.get(0).getPopupIndex();
        String itemName = "";
        if (resultBeans.size() == 1 && resultBeans.get(0).getItemId() == -1) {
            // 不限
            // itemId = -1 即为点击“不限” FilterTabView名称不变
            mTextViewLists.get(popupIndex).setText(mTextContents.get(popupIndex));
        } else {
            for (int i = 0; i < resultBeans.size(); i++) {
                FilterResultBean resultBean = resultBeans.get(i);
                if (i == resultBeans.size() - 1) {
                    itemName = itemName + resultBean.getName();
                } else {
                    itemName = itemName + resultBean.getName() + ",";
                }
            }
            mTextViewLists.get(popupIndex).setText(itemName);
        }
        mHasSelected.put(popupIndex, resultBeans);
        onSelectResultListener.onSelectResultList(resultBeans);
    }

    /**
     * 通过外部设置Tab的名称
     *
     * @param popupIndex
     * @param firstId
     * @param name
     * @param secondId
     */
    public void setTabName(int popupIndex, int firstId, String name, int secondId) {
        // 设置区域的名称并且保证显示的时候，对应的名称被选中
        mTextViewLists.get(popupIndex).setText(name);
        List<BaseFilterBean> list = mDataList.get(popupIndex);
        if (list != null && list.size() > 0) {
            BaseFilterBean selectBean = null;
            for (int i = 0; i < list.size(); i++) {
                BaseFilterBean bean = list.get(i);
                if (bean.getId() == firstId) {
                    bean.setSelecteStatus(1);
                    selectBean = bean;
                } else {
                    bean.setSelecteStatus(0);
                }
            }

            if (selectBean != null && secondId != 0) {
                List<BaseFilterBean> childList = selectBean.getChildList();
                if (childList != null && childList.size() > 0) {
                    for (int j = 0; j < childList.size(); j++) {
                        BaseFilterBean childBean = childList.get(j);
                        if (childBean.getId() == secondId) {
                            childBean.setSelecteStatus(1);
                        } else {
                            childBean.setSelecteStatus(0);
                        }
                    }
                }
            }

            onAdapterRefreshListener.onRefresh(selectBean);
        }

    }


    /**
     * 多次加载 清空数据
     */
    public void removeViews() {
        mTextViewLists.clear();
        mTextContents.clear();
        mPopupWs.clear();
        mView.clear();
        mTabPostion = -1;
        currentIndex = -1;
        mDataList.clear();
        mHasSelected.clear();
        removeAllViews();
    }
}
