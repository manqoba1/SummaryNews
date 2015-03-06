package com.sifiso.codetribe.summarylib.model;

import java.io.Serializable;

/**
 * Created by CodeTribe1 on 2015-02-13.
 */
public class RequestData implements Serializable {
    private Category category;
    private Enclosure enclosure;
    private Article article;
    private int categoryID;
    private String categoryURL;
    private String articleByCategory;

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Enclosure getEnclosure() {
        return enclosure;
    }

    public void setEnclosure(Enclosure enclosure) {
        this.enclosure = enclosure;
    }

    public Article getArticle() {
        return article;
    }

    public void setArticle(Article article) {
        this.article = article;
    }

    public int getCategoryID() {
        return categoryID;
    }

    public void setCategoryID(int categoryID) {
        this.categoryID = categoryID;
    }


    public String getCategoryURL() {
        return categoryURL;
    }

    public void setCategoryURL() {
        this.categoryURL = "http://api.feedzilla.com/v1/categories.json";
    }

    public String getArticleByCategory() {
        return articleByCategory;
    }

    public void setArticleByCategory(int categoryID, String searcher) {
        if (categoryID > 0) {
            this.articleByCategory = "http://api.feedzilla.com/v1/categories/" + categoryID + "/articles.json";
        }
        if (categoryID > 0 && searcher != null) {
            this.articleByCategory = "http://api.feedzilla.com/v1/categories/" + categoryID + "/articles/search.json?q=" + searcher;
        }
        if (searcher != null) {
            this.articleByCategory = "http://api.feedzilla.com/v1/articles/search.json?q=" + searcher;
        }
    }


}
