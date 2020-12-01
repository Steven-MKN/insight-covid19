package com.pixelintellect.insight.utils.interfaces;

import com.pixelintellect.insight.utils.models.NewsModel;

import retrofit2.Call;
import retrofit2.http.GET;

public interface NewsApiInterface {

    @GET("news.json")
    Call<NewsModel> getCached();

}
