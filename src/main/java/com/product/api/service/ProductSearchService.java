package com.product.api.service;

import com.alibaba.fenxiao.crossborder.param.*;
import com.alibaba.ocean.rawsdk.ApiExecutor;
import com.alibaba.ocean.rawsdk.client.entity.AuthorizationToken;
import com.product.api.factory.ApiRequestFactory;
import com.product.api.param.ImageQueryParam;
import com.product.api.param.ProductDetailParam;
import com.product.api.param.ProductRecommendParam;
import com.product.api.param.ProductSearchParam;
import com.product.api.util.RetryUtil;
import com.product.api.util.SecretsManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;


/**
 * 商品搜索服务类
 * This class is used to search for products.
 */
@Service
public class ProductSearchService {

    private final ApiExecutor apiExecutor;
    private final SecretsManager secretsManager;
    private final ApiRequestFactory apiRequestFactory;
    private String accessToken;

    public ProductSearchService(ApiExecutor apiExecutor, SecretsManager secretsManager,
                                ApiRequestFactory apiRequestFactory) {
        this.apiExecutor = apiExecutor;
        this.secretsManager = secretsManager;
        this.apiRequestFactory = apiRequestFactory;
        this.accessToken = secretsManager.getSecret("AccessToken");
    }

    public ProductSearchKeywordQueryResult searchProductsByKeyword(ProductSearchParam productSearchParam) {
        ProductSearchKeywordQueryParam param = apiRequestFactory.createKeywordQueryParam(productSearchParam);
        return RetryUtil.executeWithRetry(() -> apiExecutor.execute(param, accessToken), param.getOceanApiId().getName(), this::refreshAccessToken);
    }

    public ProductSearchQueryProductDetailResult queryProductDetail(ProductDetailParam productDetailParam) {
        ProductSearchQueryProductDetailParam param = apiRequestFactory.createProductDetailParam(productDetailParam);
        return RetryUtil.executeWithRetry(() -> apiExecutor.execute(param, accessToken) , param.getOceanApiId().getName(), this::refreshAccessToken);
    }

    public ProductSearchOfferRecommendResult searchRecommendProduct(ProductRecommendParam productRecommendParam) {
        productRecommendParam.setOutMemberId(secretsManager.getSecret("MemberId"));
        ProductSearchOfferRecommendParam param = apiRequestFactory.createProductRecommendParam(productRecommendParam);
        return RetryUtil.executeWithRetry(() -> apiExecutor.execute(param, accessToken), param.getOceanApiId().getName(), this::refreshAccessToken);
    }

    public ProductImageUploadResult uploadImageAndQueryImageId(ImageQueryParam queryParam) {
        // Step 1: Upload the image and get the image ID
        ProductImageUploadParam param = apiRequestFactory.createImageUploadParam(queryParam.getBase64Image());
        return RetryUtil.executeWithRetry(() -> apiExecutor.execute(param, accessToken), param.getOceanApiId().getName(), this::refreshAccessToken);
    }

    public ProductSearchImageQueryResult imageQuery(ImageQueryParam imageQueryParam) {
        ProductSearchImageQueryParam param = apiRequestFactory.createImageQueryParam(imageQueryParam);
        return RetryUtil.executeWithRetry(() -> apiExecutor.execute(param, accessToken), param.getOceanApiId().getName(), this::refreshAccessToken);
    }

    private void refreshAccessToken() {
        AuthorizationToken token = apiExecutor.refreshToken(secretsManager.getSecret("RefreshToken"));
        String newAccessToken = token.getAccess_token();
        secretsManager.setSecret("AccessToken", newAccessToken); // Update the secrets map with the new access token
        this.accessToken = newAccessToken;
    }

//    private String getAccessToken() {
//        String accessToken = AuthService.getAccessToken(
//                appConfig.getHost(),
//                appConfig.getClientId(),
//                secretsManager.getSecret("AppSecret"),
//                appConfig.getRedirectUri(),
//                secretsManager.getSecret("RefreshToken")
//        );
//        LOG.info("授权令牌的返回结果：{}", accessToken);
//        secretsManager.setSecret("AccessToken", accessToken); // Update the secrets map with the new access token
//        return accessToken;
//    }
}