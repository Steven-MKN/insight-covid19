package com.pixelintellect.insight;

import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.mancj.materialsearchbar.MaterialSearchBar;
import com.pixelintellect.insight.Adapters.TopHeadlinesRecyclerAdapter;
import com.pixelintellect.insight.utils.ApiClient;
import com.pixelintellect.insight.utils.LayoutDataClass;
import com.pixelintellect.insight.utils.models.ArticlesModel;
import com.pixelintellect.insight.utils.models.NewsModel;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewsFragment extends Fragment {

    /**
     * Creates and returns anew instance of this class
     * @return NewsFragment
     */

    MaterialSearchBar searchBar;
    RecyclerView newsRecyclerView;
    TopHeadlinesRecyclerAdapter RecyclerAdapter;
    SwipeRefreshLayout swipeRefreshLayout;

    List<ArticlesModel> articlesModelList = new ArrayList<>();
    String API_KEY ,Query,Country,Url ;
//    final String api ="bdfd515e9a814f8984b5bf9be576d792";
    public static NewsFragment newInstance() {


        return new NewsFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_news, container, false);
        newsRecyclerView = view.findViewById(R.id.top_headlines_recycler);
        searchBar = view.findViewById(R.id.searchBar);
        swipeRefreshLayout = view.findViewById(R.id.refreshLayout);
        API_KEY = getResources().getString(R.string.API_KEY); //get api key from the string resources
        Country = getCountry();
        Query = "COVID";

        getJsonData(API_KEY,Query,"");

        //Search news with search bar

        searchBar.setOnSearchActionListener(new MaterialSearchBar.OnSearchActionListener() {
            @Override
            public void onSearchStateChanged(boolean enabled) {

            }

            @Override
            public void onSearchConfirmed(CharSequence text) {
                    searchNews();
                swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        getJsonData(API_KEY,Query,"");
                    }
                });
            }

            @Override
            public void onButtonClicked(int buttonCode) {

            }
        });

        //Refresh news feed and get new data
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getJsonData(API_KEY,Query,"");
            }
        });
        return view;
    }

    public void getJsonData(String api_key ,String q ,String country){

        Call<NewsModel> request;
        swipeRefreshLayout.setRefreshing(true);
       request = ApiClient.getInstance().getApi().getTopHeadlines(API_KEY,Query,"");
       request.enqueue(new Callback<NewsModel>() {
           @Override
           public void onResponse(Call<NewsModel> call, Response<NewsModel> response) {
               assert response.body() != null;
               if(response.isSuccessful() && response.body().getArticles() !=null){
                   swipeRefreshLayout.setRefreshing(false);
                   articlesModelList.clear();
                   articlesModelList = response.body().getArticles();
                   RecyclerAdapter = new TopHeadlinesRecyclerAdapter(getContext(),articlesModelList);
                   newsRecyclerView.setHasFixedSize(true);
                   newsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
                   newsRecyclerView.setAdapter(RecyclerAdapter);

               }
           }

           @Override
           public void onFailure(Call<NewsModel> call, Throwable t) {
               swipeRefreshLayout.setRefreshing(false);
               Toast.makeText(getContext(), t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
           }
       });



    }

    public  void searchNews(){
        Query = searchBar.getText();

        getJsonData(API_KEY,Query,"");
        searchBar.closeSearch();

    }

    public String getCountry() {
        Locale locale = Locale.getDefault();
        String country = locale.getCountry();
        return country.toLowerCase();

    }
}