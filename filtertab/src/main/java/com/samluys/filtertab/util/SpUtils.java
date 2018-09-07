package com.samluys.filtertab.util;


import android.content.Context;
import android.content.SharedPreferences;

import com.samluys.filtertab.R;

public class SpUtils {
    public static final String PREFERENCE_NAME = "config_info";
    private static SpUtils instance;
    private SharedPreferences sharedPreferences;
    private static SharedPreferences.Editor editor;
    private Context mContext;

    private SpUtils (Context context) {
        mContext = context;
        sharedPreferences = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public static SpUtils getInstance(Context context) {

        if (instance == null) {
            synchronized (SharedPreferences.class){
                if (instance == null) {
                    instance = new SpUtils(context);
                }
            }
        }
        return instance;
    }

    /**
     * 选中字体为粗体的样式
     * @param value
     */
    public void putTextStyle(int value) {
        editor.putInt("text_style", value);
        editor.commit();
    }

    public int getTextStyle() {
        return sharedPreferences.getInt("text_style", 0);
    }

    /**
     * 主题色
     * @param value
     */
    public void putColorMain(int value) {
        editor.putInt("color_main", value);
        editor.commit();
    }

    public int getColorMain() {
        return sharedPreferences.getInt("color_main", mContext.getResources().getColor(R.color.color_main));
    }

    /**
     * 默认字体颜色
     * @param value
     */
    public void putDefaultTextColor(int value) {
        editor.putInt("default_text_color", value);
        editor.commit();
    }

    public int getDefaultTextColor() {
        return sharedPreferences.getInt("default_text_color", mContext.getResources().getColor(R.color.color_default_text));
    }

    /**
     * button选中后边框的颜色
     * @param value
     */
    public void putStrokeSelectColor(int value) {
        editor.putInt("btnStrokeSelect", value);
        editor.commit();
    }

    public int getStrokeSelectColor() {
        return sharedPreferences.getInt("btnStrokeSelect", mContext.getResources().getColor(R.color.color_main));
    }

    /**
     * button未选中后边框的颜色
     * @param value
     */
    public void putStrokeUnSelectColor(int value) {
        editor.putInt("btnStrokeUnselect", value);
        editor.commit();
    }

    public int getStrokeUnSelectColor() {
        return sharedPreferences.getInt("btnStrokeUnselect", mContext.getResources().getColor(R.color.color_dfdfdf));
    }

    /**
     * button未选中填充的颜色
     * @param value
     */
    public void putSolidUnSelectColor(int value) {
        editor.putInt("btnSolidUnselect", value);
        editor.commit();
    }

    public int getSolidUnSelectColor() {
        return sharedPreferences.getInt("btnSolidUnselect", 0);
    }

    /**
     * button选中填充的颜色
     * @param value
     */
    public void putSolidSelectColor(int value) {
        editor.putInt("btnSolidSelect", value);
        editor.commit();
    }

    public int getSolidSelectColor() {
        return sharedPreferences.getInt("btnSolidSelect", 0);
    }

    /**
     * button圆角弧度
     * @param value
     */
    public void putCornerRadius(float value) {
        editor.putFloat("btnCornerRadius", value);
        editor.commit();
    }

    public float getCornerRadius() {
        return sharedPreferences.getFloat("btnCornerRadius", 0);
    }

    /**
     * button选中字体颜色
     * @param value
     */
    public void putTextSelect(int value) {
        editor.putInt("btnTextSelect", value);
        editor.commit();
    }

    public int getTextSelect() {
        return sharedPreferences.getInt("btnTextSelect", mContext.getResources().getColor(R.color.color_main));
    }

    /**
     * button未选中字体颜色
     * @param value
     */
    public void putTextUnSelect(int value) {
        editor.putInt("btnTextUnSelect", value);
        editor.commit();
    }

    public int getTextUnSelect() {
        return sharedPreferences.getInt("btnTextUnSelect", mContext.getResources().getColor(R.color.color_666666));
    }

    /**
     * 多选Item的列数
     * @param value
     */
    public void putColumnNum(int value) {
        editor.putInt("column_num", value);
        editor.commit();
    }

    public int getColumnNum() {
        return sharedPreferences.getInt("column_num", 3);
    }
}
