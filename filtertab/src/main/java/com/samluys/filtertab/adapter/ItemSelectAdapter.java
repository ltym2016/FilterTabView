package com.samluys.filtertab.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.samluys.filtertab.R;
import com.samluys.filtertab.base.BaseFilterBean;

import java.util.List;


public class ItemSelectAdapter extends RecyclerView.Adapter {

    private Context mContext;
    private List<BaseFilterBean> mList;

    public ItemSelectAdapter(Context context, List<BaseFilterBean> list) {
        mContext = context;
        mList = list;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        return new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_mul_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {

        try {
            BaseFilterBean bean = mList.get(position);
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
                viewHolder.btn_content.setTextColor(mContext.getResources().getColor(R.color.color_666666));
                viewHolder.btn_content.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.bg_button_normal));
            } else {
                viewHolder.btn_content.setTextColor(mContext.getResources().getColor(R.color.color_main));
                viewHolder.btn_content.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.bg_button_select));
            }

            viewHolder.btn_content.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    for (int i = 0; i < mList.size(); i++) {
                        if (i == position) {
                            mList.get(position).setSelecteStatus(1);
                        } else {
                            mList.get(i).setSelecteStatus(0);
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

        Button btn_content;

        public ViewHolder(View itemView) {
            super(itemView);

            btn_content = itemView.findViewById(R.id.btn_content);


        }
    }
}
