package com.product.api.controller;

import com.product.api.service.ProductSearchService;
import com.alibaba.fenxiao.crossborder.param.ProductSearchKeywordQueryResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProductSearchController {

    private final ProductSearchService productSearchService;

    @Autowired
    public ProductSearchController(ProductSearchService productSearchService) {
        this.productSearchService = productSearchService;
    }

    @GetMapping("/search")
    public ProductSearchKeywordQueryResult searchProducts(@RequestParam String keyword, @RequestParam String accessToken) {
        return productSearchService.searchProductsByKeyword(keyword, accessToken);
    }
}