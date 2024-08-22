package com.product.api.controller;

import com.alibaba.fenxiao.crossborder.param.CategoryTranslationGetByKeywordResult;
import com.alibaba.fenxiao.crossborder.param.ProductSearchTopKeywordResult;
import com.alibaba.fenxiao.crossborder.param.ProductTopListQueryResult;
import com.product.api.param.CategorySearchParam;
import com.product.api.param.RankQueryParam;
import com.product.api.param.TopKeywordParam;
import com.product.api.service.CategorySearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CategorySearchController {
    private final CategorySearchService categorySearchService;

    @Autowired
    public CategorySearchController(CategorySearchService categorySearchService) {
        this.categorySearchService = categorySearchService;
    }

    /**
     * 商品热搜词
     * com.alibaba.fenxiao.crossborder:product.search.topKeyword-1
     */
    @PostMapping("/topKeyword")
    public ProductSearchTopKeywordResult searchTopKeywords(@RequestBody TopKeywordParam param) {
        return categorySearchService.searchTopKeywords(param);
    }

    /**
     * 查询榜单列表
     * com.alibaba.fenxiao.crossborder:product.search.topKeyword-1
     */
    @PostMapping("/topList")
    public ProductTopListQueryResult queryTopList(@RequestBody RankQueryParam param) {
        return categorySearchService.queryTopList(param);
    }

    /**
    * 根据类目名称查询多语言类目
    * com.alibaba.fenxiao.crossborder:category.translation.getByKeyword-1
     */
    @PostMapping("/category/search")
    public CategoryTranslationGetByKeywordResult getCategoryByKeyword(@RequestBody CategorySearchParam param) {
        return categorySearchService.getCategoryByKeyword(param);
    }
}
