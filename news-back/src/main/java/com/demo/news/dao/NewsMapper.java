package com.demo.news.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.demo.news.dao.model.News;
import org.apache.ibatis.annotations.Param;

public interface NewsMapper extends BaseMapper<News> {

    IPage<News> getNewsList(Page<?> page, @Param("categoryId") String categoryId);

}
