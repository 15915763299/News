package com.demo.common.views;

import com.demo.base.customview.BaseCustomViewModel;
import com.demo.base.utils.ConvertUtils;

/**
 * @author 尉迟涛
 * create time : 2020/3/10 15:21
 * description :
 */
public class NewsTitleVM extends BaseCustomViewModel {
    public String author;
    public String title;
    public String description;
    public String newsUrl;
    public String createTime;
    public String timeDistance;

    public NewsTitleVM(String author, String title, String description, String newsUrl, String createTime) {
        this.author = author;
        this.title = title;
        this.description = description;
        this.newsUrl = newsUrl;
        this.createTime = createTime;
        this.timeDistance = ConvertUtils.pointTime2PassTime(createTime);
    }
}
