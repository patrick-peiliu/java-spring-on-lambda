package com.product.api.param;

/**
 * imageId
 * java.lang.String
 * 是
 * 图片id，必传
 * 图片id
 * beginPage
 * java.lang.Integer
 * 是
 * 分页
 * 分页
 * pageSize
 * java.lang.Integer
 * 是
 * 分页，最大不超过50，建议20效果最佳
 * 分页
 * region
 * java.lang.String
 * 否
 * 主体选择
 * 266,799,48,581
 * filter
 * java.lang.String
 * 否
 * 筛选参数，多个通过英文逗号分隔，枚举参见解决方案介绍
 * shipInToday,ksCiphertext
 * sort
 * java.lang.String
 * 否
 * 排序参数，枚举参见解决方案介绍
 * {"price":"asc"}
 * outMemberId
 * java.lang.String
 * 否
 * 外部用户uid
 * 外部用户uid
 * priceStart
 * java.lang.String
 * 否
 * 批发价开始
 * 10
 * priceEnd
 * java.lang.String
 * 否
 * 批发价结束
 * 20
 * categoryId
 * java.lang.Long
 * 否
 * 类目id
 * 类目id
 * imageAddress
 * java.lang.String
 * 否
 * 图片地址，仅使用1688图片链接查询场景，其他不保证有数据返回
 * 图片地址
 * country
 * java.lang.String
 * 是
 * 语言
 * 如en-英语，详细枚举请参考开发人员参考菜单
 * keyword
 * String
 * 否
 * 在结果中搜索
 * 书本
 * auxiliaryText
 * String
 * 否
 * 多模态图搜文案
 * 热卖的
 * productCollectionId
 * String
 * 否
 * 寻源通工作台货盘ID
 * 21432232
 */
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
    private String base64Image;
    private String imageAddress;

    public String getBase64Image() {
        return base64Image;
    }

    public String getFilter() {
        return filter;
    }

    public String getImageId() {
        return imageId;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
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

    public void setSort(String sort) { this.sort = sort;}

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

    public void setBeginPage(Integer beginPage) {
        this.beginPage = beginPage;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getImageAddress() {
        return this.imageAddress;
    }
}
