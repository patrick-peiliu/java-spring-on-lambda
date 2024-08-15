package com.product.api.factory;

import com.alibaba.fenxiao.crossborder.param.*;
import com.product.api.param.ImageQueryParam;
import com.product.api.param.ProductDetailParam;
import com.product.api.param.ProductSearchParam;

public class ApiRequestFactory {

    public ProductSearchKeywordQueryParam createKeywordQueryParam(ProductSearchParam productSearchParam) {
        ProductSearchKeywordQueryParam param = new ProductSearchKeywordQueryParam();
        ProductSearchKeywordQueryParamOfferQueryParam offerQueryParam = new ProductSearchKeywordQueryParamOfferQueryParam();
        offerQueryParam.setKeyword(productSearchParam.getKeyword());
        offerQueryParam.setBeginPage(productSearchParam.getBeginPage());
        offerQueryParam.setPageSize(productSearchParam.getPageSize());
        offerQueryParam.setCountry(productSearchParam.getCountry());
        param.setOfferQueryParam(offerQueryParam);
        return param;
    }

    public ProductSearchQueryProductDetailParam createProductDetailParam(ProductDetailParam productDetailParam) {
        ProductSearchQueryProductDetailParam param = new ProductSearchQueryProductDetailParam();
        ProductSearchQueryProductDetailParamOfferDetailParam detailParam = new ProductSearchQueryProductDetailParamOfferDetailParam();
        detailParam.setCountry(productDetailParam.getCountry());
        detailParam.setOfferId(productDetailParam.getOfferId());
        param.setOfferDetailParam(detailParam);
        return param;
    }

    public ProductImageUploadParam createImageUploadParam(String base64Image) {
        ProductImageUploadParam param = new ProductImageUploadParam();
        ProductImageUploadParamUploadImageParam imageParam = new ProductImageUploadParamUploadImageParam();
        imageParam.setImageBase64(base64Image);
        param.setUploadImageParam(imageParam);
        return param;
    }

    public ProductSearchImageQueryParam createImageQueryParam(ImageQueryParam imageQueryParam) {
        ProductSearchImageQueryParam param = new ProductSearchImageQueryParam();
        ProductSearchImageQueryParamOfferQueryParam offerQueryParam = new ProductSearchImageQueryParamOfferQueryParam();
        offerQueryParam.setImageId(imageQueryParam.getImageId());
        offerQueryParam.setCountry(imageQueryParam.getCountry());
        offerQueryParam.setBeginPage(imageQueryParam.getBeginPage());
        offerQueryParam.setPageSize(imageQueryParam.getPageSize());
        param.setOfferQueryParam(offerQueryParam);
        return param;
    }
}