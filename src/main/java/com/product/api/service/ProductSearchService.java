package com.product.api.service;

import com.alibaba.fenxiao.crossborder.param.ProductSearchKeywordQueryParam;
import com.alibaba.fenxiao.crossborder.param.ProductSearchKeywordQueryParamOfferQueryParam;
import com.alibaba.fenxiao.crossborder.param.ProductSearchKeywordQueryResult;
import com.alibaba.ocean.rawsdk.ApiExecutor;
import org.springframework.stereotype.Service;

@Service
public class ProductSearchService {

    private final ApiExecutor apiExecutor;

    public ProductSearchService(ApiExecutor apiExecutor) {
        this.apiExecutor = apiExecutor;
    }

    public ProductSearchKeywordQueryResult searchProductsByKeyword(String keyword, String accessToken) {
        ProductSearchKeywordQueryParam param = new ProductSearchKeywordQueryParam();
        // Set the keyword in the request parameter
        ProductSearchKeywordQueryParamOfferQueryParam offerQueryParam = new ProductSearchKeywordQueryParamOfferQueryParam();
        offerQueryParam.setKeyword(keyword);
        param.setOfferQueryParam(offerQueryParam);

        // Call the API
        ProductSearchKeywordQueryResult result = apiExecutor.execute(param, accessToken).getResult();

        // Handle the response
        if (result != null && result.getResult().getSuccess()) {
            return result;
        } else {
            throw new RuntimeException("API call failed: " + (result != null ? result.getResult().getMessage() : "Unknown error"));
        }
    }
}
