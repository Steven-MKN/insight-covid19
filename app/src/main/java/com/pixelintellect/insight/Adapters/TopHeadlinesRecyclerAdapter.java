package com.pixelintellect.insight.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textview.MaterialTextView;
import com.makeramen.roundedimageview.RoundedImageView;
import com.pixelintellect.insight.R;
import com.pixelintellect.insight.WebViewActivity;
import com.pixelintellect.insight.utils.LayoutDataClass;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class TopHeadlinesRecyclerAdapter extends RecyclerView.Adapter<TopHeadlinesRecyclerAdapter.RecyclerViewHolder> {

    //create constructor that gets values from the LayoutDataClass
    Context context;
    ArrayList<LayoutDataClass> layoutDataClassArrayList; //gets the variables in the LayoutDataClass

    public TopHeadlinesRecyclerAdapter(ArrayList<LayoutDataClass> LayoutDataClass) {
        this.layoutDataClassArrayList = LayoutDataClass;
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
    public void onBindViewHolder(@NonNull RecyclerViewHolder holder, int position) {
//       this method binds the design and code
        LayoutDataClass dataClass = layoutDataClassArrayList.get(position);

        //now set values to elements in the card layouts
        String imgUrl = dataClass.getImage();
        Picasso.get().load("https://images.pexels.com/photos/3952231/pexels-photo-3952231.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500").into(holder.imageView);
        Drawable drawable = holder.imageView.getDrawable();
        holder.imageView.setBackground(drawable);

        holder.headline.setText(dataClass.getHeadline());

        holder.description.setText(dataClass.getDescription());
        holder.source.setText(dataClass.getSource());
        holder.publishedDate.setText(dataClass.getPublishedDate());





    }

    @Override
    public int getItemCount() {
        //return the size of the array list
        return layoutDataClassArrayList.size();
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
            news_card = itemView.findViewById(R.id.news_card);


        }
    }
}
