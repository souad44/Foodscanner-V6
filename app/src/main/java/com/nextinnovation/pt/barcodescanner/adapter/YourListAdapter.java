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

import com.nextinnovation.pt.barcodescanner.R;
import com.nextinnovation.pt.barcodescanner.database.DatabaseHelper;
import com.nextinnovation.pt.barcodescanner.fragment.YourListFragment;
import com.nextinnovation.pt.barcodescanner.model.JsonProduct;
import com.nextinnovation.pt.barcodescanner.model.Product;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

public class YourListAdapter extends RecyclerView.Adapter<YourListAdapter.YourListViewHolder> implements Filterable {

    private ArrayList<JsonProduct> products;
    private ArrayList<JsonProduct> filtredProducts;
    Context context;
    DatabaseHelper db;

    public YourListAdapter(Context context, ArrayList<JsonProduct> products) {
        this.products = products;
        this.context = context;
    }


    @NonNull
    @Override
    public YourListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view  = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_favoris,parent,false);

        return new YourListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull YourListViewHolder holder, int position) {

        holder.name.setText(products.get(position).getProduct().getLabels());
        holder.fournisseur.setText(products.get(position).getProduct().getBrands());
        String grade = products.get(position).getProduct().getNutriscore_grade();
        if(grade!=null){
            nutriscorefct(grade,holder);}
        else{
        }

        holder.photo.setAnimation(AnimationUtils.loadAnimation(context, R.anim.fade_transition_animation));
        holder.container.setAnimation(AnimationUtils.loadAnimation(context, R.anim.fade_scale_animation));
        Picasso.with(context).load(products.get(position).getProduct().getProduct_image()).into(holder.photo);

    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public int getItemCount() {
        return products.size();
    }


    public void clear() {
        int size = products.size();
        products.clear();
        notifyItemRangeRemoved(0, size);
    }


    //file search result
    public void filterResult(ArrayList<JsonProduct> newProcucts){
        products=new ArrayList<>();
        products.addAll(newProcucts);
        notifyDataSetChanged();
    }

    public class YourListViewHolder extends RecyclerView.ViewHolder {

        ImageView photo,nutriScore;
        TextView name, fournisseur;
        RelativeLayout container;

        public YourListViewHolder(@NonNull View itemView) {
            super(itemView);

            photo = itemView.findViewById(R.id.img_produit);
            nutriScore = itemView.findViewById(R.id.nutri_scrore_img);
            container = itemView.findViewById(R.id.container);
            name =  itemView.findViewById(R.id.name);
            fournisseur = itemView.findViewById(R.id.fournisseur);

        }

    }

    @Override
    public Filter getFilter() {

        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {

                String Key = constraint.toString();
                if (Key.isEmpty()) {

                    filtredProducts = products ;

                }
                else {
                    ArrayList<JsonProduct> lstFiltered = new ArrayList<>();
                    for (JsonProduct row : products) {

                        if (row.getProduct().getLabels().toLowerCase().contains(Key.toLowerCase())){
                            lstFiltered.add(row);
                        }
                    }
                    filtredProducts = lstFiltered;

                }


                FilterResults filterResults = new FilterResults();
                filterResults.values= filterResults;
                return filterResults;

            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                filtredProducts = (ArrayList<JsonProduct>) results.values;
                notifyDataSetChanged();

            }
        };

    }

    public void nutriscorefct(String grade, YourListViewHolder holder){
        switch(grade){
            case  "a" :{
                holder.nutriScore.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.nnc_small_a));
                break;
            }
            case  "b" :{
                holder.nutriScore.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.nnc_small_b));
                break;
            }
            case  "c" :{
                holder.nutriScore.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.nnc_small_c));
                break;
            }
            case  "d" :{
                holder.nutriScore.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.nnc_small_d));
                break;
            }
            case  "e" :{
                holder.nutriScore.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.nnc_small_e));
                break;
            }
        }

    }

}

