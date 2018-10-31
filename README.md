# FilterTabView

[![Download](https://img.shields.io/badge/DownloadApp-2.1M-ff69b4.svg) ](https://github.com/ltym2016/FilterTabView/blob/master/app-debug.apk)

## 基于Popupwindow的下拉筛选控件
<img src="https://github.com/ltym2016/FilterTabView/blob/master/video2gif_20180713_144109.gif" width="200"  align=center /> <img src="https://github.com/ltym2016/FilterTabView/blob/master/Screenshot1.png" width="200"  align=center /> <img src="https://github.com/ltym2016/FilterTabView/blob/master/Screenshot2.png" width="200"  align=center />
<img src="https://github.com/ltym2016/FilterTabView/blob/master/Screenshot3.png" width="200"  align=center /> <img src="https://github.com/ltym2016/FilterTabView/blob/master/Screenshot4.png" width="200"  align=center /> <img src="https://github.com/ltym2016/FilterTabView/blob/master/Screenshot5.png" width="200"  align=center />


功能|区分
---|---
支持区域二级联动选择| FilterTabConfig.FILTER_TYPE_AREA
支持单行List样式选择 |FilterTabConfig.FILTER_TYPE_SINGLE_SELECT
支持带EditText的单行选择 |FilterTabConfig.FILTER_TYPE_PRICE
支持多类型选择| FilterTabConfig.FILTER_TYPE_MUL_SELECT
支持Grid样式多选| FilterTabConfig.FILTER_TYPE_SINGLE_GIRD
- 目前只支持以上5种类型的Popupwindow样式，可以自由组合
# 如何使用
## Gradle
- Add it in your root build.gradle at the end of repositories:
 ```
allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
```
- Add the dependency
```
dependencies {        
    implementation 'com.github.ltym2016:FilterTabView:-SNAPSHOT'
}

```

## Maven
```
<repositories>
		<repository>
		    <id>jitpack.io</id>
		    <url>https://jitpack.io</url>
		</repository>
	</repositories>
	
	<dependency>
    	    <groupId>com.github.ltym2016</groupId>
    	    <artifactId>FilterTabView</artifactId>
    	    <version>-SNAPSHOT</version>
    	</dependency>
```
## XML
```
<com.samluys.filtertab.FilterTabView
    android:id="@+id/ftb_filter"
    android:layout_width="match_parent"
    android:layout_height="@dimen/tool_bar"
    android:background="@color/white"
    app:btn_solid_select_color="@color/color_e3ecf6"
    app:btn_solid_unselect_color="@color/color_f5f5f6"
    app:btn_corner_radius="24dp"
    app:btn_text_select_color="@color/color_222222"
    app:btn_text_unselect_color="@color/color_222222"
    app:tab_text_style="1"
    app:column_num="3"
    app:color_main="#60dd6c"/>
```
自定义属性|说明
---|---
tab_arrow_select_color | 筛选Tab选择的图片
tab_arrow_unselect_color|筛选Tab选择的图片
tab_text_style | 选中状态是否为粗体 0为否 1为是 默认为0
color_main | 主题色
btn_stroke_select_color| 选中button边框颜色 
btn_stroke_unselect_color|未选中button边框颜色 
btn_solid_select_color |选中button填充颜色
btn_solid_unselect_color| 未选中button填充颜色
btn_corner_radius | button圆角大小
btn_text_select_color| 选中字体颜色
btn_text_unselect_color| 未选中字体颜色
column_num |grid样式下的列数

## Java
```
FilterInfoBean bean1 = new FilterInfoBean("区域", FilterTabConfig.FILTER_TYPE_AREA, filterEntity.getArea());
FilterInfoBean bean2 = new FilterInfoBean("总价", FilterTabConfig.FILTER_TYPE_PRICE, filterEntity.getPrice());
FilterInfoBean bean3 = new FilterInfoBean("户型", FilterTabConfig.FILTER_TYPE_SINGLE_SELECT, filterEntity.getHouseType());
FilterInfoBean bean4 = new FilterInfoBean("筛选", FilterTabConfig.FILTER_TYPE_MUL_SELECT, filterEntity.getMulSelect());

ftb_filter.addFilterItem(bean1.getTabName(), bean1.getFilterData(), bean1.getPopupType(), 0);
ftb_filter.addFilterItem(bean2.getTabName(), bean2.getFilterData(), bean2.getPopupType(), 1);
ftb_filter.addFilterItem(bean3.getTabName(), bean3.getFilterData(), bean3.getPopupType(), 2);
ftb_filter.addFilterItem(bean4.getTabName(), bean4.getFilterData(), bean4.getPopupType(), 3);
```
- javabean 要继承 BaseFilterBean，重写里面的方法
```
public class FilterAreaEntity extends BaseFilterBean {
    ....
}
```
- 需要用到的Activity或者fragment里面实现OnSelectResultListener这个接口
```
ftb_filter.setOnSelectResultListener(new OnSelectResultListener() {
    @Override
    public void onSelectResult(FilterResultBean resultBean) {
        // 接受点击的返回值
    }
});
```
- 控件调用之前最好初始化一下，避免加载失败重新加载数据是出现重复的问题。
```
ftb_filter.removeViews();
```
- OnSelectFilterNameListener  实现这个接口可以拿到选择后对应的Tab名称
```
ftb_filter.setOnSelectFilterNameListener(new OnSelectFilterNameListener() {
    @Override
    public void onSelectFilterName(String name, int popupindex) {
        // name：tab名称  popupindex：对应的popup的位置
    }
});
```
- OnPopupDismissListener  Popupwindow消失监听
