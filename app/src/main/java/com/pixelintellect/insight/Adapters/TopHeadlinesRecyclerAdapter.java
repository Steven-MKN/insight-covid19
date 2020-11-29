package com.pixelintellect.insight.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textview.MaterialTextView;
import com.makeramen.roundedimageview.RoundedImageView;
import com.pixelintellect.insight.R;
import com.pixelintellect.insight.WebViewActivity;
import com.pixelintellect.insight.utils.LayoutDataClass;
import com.pixelintellect.insight.utils.models.ArticlesModel;
import com.squareup.picasso.Picasso;

import org.ocpsoft.prettytime.PrettyTime;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class TopHeadlinesRecyclerAdapter extends RecyclerView.Adapter<TopHeadlinesRecyclerAdapter.RecyclerViewHolder> {

    private static final String TAG = "prettyTime";
    Context context;
    List<ArticlesModel> articlesModelList;

    //create constructor that gets values from the ArticlesModelClass
    public TopHeadlinesRecyclerAdapter(Context context, List<ArticlesModel> getArticles) {

        this.context = context;
        this.articlesModelList = getArticles;
    }


    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        //get the elements from the top_headlines_card layout
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.top_headlines_card, parent, false);
        RecyclerViewHolder viewHolder = new RecyclerViewHolder(view);
        return viewHolder;
    }


    @Override
    //  this method binds the design and code
    public void onBindViewHolder(@NonNull RecyclerViewHolder holder, int position) {


        //now set values to elements in the card layouts
        final ArticlesModel articlesModel = articlesModelList.get(position);
        String imgUrl = articlesModel.getUrlToImage();
        String webUrl = articlesModel.getUrl();
        Picasso.get().load(imgUrl).into(holder.imageView);
        Drawable drawable = holder.imageView.getDrawable();
        holder.imageView.setBackground(drawable);
        holder.headline.setText(articlesModel.getTitle());
        holder.description.setText(articlesModel.getDiscription());
        holder.source.setText(articlesModel.getSource().getName());
        holder.publishedDate.setText(articlesModel.getPublished());

        //open and pass data to webview
        holder.news_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, WebViewActivity.class);
                intent.putExtra("url", articlesModel.getUrl());
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        //return the size of the array list
        return articlesModelList.size();
    }

    public static class RecyclerViewHolder extends RecyclerView.ViewHolder {
        //attributes
        RoundedImageView imageView;
        MaterialTextView headline, description, source, publishedDate;
        MaterialCardView news_card;


        public RecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
            //create hooks to get the elements from the card layouts
            imageView = itemView.findViewById(R.id.article_image);
            headline = itemView.findViewById(R.id.tv_top_headlines);
            description = itemView.findViewById(R.id.tv_description);
            source = itemView.findViewById(R.id.tv_source);
            publishedDate = itemView.findViewById(R.id.tv_date);
            news_card = itemView.findViewById(R.id.top_headlines_card);


        }
    }


}

