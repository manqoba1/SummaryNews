package com.sifiso.codetribe.summarylib.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by CodeTribe1 on 2015-02-13.
 */
public class Category implements Serializable {
    private Integer category_id;
    private String display_category_name, english_category_name, url_category_name;
    private List<Article> articles = new ArrayList<Article>();

    public Category(Integer category_id, String display_category_name, String english_category_name, String url_category_name) {
        this.category_id = category_id;
        this.display_category_name = display_category_name;
        this.english_category_name = english_category_name;
        this.url_category_name = url_category_name;
    }

    public Category() {
    }

    public List<Article> getArticles() {
        return articles;
    }

    public void setArticles(List<Article> articles) {
        this.articles = articles;
    }

    public Integer getCategory_id() {
        return category_id;
    }

    public void setCategory_id(Integer category_id) {
        this.category_id = category_id;
    }

    public String getDisplay_category_name() {
        return display_category_name;
    }

    public void setDisplay_category_name(String display_category_name) {
        this.display_category_name = display_category_name;
    }

    public String getEnglish_category_name() {
        return english_category_name;
    }

    public void setEnglish_category_name(String english_category_name) {
        this.english_category_name = english_category_name;
    }

    public String getUrl_category_name() {
        return url_category_name;
    }

    public void setUrl_category_name(String url_category_name) {
        this.url_category_name = url_category_name;
    }
}
