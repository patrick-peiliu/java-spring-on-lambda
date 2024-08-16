package com.product.api.param;


/**
 * rankId
 * java.lang.String
 * 是
 * 榜单ID，可传入类目ID，目前支持类目榜单
 * 1111
 * rankType
 * java.lang.String
 * 是
 * 榜单类型，complex综合榜，hot热卖榜，goodPrice好价榜
 * complex
 * limit
 * java.lang.Integer
 * 是
 * 榜单商品个数，最多20
 * 10
 * language
 * java.lang.String
 * 是
 * 榜单商品语言
 * en
 */
public class RankQueryParam {
    private String rankId;
    private String rankType;
    private Integer limit;
    private String language;

    public String getRankId() {
        return rankId;
    }

    public String getRankType() {
        return rankType;
    }

    public Integer getLimit() {
        return limit;
    }

    public String getLanguage() {
        return language;
    }
}
