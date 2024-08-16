package com.product.api.param;

/**
 * offerId
 * Long
 * 是
 * 商品id
 * 123
 * startDate
 * String
 * 是
 * 查询起始时间
 * 20240701
 * endDate
 * String
 * 是
 * 查询截止时间
 * 20240704
 */
public class OfferSellTrendQueryParam {
    private Long offerId;
    private String startDate;
    private String endDate;

    public Long getOfferId() {
        return offerId;
    }

    public String getStartDate() {
        return startDate;
    }

    public String getEndDate() {
        return endDate;
    }
}
