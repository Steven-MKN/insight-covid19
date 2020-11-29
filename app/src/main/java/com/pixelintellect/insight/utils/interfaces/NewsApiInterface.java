package com.pixelintellect.insight.utils.interfaces;

import com.pixelintellect.insight.utils.models.NewsModel;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface NewsApiInterface {
    //get news top headlines
    @GET("top-headlines")
    Call<NewsModel> getTopHeadlines(


            @Query("apiKey") String apiKey,
             @Query("q") String query,
            @Query("country") String country


    );

    //get everything
    @GET("everything")
    Call<NewsModel> getNews(
            @Query("q") String query,

            @Query("apiKey") String apiKey


    );

}
