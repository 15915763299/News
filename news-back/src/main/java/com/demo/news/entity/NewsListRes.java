package com.demo.news.entity;

import com.demo.news.dao.model.News;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.util.List;

/**
 * @author 尉涛
 * @date 2020-03-08 20:48
 **/
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)//为null的字段 不输出到前端 看以下的stuAge字段
@ApiModel(description = "新闻列表返回信息")
public class NewsListRes {

    private List<News> newses;
    private Integer totalCount;

}
