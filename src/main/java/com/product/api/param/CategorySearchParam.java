package com.product.api.param;


/**
 * language
 * java.lang.String
 * 是
 * 语种。见常见问题中的枚举。
 * ja
 * cateName
 * java.lang.String
 * 是
 * 类目名。可模糊搜索。
 * 裙子
 */
public class CategorySearchParam {
    private String language;
    private String cateName;

    public String getLanguage() {
        return language;
    }

    public String getCateName() {
        return cateName;
    }
}
