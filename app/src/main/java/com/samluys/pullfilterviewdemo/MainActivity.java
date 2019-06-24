package com.samluys.pullfilterviewdemo;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.samluys.filtertab.FilterInfoBean;
import com.samluys.filtertab.FilterResultBean;
import com.samluys.filtertab.FilterTabConfig;
import com.samluys.filtertab.FilterTabView;
import com.samluys.filtertab.StatusBarHelper;
import com.samluys.filtertab.listener.OnSelectResultListener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

public class MainActivity extends AppCompatActivity implements OnSelectResultListener {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarHelper.translucent(this);
        setContentView(R.layout.activity_main);

        String jsonStr = getJson(this,"demo_data.json");
        FilterEntity filterEntity = JsonToObject(jsonStr, FilterEntity.class);

        FilterTabView ftb_filter = findViewById(R.id.ftb_filter);
        ftb_filter.setColorMain(getResources().getColor(R.color.color_FF6F00));
        ftb_filter.removeViews();
        FilterInfoBean bean1 = new FilterInfoBean("区域", FilterTabConfig.FILTER_TYPE_AREA, filterEntity.getArea());
        FilterInfoBean bean2 = new FilterInfoBean("总价", FilterTabConfig.FILTER_TYPE_PRICE, filterEntity.getPrice());
        FilterInfoBean bean3 = new FilterInfoBean("户型", FilterTabConfig.FILTER_TYPE_SINGLE_SELECT, filterEntity.getHouseType());
        FilterInfoBean bean4 = new FilterInfoBean("筛选", FilterTabConfig.FILTER_TYPE_MUL_SELECT, filterEntity.getMulSelect());
        FilterInfoBean bean5 = new FilterInfoBean("几室", FilterTabConfig.FILTER_TYPE_SINGLE_GIRD, filterEntity.getHouseType());
        FilterInfoBean bean6 = new FilterInfoBean("几室", FilterTabConfig.FILTER_TYPE_SINGLE_GIRD, filterEntity.getPrice());


        ftb_filter.addFilterItem(bean1.getTabName(), bean1.getFilterData(), bean1.getPopupType(), 0);
        ftb_filter.addFilterItem(bean2.getTabName(), bean2.getFilterData(), bean2.getPopupType(), 1);
        ftb_filter.addFilterItem(bean3.getTabName(), bean3.getFilterData(), bean3.getPopupType(), 2);
        ftb_filter.addFilterItem(bean4.getTabName(), bean4.getFilterData(), bean4.getPopupType(), 3);
        ftb_filter.addFilterItem(bean5.getTabName(), bean5.getFilterData(), bean5.getPopupType(), 4);
        ftb_filter.addFilterItem(bean6.getTabName(), bean6.getFilterData(), bean6.getPopupType(), 5);



        final TextView toolbar = findViewById(R.id.toolbar);
        toolbar.post(new Runnable() {
            @Override
            public void run() {
                Log.e("LUYS",toolbar.getHeight() + ":::::" );
            }
        });
        ftb_filter.setOnSelectResultListener(this);
    }

    /**
     * 获取assets下的json文件
     *
     * @param context
     * @param fileName
     * @return
     */
    public static String getJson(Context context, String fileName) {
        //将json数据变成字符串
        StringBuilder stringBuilder = new StringBuilder();
        try {
            //获取assets资源管理器
            AssetManager assetManager = context.getAssets();
            //通过管理器打开文件并读取
            BufferedReader bf = new BufferedReader(new InputStreamReader(
                    assetManager.open(fileName), "utf-8"));
            String line;
            while ((line = bf.readLine()) != null) {
                stringBuilder.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }

    /**
     * 将字符串转换为 对象
     *
     * @param json
     * @param type
     * @return
     */
    public static <T> T JsonToObject(String json, Class<T> type) {
        try {
            Gson gson = new Gson();
            return gson.fromJson(json, type);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public void onSelectResult(FilterResultBean resultBean) {

        String message="";
        if(resultBean.getPopupType() == 3) {
            List<FilterResultBean.MulTypeBean> list = resultBean.getSelectList();
            for (int i = 0; i < list.size(); i++) {
                FilterResultBean.MulTypeBean bean = list.get(i);
                if (i == (list.size() - 1)) {
                    message = message + bean.getItemName();
                } else {
                    message = message + bean.getItemName() + ",";
                }
            }
        } else {
            message =resultBean.getItemId()+":"+resultBean.getName();
        }

        Toast.makeText(this,message,Toast.LENGTH_LONG).show();
    }

    @Override
    public void onSelectResultList(List<FilterResultBean> resultBeans) {
        String message="";
        List<FilterResultBean> list = resultBeans;
        for (int i = 0; i < list.size(); i++) {
            FilterResultBean bean = list.get(i);
            if (i == (list.size() - 1)) {
                message = message + bean.getName();
            } else {
                message = message + bean.getName() + ",";
            }
        }
        Toast.makeText(this,message,Toast.LENGTH_LONG).show();
    }
}
