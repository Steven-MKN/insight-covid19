package com.pixelintellect.insight.utils;

import android.net.Uri;

public class LayoutDataClass {

    /**
     *This class sets and gets data that will be used in the Adapter which connects to the recycler view.
     *

     */
    String imageUrL;
    String headline,description,source,publishedDate;


    public LayoutDataClass(String imageUrL, String headline, String description, String source, String publishedDate) {
        this.imageUrL = imageUrL;
        this.headline = headline;
        this.description = description;
        this.source = source;
        this.publishedDate = publishedDate;
    }

    public String getImage() {
        return imageUrL;
    }

    public void setImage(String image) {
        this.imageUrL = image;
    }

    public String getHeadline() {
        return headline;
    }

    public void setHeadline(String headline) {
        this.headline = headline;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getPublishedDate() {
        return publishedDate;
    }

    public void setPublishedDate(String publishedDate) {
        this.publishedDate = publishedDate;
    }
}
