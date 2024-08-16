package com.product.api.param;

public class TopKeywordParam {
    private String country;

    //    类目id
    private String sourceId;

    // 热搜类型，目前只提供类目纬度，此固定传cate
    private String hotKeywordType;

    public String getCountry() {
        return country;
    }

    public String getSourceId() {
        return sourceId;
    }

    public String getHotKeywordType() {
        return hotKeywordType;
    }
}
