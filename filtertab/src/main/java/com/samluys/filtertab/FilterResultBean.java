package com.samluys.filtertab;

import java.util.List;


public class FilterResultBean {

    /**
     * Popupwindow布局位置
     */
    private int popupIndex;

    /**
     * Popupwindow区分
     */
    private int popupType;

    /**
     * 单选或者父级ID
     */
    private int itemId = -1;

    /**
     * 子Id,有二级分类使用
     */
    private int childId = -1;

    /**
     * 返回的筛选名称
     */
    private String name;

    /**
     * 多选返回结果
     */
    private List<MulTypeBean> selectList;

    public int getPopupIndex() {
        return popupIndex;
    }

    public void setPopupIndex(int popupIndex) {
        this.popupIndex = popupIndex;
    }

    public int getPopupType() {
        return popupType;
    }

    public void setPopupType(int popupType) {
        this.popupType = popupType;
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public int getChildId() {
        return childId;
    }

    public void setChildId(int childId) {
        this.childId = childId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<MulTypeBean> getSelectList() {
        return selectList;
    }

    public void setSelectList(List<MulTypeBean> selectList) {
        this.selectList = selectList;
    }

    public static class MulTypeBean {

        /**
         * 大分类对应的key
         */
        private String typeKey;

        /**
         * 大分类对应的名称
         */
        private String typeName;

        /**
         * 选择的Id
         */
        private int itemId;

        /**
         * 选择的名称
         */
        private String itemName;

        public String getItemName() {
            return itemName;
        }

        public void setItemName(String itemName) {
            this.itemName = itemName;
        }

        public String getTypeName() {
            return typeName;
        }

        public void setTypeName(String typeName) {
            this.typeName = typeName;
        }

        public String getTypeKey() {
            return typeKey;
        }

        public void setTypeKey(String typeKey) {
            this.typeKey = typeKey;
        }

        public int getItemId() {
            return itemId;
        }

        public void setItemId(int itemId) {
            this.itemId = itemId;
        }
    }
}
