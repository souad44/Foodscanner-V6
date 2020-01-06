 package com.nextinnovation.pt.barcodescanner.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.nextinnovation.pt.barcodescanner.R;
import com.ensak.project.foodscanner.model.Item;

import java.util.List;

public class ItemArrayAdapter extends RecyclerView.Adapter<ItemArrayAdapter.NutrientViewHolder1> {
    private Context context;
    private List<Item> nutrientLevelItems;

    public ItemArrayAdapter(Context context, List<Item> navDrawerItems) {
        this.context = context;
        this.nutrientLevelItems = navDrawerItems;
    }

    @Override
    public NutrientViewHolder1 onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list, parent, false);
        return new NutrientViewHolder1(itemView);
    }


    public void onBindViewHolder(ItemArrayAdapter.NutrientViewHolder1 holder, int position) {

        Item nutrientLevelItem = nutrientLevelItems.get(position);


        TextView titleTxt = holder.txtTitle;
        TextView titleTxt1 = holder.txtTittle1;
        TextView titleTxt2 = holder.txtTittle3;

        titleTxt.setText(nutrientLevelItem.getName());
        titleTxt1.setText(nutrientLevelItem.getPortion());
        titleTxt2.setText(nutrientLevelItem.getTaille());


    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return nutrientLevelItems.size();
    }


    class NutrientViewHolder1 extends RecyclerView.ViewHolder {
        TextView txtTitle;
        TextView txtTittle1;
        TextView txtTittle3;


        public NutrientViewHolder1(View itemView) {
            super(itemView);
            txtTitle = itemView.findViewById(R.id.row_item);
txtTittle1=itemView.findViewById(R.id.row_item2);
txtTittle3=itemView.findViewById(R.id.row_item1);

        }
    }
}