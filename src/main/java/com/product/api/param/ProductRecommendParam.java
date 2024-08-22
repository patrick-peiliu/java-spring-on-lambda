package com.product.api.param;

/**beginPage
 java.lang.Integer
 是
 开始页码
 1
 pageSize
 java.lang.Integer
 是
 页面大小
 20
 country
 java.lang.String
 是
 语言
 en-英语，ja-日语，具体请参考开放参考菜单
 outMemberId
 java.lang.String
 是
 外部机构用户id，此id为服务商平台id
 dferg0001
 */
public class ProductRecommendParam {
    private String  outMemberId;
    private Integer beginPage;
    private Integer pageSize;
    private String country;

    public String getCountry() {
        return country;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public Integer getBeginPage() {
        return beginPage;
    }

    public String getOutMemberId() {
        return outMemberId;
    }

    public void setOutMemberId(String outMemberId) {
        this.outMemberId = outMemberId;
    }
}
