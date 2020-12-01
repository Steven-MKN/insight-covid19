package com.pixelintellect.insight;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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
import com.pixelintellect.insight.utils.AppData;
import com.pixelintellect.insight.utils.Constants;
import com.pixelintellect.insight.utils.DataController;
import com.pixelintellect.insight.utils.models.ArticlesModel;
import com.pixelintellect.insight.utils.models.NewsModel;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewsFragment extends Fragment {

    /**
     * Creates and returns anew instance of this class
     *
     * @return NewsFragment
     */

    MaterialSearchBar searchBar;
    RecyclerView newsRecyclerView;
    TopHeadlinesRecyclerAdapter RecyclerAdapter;
    SwipeRefreshLayout swipeRefreshLayout;

    List<ArticlesModel> articlesModelList = new ArrayList<>();

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

        getJsonData();

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
                        getJsonData();
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
                getJsonData();
            }
        });

        return view;
    }

    public void getJsonData() {
        swipeRefreshLayout.setRefreshing(true);

        BroadcastReceiver receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getBooleanExtra(Constants.IS_SUCCESSFUL, false)) {
                    swipeRefreshLayout.setRefreshing(false);
                    articlesModelList.clear();
                    articlesModelList = AppData.getInstance().getNewsModel().getArticles();
                    RecyclerAdapter = new TopHeadlinesRecyclerAdapter(getContext(), articlesModelList);
                    newsRecyclerView.setHasFixedSize(true);
                    newsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
                    newsRecyclerView.setAdapter(RecyclerAdapter);
                } else {
                    Toast.makeText(context, intent.getStringExtra(Constants.MESSAGE), Toast.LENGTH_SHORT).show();
                }
            }
        };

        getContext().registerReceiver(receiver, new IntentFilter(Constants.ACTION_NEWS));

        new DataController(getActivity().getSharedPreferences(getActivity().getPackageName(), Context.MODE_PRIVATE))
            .retrieveNewsJson(getContext(), Constants.ACTION_NEWS);


    }

    public void searchNews() {

    }

}