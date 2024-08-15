package com.product.api.param;

public class ImageQueryParam {
    private String imageId;
    private String keyword;
    private Integer beginPage;
    private Integer pageSize;
    private String filter;
    private String sort;
    private String priceStart;
    private String priceEnd;
    private Long categoryId;
    private String country;

    public String getFilter() {
        return filter;
    }

    public String getImageId() {
        return imageId;
    }

    public String getKeyword() {
        return keyword;
    }

    public Integer getBeginPage() {
        return beginPage;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public String getSort() {
        return sort;
    }

    public String getPriceStart() {
        return priceStart;
    }

    public String getPriceEnd() {
        return priceEnd;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public String getCountry() {
        return country;
    }
}
