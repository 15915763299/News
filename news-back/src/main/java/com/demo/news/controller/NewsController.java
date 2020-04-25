package com.demo.news.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.demo.news.dao.model.Category;
import com.demo.news.dao.model.News;
import com.demo.news.entity.NewsListRes;
import com.demo.news.entity.base.BaseRes;
import com.demo.news.service.NewsService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.hibernate.validator.constraints.Length;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author 尉迟涛
 * <p>
 * BaseRes 在这一层封装，Service层专注于获取结果
 * 这里的BaseRes都返回成功，如果异常，Service层抛出自定义异常
 * <p>
 * 按照REST风格定义API地址
 */
@RestController
@RequestMapping("/news")
public class NewsController {

    @Resource(name = "NewsServiceImpl")
    private NewsService newsService;

    @ApiOperation(value = "获取分类目录")
    @RequestMapping(value = "/categories", method = RequestMethod.GET)
    @ResponseBody
    public BaseRes<List<Category>> getCategories() throws Exception {
        List<Category> categories = newsService.getCategory();
        return new BaseRes<>(categories);
    }

    @ApiOperation(value = "获取新闻列表", response = NewsListRes.class)
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "pageSize", value = "页长", required = true),
            @ApiImplicitParam(paramType = "query", name = "pageIndex", value = "页码", required = true),
            @ApiImplicitParam(paramType = "query", name = "categoryId", value = "分类编号")
    })
    @RequestMapping(value = "/newsPage", method = RequestMethod.GET)
    @ResponseBody
    public BaseRes<IPage<News>> getNewsPage(
            @RequestParam("pageSize") @NotNull(message = "【页长】不能为空") @Min(value = 1, message = "【页长】需大于零") Integer pageSize,
            @RequestParam("pageIndex") @NotNull(message = "【页码】不能为空") @Min(value = 0, message = "【页码】需大于等于零") Integer pageIndex,
            @RequestParam(value = "categoryId", required = false) @Length(max = 32, message = "【分类编号】长度不能超过【32】") String categoryId) throws Exception {
        IPage<News> iPage = newsService.getNesList(pageSize, pageIndex, categoryId);
        return new BaseRes<>(iPage);
    }
}
