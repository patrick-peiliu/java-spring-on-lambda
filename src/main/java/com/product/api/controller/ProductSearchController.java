package com.product.api.controller;

import com.alibaba.fenxiao.crossborder.param.ProductImageUploadResult;
import com.alibaba.fenxiao.crossborder.param.ProductSearchImageQueryResult;
import com.alibaba.fenxiao.crossborder.param.ProductSearchQueryProductDetailResult;
import com.product.api.param.ImageQueryParam;
import com.product.api.param.ProductDetailParam;
import com.product.api.param.ProductSearchParam;
import com.product.api.service.ProductSearchService;
import com.alibaba.fenxiao.crossborder.param.ProductSearchKeywordQueryResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class ProductSearchController {

    private final ProductSearchService productSearchService;

    @Autowired
    public ProductSearchController(ProductSearchService productSearchService) {
        this.productSearchService = productSearchService;
    }

    /*
     * 关键词搜索
     * com.alibaba.fenxiao.crossborder:product.search.keywordQuery-1
     * */
    @PostMapping("/search")
    public ProductSearchKeywordQueryResult searchProducts(@RequestBody ProductSearchParam param) {
        return productSearchService.searchProductsByKeyword(param);
    }

    /*
     * 多语言商详
     * com.alibaba.fenxiao.crossborder:product.search.queryProductDetail-1
     * */
    @PostMapping("/detail")
    public ProductSearchQueryProductDetailResult queryProductDetail(@RequestBody ProductDetailParam param) {
        return productSearchService.queryProductDetail(param);
    }
    /*
    *
    * 上传图片获取imageId
    * com.alibaba.fenxiao.crossborder:product.image.upload-1
     * todo
    * 上传图片接口为什么没有imageId返回，或者返回0
    * 请将图片压缩在100k左右，注意base64内容生成后检查下不需要【data:image/jpeg;base64,】这一串，/9后为有效字符
    * */
    @PostMapping("/upload")
    public ProductImageUploadResult productImageUpload(@RequestParam("imageFile") MultipartFile imageFile) {
        return productSearchService.productImageUpload(imageFile);
    }

    /*
     * 多语言图搜
     * com.alibaba.fenxiao.crossborder:product.search.imageQuery-1
     * */
    @PostMapping("/imageQuery")
    public ProductSearchImageQueryResult imageQuery(@RequestBody ImageQueryParam param) {
        return productSearchService.imageQuery(param);
    }
}