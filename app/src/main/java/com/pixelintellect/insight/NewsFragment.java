package com.pixelintellect.insight;

import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mancj.materialsearchbar.MaterialSearchBar;
import com.pixelintellect.insight.Adapters.TopHeadlinesRecyclerAdapter;
import com.pixelintellect.insight.utils.LayoutDataClass;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;

import java.util.ArrayList;

public class NewsFragment extends Fragment {

    /**
     * Creates and returns anew instance of this class
     * @return NewsFragment
     */

    MaterialSearchBar searchBar;
    RecyclerView newsRecyclerView;
    TopHeadlinesRecyclerAdapter RecyclerAdapter;
    Picasso picasso;
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
        LoadRecyclerView();
        return view;
    }

    public void LoadRecyclerView(){
        newsRecyclerView.hasFixedSize();
        newsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false));

        ArrayList<LayoutDataClass> list = new ArrayList<>();
   String uri = "https://images.pexels.com/photos/3952231/pexels-photo-3952231.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500";

    ;
        //hardcoded values for design purspose ,will be different when we use API
        list.add( new LayoutDataClass(uri,"Breaking News ","Lorem ipsum dolor sit amet, consectetur adipiscing elit. Quisque nisl erospulvinar facilisis justo mollis, auctor consequat urna. Morbi a bibendum metus.","Dev ted","27-11-2020"));
        list.add( new LayoutDataClass(uri,"Trending News ","Lorem ipsum dolor sit amet, consectetur adipiscing elit. Quisque nisl erospulvinar facilisis justo mollis, auctor consequat urna. Morbi a bibendum metus.","Dev ted","27-11-2020"));
        list.add( new LayoutDataClass(uri,"Top News ","Lorem ipsum dolor sit amet, consectetur adipiscing elit. Quisque nisl erospulvinar facilisis justo mollis, auctor consequat urna. Morbi a bibendum metus.","Dev ted","27-11-2020"));


 //pass arraylist to adapter
        RecyclerAdapter = new TopHeadlinesRecyclerAdapter(list);
        newsRecyclerView.setAdapter(RecyclerAdapter);
    }
}