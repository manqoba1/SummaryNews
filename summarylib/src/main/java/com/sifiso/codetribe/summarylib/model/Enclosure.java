package com.sifiso.codetribe.summarylib.model;

import java.io.Serializable;

/**
 * Created by CodeTribe1 on 2015-02-13.
 */
public class Enclosure implements Serializable {
    private String media_type, uri;
    private int file_length;

    public Enclosure() {
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

    public int getFile_length() {
        return file_length;
    }

    public void setFile_length(int file_length) {
        this.file_length = file_length;
    }
}
