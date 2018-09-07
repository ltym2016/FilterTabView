package com.samluys.filtertab.adapter;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextPaint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.samluys.filtertab.R;
import com.samluys.filtertab.base.BaseFilterBean;
import com.samluys.filtertab.util.SpUtils;

import java.util.List;


public class ItemSelectAdapter extends RecyclerView.Adapter {

    private Context mContext;
    private List<BaseFilterBean> mList;
    private boolean isCanMulSelect;

    public ItemSelectAdapter(Context context, List<BaseFilterBean> list, boolean isCanMulSelect) {
        mContext = context;
        mList = list;
        this.isCanMulSelect = isCanMulSelect;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        return new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_mul_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {

        try {
            final BaseFilterBean bean = mList.get(position);
            ViewHolder viewHolder = (ViewHolder) holder;
            viewHolder.btn_content.setText(bean.getItemName());

            // 是否设置“不限”为选中
            boolean isSelectFirst = true;
            for (int i = 0; i < mList.size(); i++) {
                BaseFilterBean entity = mList.get(i);
                if (entity.getSelecteStatus() == 1) {
                    isSelectFirst = false;
                    break;
                }
            }

            if (isSelectFirst) {
                mList.get(0).setSelecteStatus(1);
            }

            if (bean.getSelecteStatus() == 0) {

                GradientDrawable unselectDrawable = new GradientDrawable();
                if (SpUtils.getInstance(mContext).getSolidUnSelectColor() == 0) {
                    unselectDrawable.setStroke(2, SpUtils.getInstance(mContext).getStrokeUnSelectColor());
                }

                if (SpUtils.getInstance(mContext).getTextStyle() == 1) {
                    TextPaint textPaint = viewHolder.btn_content.getPaint();
                    textPaint.setFakeBoldText(false);
                }

                unselectDrawable.setCornerRadius(SpUtils.getInstance(mContext).getCornerRadius());
                unselectDrawable.setColor(SpUtils.getInstance(mContext).getSolidUnSelectColor());
                viewHolder.btn_content.setTextColor(SpUtils.getInstance(mContext).getTextUnSelect());
                viewHolder.btn_content.setBackgroundDrawable(unselectDrawable);
            } else {
                GradientDrawable selectDrawable = new GradientDrawable();
                if (SpUtils.getInstance(mContext).getSolidSelectColor() == 0) {
                    selectDrawable.setStroke(2, SpUtils.getInstance(mContext).getStrokeSelectColor());
                }

                if (SpUtils.getInstance(mContext).getTextStyle() == 1) {
                    TextPaint textPaint = viewHolder.btn_content.getPaint();
                    textPaint.setFakeBoldText(true);
                }

                selectDrawable.setCornerRadius(SpUtils.getInstance(mContext).getCornerRadius());
                selectDrawable.setColor(SpUtils.getInstance(mContext).getSolidSelectColor());
                viewHolder.btn_content.setTextColor(SpUtils.getInstance(mContext).getTextSelect());
                viewHolder.btn_content.setBackgroundDrawable(selectDrawable);
            }

            viewHolder.btn_content.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (isCanMulSelect) {

                        if (position == 0) {
                            // 不限
                            for (int i = 0; i < mList.size(); i++) {
                               mList.get(i).setSelecteStatus(0);
                            }
                        } else {
                            mList.get(0).setSelecteStatus(0);
                            if (mList.get(position).getSelecteStatus() == 0) {
                                mList.get(position).setSelecteStatus(1);
                            } else {
                                mList.get(position).setSelecteStatus(0);
                            }
                        }
                    } else {
                        for (int i = 0; i < mList.size(); i++) {
                            if (i == position) {
                                if (mList.get(position).getSelecteStatus() == 0) {
                                    mList.get(position).setSelecteStatus(1);
                                } else {
                                    mList.get(position).setSelecteStatus(0);
                                }
                            } else {
                                mList.get(i).setSelecteStatus(0);
                            }
                        }
                    }
                    notifyDataSetChanged();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    private static class ViewHolder extends RecyclerView.ViewHolder {

        TextView btn_content;

        public ViewHolder(View itemView) {
            super(itemView);

            btn_content = itemView.findViewById(R.id.btn_content);


        }
    }
}
