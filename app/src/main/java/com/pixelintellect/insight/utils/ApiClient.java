package com.pixelintellect.insight.utils;

import com.pixelintellect.insight.utils.interfaces.NewsApiInterface;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

//this a singleton class for the retrofit client which calls the api and handles the results

public class ApiClient {
    private static final String baseUrl = "https://stevenmokoena.co.za/insight/";
    private static ApiClient apiClient;
    private static Retrofit retrofit;

    //create Api client
    private ApiClient() {
        buildRetrofit();
    }

    //    retrieve data from json file with retrofit
    private Retrofit buildRetrofit() {
        OkHttpClient.Builder client = new OkHttpClient.Builder();
        retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl).addConverterFactory(GsonConverterFactory.create()).
                        client(client.build()).build();

        return retrofit;
    }


  public  static synchronized  ApiClient getInstance(){
        if(apiClient ==null){
            apiClient = new ApiClient();
        }

        return apiClient;
  }

  public NewsApiInterface getApi(){
        return  retrofit.create(NewsApiInterface.class);
  }


}
