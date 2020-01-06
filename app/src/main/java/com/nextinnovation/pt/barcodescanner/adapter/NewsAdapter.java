package com.nextinnovation.pt.barcodescanner.adapter;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nextinnovation.pt.barcodescanner.R;
import com.nextinnovation.pt.barcodescanner.database.DatabaseHelper;
import com.nextinnovation.pt.barcodescanner.model.Product;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsViewHolder> implements Filterable {


    Context mContext;
    List<Product> mData ;
    List<Product> mDataFiltered ;
    boolean isDark = false;
    DatabaseHelper db;

    public NewsAdapter(Context mContext, List<Product> mData, boolean isDark) {
        this.mContext = mContext;
        this.mData = mData;
        this.isDark = isDark;
        this.mDataFiltered = mData;
    }

    public NewsAdapter(Context mContext, List<Product> mData) {
        this.mContext = mContext;
        this.mData = mData;
        this.mDataFiltered = mData;

    }
    public void updateData(List<Product> mList, boolean isDark){
        this.mData = mList;
        this.isDark = isDark;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public NewsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View layout;
        layout = LayoutInflater.from(mContext).inflate(R.layout.item_news,viewGroup,false);
        return new NewsViewHolder(layout);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsViewHolder newsViewHolder, int position) {

        newsViewHolder.img_user.setAnimation(AnimationUtils.loadAnimation(mContext,R.anim.fade_transition_animation));
        newsViewHolder.container.setAnimation(AnimationUtils.loadAnimation(mContext,R.anim.fade_scale_animation));

        db = new DatabaseHelper(this.mContext);
        Long data;
        newsViewHolder.tv_title.setText(mDataFiltered.get(position).getLabels());
        Cursor date = db.getDate(mData.get(position));
date.moveToLast();
                data = date.getLong(date.getColumnIndex("created_at"));
                String dateFormat = db.getFormattedDate(data);
                newsViewHolder.tv_date.setText(dateFormat);

        date.close();
        Picasso.with(mContext).load(mDataFiltered.get(position).getProduct_image()).into(newsViewHolder.img_user);

    }

    @Override
    public int getItemCount() {
        return mDataFiltered.size();
    }

    @Override
    public Filter getFilter() {

        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {

                String Key = constraint.toString();
                if (Key.isEmpty()) {

                    mDataFiltered = mData ;

                }
                else {
                    List<Product> lstFiltered = new ArrayList<>();
                    for (Product row : mData) {

                        if (row.getLabels().toLowerCase().contains(Key.toLowerCase())){
                            lstFiltered.add(row);
                        }

                    }

                    mDataFiltered = lstFiltered;

                }


                FilterResults filterResults = new FilterResults();
                filterResults.values= mDataFiltered;
                return filterResults;

            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {


                mDataFiltered = (List<Product>) results.values;
                notifyDataSetChanged();

            }
        };




    }

    public class NewsViewHolder extends RecyclerView.ViewHolder {



        TextView tv_title,tv_content,tv_date;
        ImageView img_user;
        RelativeLayout container;





        public NewsViewHolder(@NonNull View itemView) {
            super(itemView);
            container = itemView.findViewById(R.id.container);
            tv_title = itemView.findViewById(R.id.tv_title);
            tv_content = itemView.findViewById(R.id.tv_description);
            tv_date = itemView.findViewById(R.id.tv_date);
            img_user = itemView.findViewById(R.id.img_user);


            if (isDark) {
                setDarkTheme();
            }



        }


        private void setDarkTheme() {

            container.setBackgroundResource(R.drawable.card_bg_dark);

        }



    }
}
