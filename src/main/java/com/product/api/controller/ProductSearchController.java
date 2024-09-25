package com.product.api.controller;

import com.alibaba.fenxiao.crossborder.param.*;
import com.product.api.model.ApiResponse;
import com.product.api.param.ImageQueryParam;
import com.product.api.param.ProductDetailParam;
import com.product.api.param.ProductRecommendParam;
import com.product.api.param.ProductSearchParam;
import com.product.api.service.ProductSearchService;
import com.product.api.service.S3Service;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class ProductSearchController {
    private static final Logger LOG = LogManager.getLogger();

    private final ProductSearchService productSearchService;
    private final S3Service s3Service;

    @Autowired
    public ProductSearchController(ProductSearchService productSearchService, S3Service s3Service) {
        this.productSearchService = productSearchService;
        this.s3Service = s3Service;
    }

    /**
     * 关键词搜索
     * com.alibaba.fenxiao.crossborder:product.search.keywordQuery-1
     */
    @PostMapping("/search")
    public ProductSearchKeywordQueryResult searchProducts(@RequestBody ProductSearchParam param) {
        return productSearchService.searchProductsByKeyword(param);
    }

    /**
     * 多语言商详
     * com.alibaba.fenxiao.crossborder:product.search.queryProductDetail-1
     */
    @PostMapping("/detail")
    public ProductSearchQueryProductDetailResult queryProductDetail(@RequestBody ProductDetailParam param) {
        return productSearchService.queryProductDetail(param);
    }

    /**
     * 多语言图搜(通过1688url或者1688imageId)
     * com.alibaba.fenxiao.crossborder:product.search.imageQuery-1
     */
    @PostMapping("/imageQuery")
    public ProductSearchImageQueryResult imageQuery(@RequestBody ImageQueryParam param) {
        return productSearchService.imageQuery(param);
    }

    /**
     * 上传图片获取imageId
     * com.alibaba.fenxiao.crossborder:product.image.upload-1
     * 上传图片接口为什么没有imageId返回，或者返回0
     * 请将图片压缩在100k左右，注意base64内容生成后检查下不需要【data:image/jpeg;base64,】这一串，/9后为有效字符
     */
    @PostMapping("/upload")
    public ProductImageUploadResult uploadImageAndQuery(@RequestBody ImageQueryParam param) {
        return productSearchService.uploadImageAndQueryImageId(param);
    }

    /**
     * 用户商品推荐
     * com.alibaba.fenxiao.crossborder:product.search.offerRecommend-1
     */
    @PostMapping("/recommend")
    public ProductSearchOfferRecommendResult productRecommend(@RequestBody ProductRecommendParam param) {
        return productSearchService.searchRecommendProduct(param);
    }

    @PostMapping("/fileUpload")
    public ResponseEntity<ApiResponse<String>> uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            String fileUrl = s3Service.uploadFile(file);
            ApiResponse<String> response = new ApiResponse<>(true, "200", null, fileUrl);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            LOG.error("Error uploading file to S3", e);
            ApiResponse<String> errorResponse = new ApiResponse<>(false, "500", "Error uploading file", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
}