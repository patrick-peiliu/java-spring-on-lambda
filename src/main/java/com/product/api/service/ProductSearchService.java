package com.product.api.service;

import com.alibaba.fenxiao.crossborder.param.*;
import com.alibaba.ocean.rawsdk.ApiExecutor;
import com.product.api.auth.AuthService;
import com.product.api.config.AppConfig;
import com.product.api.param.ImageQueryParam;
import com.product.api.param.ProductDetailParam;
import com.product.api.param.ProductSearchParam;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;

@Service
public class ProductSearchService {
    private static final Logger LOG = LogManager.getLogger();

    private final ApiExecutor apiExecutor;
    private final AppConfig appConfig;

    public ProductSearchService(ApiExecutor apiExecutor, AppConfig appConfig) {
        this.apiExecutor = apiExecutor;
        this.appConfig = appConfig;
    }

    /**
     * todo
     * 1 特殊情况处理。主要是accessToken失效以及refreshToken过期的处理。
     * 如果accessToken失效，那么就需要用保存的refreshToken调用getToken接口生成一个新的accessToken；
     * 如果refreshToken失效，那么需要重新进行用户授权。
     * 2 错误处理。如果出现签名错误、时间戳错误等api调用失败的情况，最好是能够在日志中记录当前调用的url以及参数，这样即使在出现问题时也能快速查找并解决问题。
     */

    /*
    * 多语言关键词搜索
    * */
    public ProductSearchKeywordQueryResult searchProductsByKeyword(ProductSearchParam productSearchParam) {
        String accessToken = AuthService.getAccessToken(
                appConfig.getHost(),
                appConfig.getClientId(),
                appConfig.getAppSecret(),
                appConfig.getRedirectUri(),
                appConfig.getRefreshToken()
        );
        LOG.info("授权令牌的返回结果：{}", accessToken);
        ProductSearchKeywordQueryParam param = new ProductSearchKeywordQueryParam();
        ProductSearchKeywordQueryParamOfferQueryParam offerQueryParam = new ProductSearchKeywordQueryParamOfferQueryParam();
        offerQueryParam.setKeyword(productSearchParam.getKeyword());
        offerQueryParam.setBeginPage(productSearchParam.getBeginPage());
        offerQueryParam.setPageSize(productSearchParam.getPageSize());
        offerQueryParam.setCountry(productSearchParam.getCountry());
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

    /*
     * 多语言商详
     * */
    public ProductSearchQueryProductDetailResult queryProductDetail(ProductDetailParam productDetailParam) {
        String accessToken = AuthService.getAccessToken(
                appConfig.getHost(),
                appConfig.getClientId(),
                appConfig.getAppSecret(),
                appConfig.getRedirectUri(),
                appConfig.getRefreshToken()
        );
        LOG.info("授权令牌的返回结果：{}", accessToken);
        ProductSearchQueryProductDetailParam param = new ProductSearchQueryProductDetailParam();
        ProductSearchQueryProductDetailParamOfferDetailParam detailParam = new ProductSearchQueryProductDetailParamOfferDetailParam();
        detailParam.setCountry(productDetailParam.getCountry());
        detailParam.setOfferId(productDetailParam.getOfferId());
        param.setOfferDetailParam(detailParam);

        // Call the API
        ProductSearchQueryProductDetailResult result = apiExecutor.execute(param, accessToken).getResult();

        // Handle the response
        if (result != null && result.getResult().getSuccess()) {
            return result;
        } else {
            throw new RuntimeException("API call failed: " + (result != null ? result.getResult().getMessage() : "Unknown error"));
        }
    }

    /*
     * 上传图片获取imageId
     * */
    public ProductImageUploadResult productImageUpload(MultipartFile imageFile) {
        String accessToken = AuthService.getAccessToken(
                appConfig.getHost(),
                appConfig.getClientId(),
                appConfig.getAppSecret(),
                appConfig.getRedirectUri(),
                appConfig.getRefreshToken()
        );
        LOG.info("授权令牌的返回结果：{}", accessToken);

        // Convert image file to base64 string
        String base64Image;
        try {
            byte[] imageBytes = imageFile.getBytes();
            base64Image = Base64.getEncoder().encodeToString(imageBytes);
        } catch (IOException e) {
            throw new RuntimeException("Failed to convert image file to base64", e);
        }

        ProductImageUploadParam param = new ProductImageUploadParam();
        ProductImageUploadParamUploadImageParam imageParam = new ProductImageUploadParamUploadImageParam();
        imageParam.setImageBase64(base64Image);
        param.setUploadImageParam(imageParam);
        // Call the API
        ProductImageUploadResult result = apiExecutor.execute(param, accessToken).getResult();

        // Handle the response
        // String success = result.getResult().getSuccess();
        if (result != null && Boolean.TRUE.toString().equals(result.getResult().getSuccess())) {
            return result;
        } else {
            throw new RuntimeException("API call failed: " + (result != null ? result.getResult().getMessage() : "Unknown error"));
        }
    }

    /*
     * 多语言图搜
     * */
    public ProductSearchImageQueryResult imageQuery(ImageQueryParam imageQueryParam) {
        String accessToken = AuthService.getAccessToken(
                appConfig.getHost(),
                appConfig.getClientId(),
                appConfig.getAppSecret(),
                appConfig.getRedirectUri(),
                appConfig.getRefreshToken()
        );
        LOG.info("授权令牌的返回结果：{}", accessToken);
        ProductSearchImageQueryParam param = new ProductSearchImageQueryParam();
        ProductSearchImageQueryParamOfferQueryParam offerQueryParam = new ProductSearchImageQueryParamOfferQueryParam();
        offerQueryParam.setImageId(imageQueryParam.getImageId());
        offerQueryParam.setCountry(imageQueryParam.getCountry());
        offerQueryParam.setBeginPage(imageQueryParam.getBeginPage());
        offerQueryParam.setPageSize(imageQueryParam.getPageSize());
        param.setOfferQueryParam(offerQueryParam);

        // Call the API
        ProductSearchImageQueryResult result = apiExecutor.execute(param, accessToken).getResult();

        // Handle the response
        if (result != null && Boolean.TRUE.toString().equals(result.getResult().getSuccess())) {
            return result;
        } else {
            throw new RuntimeException("API call failed: " + (result != null ? result.getResult().getMessage() : "Unknown error"));
        }
    }
}
