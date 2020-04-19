package com.demo.news.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.demo.news.dao.model.Category;
import com.demo.news.dao.model.News;

import java.util.List;

/**
 * @author 尉涛
 * @date 2020-03-08 20:44
 **/
public interface NewsService {

    List<Category> getCategory();

    IPage<News> getNesList(Integer pageSize, Integer pageIndex, String category);

}
