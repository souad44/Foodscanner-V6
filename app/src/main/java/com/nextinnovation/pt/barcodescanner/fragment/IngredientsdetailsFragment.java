package com.nextinnovation.pt.barcodescanner.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.nextinnovation.pt.barcodescanner.R;
import com.nextinnovation.pt.barcodescanner.model.JsonProduct;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class IngredientsdetailsFragment extends Fragment {
    TextView txt1;
    TextView txt2;
    TextView txt3;
    TextView txt4;
    TextView txt5;
    TextView txt6;
    TextView novadescroption;
    TextView novaexplication;
    ImageView imageViewIngredients;
    ImageView novaimg;
    JsonProduct produit;
    TextView scoretext;
    public static String fat="";
    public static String fat2="";
    public static String sugar="";
    public static String sel="";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView =inflater.inflate(R.layout.fragment_ingredientsdetails, container, false);

        produit = (JsonProduct) getArguments().getSerializable("prod");

        String ingredients=produit.getProduct().getIngredients();
        String ingredients_image=produit.getProduct().getImageIngredientsUrl();
        String nova_group=produit.getProduct().getNova_group();
        String nutri_grade=produit.getProduct().getNutriscore_grade();

        ArrayList<String> a1=new ArrayList<String>();
        a1=produit.getProduct().getadditives();
        // Intent intent = getActivity().getIntent();
        HashMap<String, String> hashMap = (HashMap<String, String>)produit.getProduct().getMap();

        setHasOptionsMenu(true);

        txt3=rootView.findViewById(R.id.textCategoryProduct2);
        txt1=rootView.findViewById(R.id.textAdditiveProduct);
        novaexplication=rootView.findViewById(R.id.novaexplication);
        imageViewIngredients=rootView.findViewById(R.id.imageViewIngredients);
        novadescroption =rootView.findViewById(R.id.novadescroption);
        novaimg =rootView.findViewById(R.id.novaimg);
        txt2=rootView.findViewById(R.id.textCategoryProduct);

if(ingredients_image!=null){
        Picasso.with(getActivity()).load(ingredients_image).into(imageViewIngredients);}
        if(ingredients!=null) {
            txt2.append(ingredients);
        }
        else{
            txt2.append("Les ingredients n'existent pas");

        }
        if(!a1.isEmpty()){
        for(int i=0;i<a1.size();i++){

            txt3.append(a1.get(i));
            txt3.append(",");}}else{
            txt3.setText("Ce produit ne contient pas des additives");

        }
        if(!hashMap.isEmpty()){
        Iterator it = hashMap.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();

        }}
        if(nova_group!=null){
        novagroupfct(nova_group);}


        return rootView;
    }
    public void novagroupfct(String group){
        switch(group){
            case  "1" :{
                novaimg.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.ic_nova_group_1));
                novaexplication.setText("Groupe 1 :");
                novadescroption.setText(R.string.nova_grp1_msg);
                break;

            }
            case  "2" :{
                novaimg.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.ic_nova_group_2));
                novaexplication.setText("Groupe 2 :");
                novadescroption.setText(R.string.nova_grp2_msg);

                break;
            }
            case  "3" :{
                novaimg.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.ic_nova_group_3));
                novaexplication.setText("Groupe 3 :");
                novadescroption.setText(R.string.nova_grp3_msg);

                break;
            }
            case  "4" :{
                novaimg.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.ic_nova_group_4));
                novaexplication.setText("Groupe 4 :");
                novadescroption.setText(R.string.nova_grp4_msg);

                break;
            }

        }
    }




}
