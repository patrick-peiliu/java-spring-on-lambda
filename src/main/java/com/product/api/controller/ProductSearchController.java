package com.product.api.controller;

import com.product.api.param.ProductSearchParam;
import com.product.api.service.ProductSearchService;
import com.alibaba.fenxiao.crossborder.param.ProductSearchKeywordQueryResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class ProductSearchController {

    private final ProductSearchService productSearchService;

    @Autowired
    public ProductSearchController(ProductSearchService productSearchService) {
        this.productSearchService = productSearchService;
    }

    @PostMapping("/search")
    public ProductSearchKeywordQueryResult searchProducts(@RequestBody ProductSearchParam param) {
        return productSearchService.searchProductsByKeyword(param);
    }

    /*
     * todo
     * 多语言商详
     * product.search.queryProductDetail
     * */

    /*
    * todo
    * 上传图片获取imageId
    * com.alibaba.fenxiao.crossborder:product.image.upload-1
    * */

    /*
     * todo
     * 多语言图搜
     * com.alibaba.fenxiao.crossborder:product.search.imageQuery-1
     * */
}