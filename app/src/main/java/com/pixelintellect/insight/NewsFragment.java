package com.pixelintellect.insight;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mancj.materialsearchbar.MaterialSearchBar;
import com.pixelintellect.insight.Adapters.TopHeadlinesRecyclerAdapter;
import com.pixelintellect.insight.utils.AppData;
import com.pixelintellect.insight.utils.Constants;
import com.pixelintellect.insight.utils.DataController;
import com.pixelintellect.insight.utils.models.ArticlesModel;

import org.apache.commons.codec.language.Metaphone;

import java.util.ArrayList;
import java.util.List;

public class NewsFragment extends Fragment {

    /**
     * Creates and returns anew instance of this class
     *
     * @return NewsFragment
     */

    private MaterialSearchBar searchBar;
    private RecyclerView newsRecyclerView;
    private TopHeadlinesRecyclerAdapter RecyclerAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private List<ArticlesModel> articlesModelList = new ArrayList<>();
    private String TAG = getClass().getName();
    private LinearLayout layoutResultsFor;
    private TextView tvResultsFor;

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
        layoutResultsFor = view.findViewById(R.id.layout_results_for);
        tvResultsFor = view.findViewById(R.id.text_view_results_for);

        layoutResultsFor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getJsonData();
                searchBar.setText("");
            }
        });

        getJsonData();

        //Search news with search bar
        searchBar.setOnSearchActionListener(new MaterialSearchBar.OnSearchActionListener() {
            @Override
            public void onSearchStateChanged(boolean enabled) {
                if (!enabled){
                    getJsonData();
                }
            }

            @Override
            public void onSearchConfirmed(CharSequence text) {
                hideKeyBoard();

                if (text != null && text.length() > 0)
                    searchNews(text.toString());
                else
                    Toast.makeText(getContext(), "Nothing to search", Toast.LENGTH_SHORT).show();
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
        layoutResultsFor.setVisibility(View.GONE);
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
                    RecyclerAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(context, intent.getStringExtra(Constants.MESSAGE), Toast.LENGTH_SHORT).show();
                }

                context.unregisterReceiver(this);
            }
        };

        getContext().registerReceiver(receiver, new IntentFilter(Constants.ACTION_NEWS));

        new DataController(getActivity().getSharedPreferences(getActivity().getPackageName(), Context.MODE_PRIVATE))
            .retrieveNewsJson(getContext(), Constants.ACTION_NEWS);


    }

    public void searchNews(String text) {
        // display search query
        tvResultsFor.setText("Showing results for: " + text);
        layoutResultsFor.setVisibility(View.VISIBLE);

        Metaphone metaphone = new Metaphone();
        metaphone.setMaxCodeLen(4);
        String phoneticText = metaphone.encode(text);

        Log.d(TAG, "search meta: " + phoneticText);

        for (int i = 0; i < articlesModelList.size(); i++){
            String[] phoneticInTitle = encode(articlesModelList.get(i).getTitle());

            logEach(phoneticInTitle);

            if (!contains(phoneticInTitle, phoneticText)){
                articlesModelList.remove(i--);
            }
        }

        RecyclerAdapter.notifyDataSetChanged();
    }

    private String[] encode(String s){
        Metaphone metaphone = new Metaphone();
        metaphone.setMaxCodeLen(4);

        String[] words = s.split(" ");
        for (int i = 0; i < words.length ; i++){
            words[i] = metaphone.encode(words[i]);
        }

        return words;
    }

    private boolean contains(String[] arr, String s){
        boolean contained = false;
        for (String a : arr){
            if (a.equals(s)){
                contained = true;
                break;
            }
        }

        return contained;
    }

    private void logEach(String[] arr){
        if (Build.VERSION.SDK_INT >= 26)
            Log.d(TAG, String.join(", ", arr));
    }

    private void hideKeyBoard() {
        try {
            ((InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(getView().getWindowToken(), 0);
        } catch (Exception e) {
            Log.e(this.TAG, e.getMessage());
            e.printStackTrace();
        }
    }
}