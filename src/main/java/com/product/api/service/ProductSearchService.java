package com.product.api.service;

import com.alibaba.fenxiao.crossborder.param.*;
import com.alibaba.ocean.rawsdk.ApiExecutor;
import com.alibaba.ocean.rawsdk.client.entity.AuthorizationToken;
import com.product.api.factory.ApiRequestFactory;
import com.product.api.param.ImageQueryParam;
import com.product.api.param.ProductDetailParam;
import com.product.api.param.ProductSearchParam;
import com.product.api.util.ImageCompressor;
import com.product.api.util.RetryUtil;
import com.product.api.util.SecretsManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;
import org.apache.commons.lang3.StringUtils;


/**
 * 商品搜索服务类
 * This class is used to search for products.
 */
@Service
public class ProductSearchService {
    private static final Logger LOG = LogManager.getLogger();

    private static final double ONE_KB = 1024.0;
    private static final float ONE_HUNDRED_KB = 100 * 1024.0f;

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

    // 线上返回图片失败
    // todo 压缩算法
    // lambda send base64 instead.
    public ProductImageUploadResult productImageUpload(MultipartFile imageFile) {
        String base64Image = "";

        //        try {
//            byte[] imageBytes = imageFile.getBytes();
//            base64Image = Base64.getEncoder().encodeToString(imageBytes);
//            LOG.info("Base64 Image: {}", base64Image); // print the base64 image
//            double sizeBeforeCompressInKB = Base64.getEncoder().encodeToString(imageBytes).length() / ONE_KB;
//
////            base64Image = Base64.getEncoder().encodeToString(imageBytes);
////            double imageBytesSizeInKB = imageBytes.length / ONE_KB;
//
////            float quality = Math.max(0.0f, Math.min(1.0f, ONE_HUNDRED_KB / (float) imageBytes.length));
//            base64Image = ImageCompressor.compressImageToBase64(imageFile, 0.5f);
//
//            // Convert the size to kilobytes (KB)
//            double base64ImageSizeInKB = base64Image.length() / ONE_KB;
//            // Log the size in KB
//            LOG.info("Compressed Image from : {} KB to: {} KB", sizeBeforeCompressInKB, base64ImageSizeInKB);
//
//            // compress the base64 String by 30%
//            if (base64ImageSizeInKB > 100) {
//                double len = base64Image.length() / ONE_KB;
//                base64Image = ImageCompressor.compressBase64String(base64Image);
//                LOG.info("Compressed base64 from : {} KB to: {} KB", len, base64Image.length() / ONE_KB);
//            }
//        } catch (IOException e) {
//            throw new RuntimeException("Failed to convert image file to base64", e);
//        }

        ProductImageUploadParam param = apiRequestFactory.createImageUploadParam(base64Image);
        return RetryUtil.executeWithRetry(() -> apiExecutor.execute(param, accessToken), param.getOceanApiId().getName(), this::refreshAccessToken);
    }

    public ProductImageUploadResult productImageUpload(String base64Image) {
        ProductImageUploadParam param = apiRequestFactory.createImageUploadParam(base64Image);
        return RetryUtil.executeWithRetry(() -> apiExecutor.execute(param, accessToken), param.getOceanApiId().getName(), this::refreshAccessToken);
    }

    public ProductSearchImageQueryResult uploadImageAndQuery(ImageQueryParam param) {
        // Step 1: Upload the image and get the image ID
        ProductImageUploadResultResultModel uploadResult = productImageUpload(param.getBase64Image()).getResult();
        ImageQueryParam imageQueryParam = new ImageQueryParam();
        imageQueryParam.setBeginPage(param.getBeginPage());
        imageQueryParam.setPageSize(param.getPageSize());
        imageQueryParam.setCountry(param.getCountry());
        String imageId = uploadResult.getResult();
        // Step 2: Check if the image ID is valid
        if (StringUtils.isNotEmpty(imageId) && !imageId.equals("0")) {
            // Set the image ID to ImageQueryParam
            imageQueryParam.setImageId(imageId);
        } else {
            // Throw an exception to inform the upload image fails
            throw new RuntimeException("Please upload an image less than 100KB");
        }

        // Step 3: Query the image
        return imageQuery(imageQueryParam);
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