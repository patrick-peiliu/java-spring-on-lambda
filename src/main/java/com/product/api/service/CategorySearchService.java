package com.product.api.service;

import com.alibaba.fenxiao.crossborder.param.*;
import com.alibaba.ocean.rawsdk.ApiExecutor;
import com.alibaba.ocean.rawsdk.client.entity.AuthorizationToken;
import com.product.api.factory.ApiRequestFactory;
import com.product.api.param.CategorySearchParam;
import com.product.api.param.RankQueryParam;
import com.product.api.param.TopKeywordParam;
import com.product.api.util.RetryUtil;
import com.product.api.util.SecretsManager;
import org.springframework.stereotype.Service;

/**
 * 类目服务类
 * This class is used to search for categories.
 */
@Service
public class CategorySearchService {

    private final ApiExecutor apiExecutor;
    private final SecretsManager secretsManager;
    private final ApiRequestFactory apiRequestFactory;
    private String accessToken;

    public CategorySearchService(ApiExecutor apiExecutor, SecretsManager secretsManager,
                                ApiRequestFactory apiRequestFactory) {
        this.apiExecutor = apiExecutor;
        this.secretsManager = secretsManager;
        this.apiRequestFactory = apiRequestFactory;
        this.accessToken = secretsManager.getSecret("AccessToken");
    }

    public ProductSearchTopKeywordResult searchTopKeywords(TopKeywordParam topKeywordParam) {
        ProductSearchTopKeywordParam param = apiRequestFactory.createTopKeywordParam(topKeywordParam);
        return RetryUtil.executeWithRetry(() -> apiExecutor.execute(param, accessToken), param.getOceanApiId().getName(), this::refreshAccessToken);
    }

    public ProductTopListQueryResult queryTopList(RankQueryParam rankQueryParam) {
        ProductTopListQueryParam param = apiRequestFactory.createRankQueryParam(rankQueryParam);
        return RetryUtil.executeWithRetry(() -> apiExecutor.execute(param, accessToken), param.getOceanApiId().getName(), this::refreshAccessToken);
    }

    // todo error code 处理
    public CategoryTranslationGetByKeywordResult getCategoryByKeyword(CategorySearchParam categorySearchParam) {
        CategoryTranslationGetByKeywordParam param = apiRequestFactory.createCategorySearchParam(categorySearchParam);
        return RetryUtil.executeWithRetry(() -> apiExecutor.execute(param, accessToken), param.getOceanApiId().getName(), this::refreshAccessToken);
    }

    private void refreshAccessToken() {
        AuthorizationToken token = apiExecutor.refreshToken(secretsManager.getSecret("RefreshToken"));
        String newAccessToken = token.getAccess_token();
        secretsManager.setSecret("AccessToken", newAccessToken); // Update the secrets map with the new access token
        this.accessToken = newAccessToken;
    }
}
