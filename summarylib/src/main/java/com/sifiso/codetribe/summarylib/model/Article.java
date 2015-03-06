package com.sifiso.codetribe.summarylib.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by CodeTribe1 on 2015-02-13.
 */
public class Article implements Serializable {
    private int id;
    private String author;
    private String url;
    private String publish_date;
    private String source, source_url, summary, title;
    private String media_type, uri;
    private List<Enclosure> enclosures = new ArrayList<Enclosure>();

    public Article() {
    }

    public Article(int id, String url, String author, String publish_date, String source, String source_url, String summary, String title, String media_type, String uri) {
        this.id = id;
        this.author = author;
        this.publish_date = publish_date;
        this.source = source;
        this.source_url = source_url;
        this.summary = summary;
        this.title = title;
        this.media_type = media_type;
        this.uri = uri;
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMedia_type() {
        return media_type;
    }

    public void setMedia_type(String media_type) {
        this.media_type = media_type;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public List<Enclosure> getEnclosures() {
        return enclosures;
    }

    public void setEnclosures(List<Enclosure> enclosures) {
        this.enclosures = enclosures;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getPublish_date() {
        return publish_date;
    }

    public void setPublish_date(String publish_date) {
        this.publish_date = publish_date;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getSource_url() {
        return source_url;
    }

    public void setSource_url(String source_url) {
        this.source_url = source_url;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


}
