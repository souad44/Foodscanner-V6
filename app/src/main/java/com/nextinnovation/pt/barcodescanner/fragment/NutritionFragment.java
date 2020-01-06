package com.nextinnovation.pt.barcodescanner.fragment;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.nextinnovation.pt.barcodescanner.R;
import com.ensak.project.foodscanner.model.Item;
import com.nextinnovation.pt.barcodescanner.adapter.ItemArrayAdapter;
import com.nextinnovation.pt.barcodescanner.model.JsonProduct;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class NutritionFragment extends Fragment {
    TextView txt1;
    // RecyclerView recyclerView;
    public static String fat="";
    public static String fat2="";
    public static String sugar="";
    public static String sel="";
    RecyclerView recyclerView;
    JsonProduct produit;
    TextView scoretext;

    ImageView img1;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View rootView =inflater.inflate(R.layout.fragment_nutrition, container, false);
        produit = (JsonProduct) getArguments().getSerializable("prod");

        String grade=produit.getProduct().getNutriscore_grade();


        img1=rootView.findViewById(R.id.imageViewNutrition);

        HashMap<String, String> hashMap = (HashMap<String, String>) produit.getProduct().getMap();

        scoretext=rootView.findViewById(R.id.textscore);


        List<Item> levelItem = new ArrayList<>();

        RecyclerView rv = rootView.findViewById(R.id.item_list);


        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        rv.setAdapter(new ItemArrayAdapter(getContext(), levelItem));

        ArrayList<String> nutriments= new ArrayList<String>(  Arrays.asList("energy-kcal_100g","fat","saturated-fat_value","carbohydrates","sugars_100g","proteins_value","salt"));
        ArrayList<String> nutriments1= new ArrayList<String>(  Arrays.asList("Enérgie","Matiéres grasses","Graisses saturées","Glucides","Sucres","Protéines","Sel"));
        ArrayList<String> unités= new ArrayList<String>(  Arrays.asList("kcal","g","g","g","g","g","g"));
if(!hashMap.isEmpty()){
        Iterator it = hashMap.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            for(int i=0;i<nutriments.size();i++){

                if(pair.getKey().toString().equals(nutriments.get(i))){
                    levelItem.add(new Item(nutriments1.get(i),unités.get(i),pair.getValue().toString()));


                }}


        }}


if(grade!=null){
        nutriscorefct(grade);}
        return rootView;
    }

    public void nutriscorefct(String grade){
        switch(grade){
            case  "a" :{
                img1.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.nnc_a));
                scoretext.setText(getString(R.string.nnc_aa));
                scoretext.setTextColor(getResources().getColor(R.color.green));


                break;

            }
            case  "b" :{
                img1.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.nnc_b));
                scoretext.setText(getString(R.string.nnc_bb));
                scoretext.setTextColor(getResources().getColor(R.color.green));

                break;
            }
            case  "c" :{
                img1.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.nnc_c));
                scoretext.setText(getString(R.string.nnc_cc));
                scoretext.setTextColor(getResources().getColor(R.color.orange_800_alpha));

                break;
            }
            case  "d" :{
                img1.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.nnc_d));
                scoretext.setText(getString(R.string.nnc_dd));
                scoretext.setTextColor(getResources().getColor(R.color.orange));

                break;
            }
            case  "e" :{
                img1.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.nnc_e));
                scoretext.setText(getString(R.string.nnc_ee));
                scoretext.setTextColor(getResources().getColor(R.color.red));


            }
        }

    }


}
