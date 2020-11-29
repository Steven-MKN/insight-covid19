package com.pixelintellect.insight.utils.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ArticlesModel {
    @SerializedName("source")
    @Expose
    private  SourceModel source;

    @SerializedName("author")
    @Expose
    private  String author;

    @SerializedName("title")
    @Expose
    private  String title;

    @SerializedName("description")
    @Expose
    private  String discription;

    @SerializedName("url")
    @Expose String url;


    @SerializedName("urlToImage")
    @Expose
    private  String urlToImage;

    @SerializedName("published")
    @Expose
    private  String published;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }


    public SourceModel getSource() {
        return source;
    }

    public void setSource(SourceModel source) {
        this.source = source;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDiscription() {
        return discription;
    }

    public void setDiscription(String discription) {
        this.discription = discription;
    }

    public String getUrlToImage() {
        return urlToImage;
    }

    public void setUrlToImage(String urlToImage) {
        this.urlToImage = urlToImage;
    }

    public String getPublished() {
        return published;
    }

    public void setPublished(String published) {
        this.published = published;
    }
}
