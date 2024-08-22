package com.product.api.factory;

import com.alibaba.fenxiao.crossborder.param.*;
import com.product.api.param.*;

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

    public ProductSearchTopKeywordParam createTopKeywordParam(TopKeywordParam topKeywordParam) {
        ProductSearchTopKeywordParam param = new ProductSearchTopKeywordParam();
        AlibabaCbuOfferParamTopSeKeywordParam topSeKeywordParam = new AlibabaCbuOfferParamTopSeKeywordParam();
        topSeKeywordParam.setCountry(topKeywordParam.getCountry());
        topSeKeywordParam.setSourceId(topKeywordParam.getSourceId());
        topSeKeywordParam.setHotKeywordType(topKeywordParam.getHotKeywordType());
        param.setTopSeKeywordParam(topSeKeywordParam);
        return param;
    }

    public ProductTopListQueryParam createRankQueryParam(RankQueryParam rankQueryParam) {
        ProductTopListQueryParam param = new ProductTopListQueryParam();
        ProductTopListQueryRankQueryParams rankQueryParams = new ProductTopListQueryRankQueryParams();
        rankQueryParams.setRankId(rankQueryParam.getRankId());
        rankQueryParams.setRankType(rankQueryParam.getRankType());
        rankQueryParams.setLanguage(rankQueryParam.getLanguage());
        rankQueryParams.setLimit(rankQueryParam.getLimit());
        param.setRankQueryParams(rankQueryParams);
        return param;
    }

    public CategoryTranslationGetByKeywordParam createCategorySearchParam(CategorySearchParam categorySearchParam) {
        CategoryTranslationGetByKeywordParam param = new CategoryTranslationGetByKeywordParam();
        param.setCateName(categorySearchParam.getCateName());
        param.setLanguage(categorySearchParam.getLanguage());
        return param;
    }

    public ProductSearchOfferRecommendParam createProductRecommendParam(ProductRecommendParam productRecommendParam) {
        ProductSearchOfferRecommendParam param = new ProductSearchOfferRecommendParam();
        AlibabaCbuOfferParamRecommendOfferParam offerParam = new AlibabaCbuOfferParamRecommendOfferParam();
        offerParam.setCountry(productRecommendParam.getCountry());
        offerParam.setBeginPage(productRecommendParam.getBeginPage());
        offerParam.setPageSize(productRecommendParam.getPageSize());
        offerParam.setOutMemberId(productRecommendParam.getOutMemberId());
        param.setRecommendOfferParam(offerParam);
        return param;
    }
}