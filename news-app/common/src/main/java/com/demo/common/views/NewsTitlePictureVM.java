package com.demo.common.views;

import com.demo.base.customview.BaseCustomViewModel;
import com.demo.base.utils.ConvertUtils;

/**
 * @author 尉迟涛
 * create time : 2020/3/10 15:00
 * description :
 */
public class NewsTitlePictureVM extends BaseCustomViewModel {

    public String author;
    public String title;
    public String description;
    public String newsUrl;
    public String imageUrl;
    public String createTime;
    public String timeDistance;

    public NewsTitlePictureVM(String author, String title, String description, String newsUrl, String imageUrl, String createTime) {
        this.author = author;
        this.title = title;
        this.description = description;
        this.newsUrl = newsUrl;
        this.imageUrl = imageUrl;
        this.createTime = createTime;
        this.timeDistance = ConvertUtils.pointTime2PassTime(createTime);
    }
}
