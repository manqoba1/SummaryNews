package com.sifiso.codetribe.summarylib.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by CodeTribe1 on 2015-02-13.
 */
public class ResponseData implements Serializable {
    private Category category;
    private Enclosure enclosure;
    private Article article;
    private String description, syndication_url, statusDescription;
    private int statusCode;

    private ArrayList<Category> categories = new ArrayList<Category>();
    private ArrayList<Enclosure> enclosures = new ArrayList<Enclosure>();
    private ArrayList<Article> articles = new ArrayList<Article>();

    public String getStatusDescription() {
        return statusDescription;
    }

    public void setStatusDescription(String statusDescription) {
        this.statusDescription = statusDescription;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSyndication_url() {
        return syndication_url;
    }

    public void setSyndication_url(String syndication_url) {
        this.syndication_url = syndication_url;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

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

    public ArrayList<Category> getCategories() {
        return categories;
    }

    public void setCategories(ArrayList<Category> categories) {
        this.categories = categories;
    }

    public ArrayList<Enclosure> getEnclosures() {
        return enclosures;
    }

    public void setEnclosures(ArrayList<Enclosure> enclosures) {
        this.enclosures = enclosures;
    }

    public ArrayList<Article> getArticles() {
        return articles;
    }

    public void setArticles(ArrayList<Article> articles) {
        this.articles = articles;
    }
}
