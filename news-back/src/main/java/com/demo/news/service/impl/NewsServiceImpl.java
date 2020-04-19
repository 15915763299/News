package com.demo.news.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.demo.news.dao.CategoryMapper;
import com.demo.news.dao.NewsMapper;
import com.demo.news.dao.model.Category;
import com.demo.news.dao.model.News;
import com.demo.news.service.NewsService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author 尉涛
 * @date 2020-03-08 21:39
 **/
@Service("NewsServiceImpl")
public class NewsServiceImpl implements NewsService {

    @Resource
    private CategoryMapper categoryMapper;
    @Resource
    private NewsMapper newsMapper;

    @Override
    public List<Category> getCategory() {
        return categoryMapper.selectByMap(null);
    }

    @Override
    public IPage<News> getNesList(Integer pageSize, Integer pageIndex, String category) {
        Page<News> page = new Page<>(pageIndex, pageSize);
        return newsMapper.getNewsList(page, category);
    }
}
