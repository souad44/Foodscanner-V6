package com.nextinnovation.pt.barcodescanner.fragment;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.ensak.project.foodscanner.model.NutrientLevelItem;
import com.ensak.project.foodscanner.model.nutriments;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.nextinnovation.pt.barcodescanner.R;
import com.nextinnovation.pt.barcodescanner.adapter.YourListAdapter;
import com.nextinnovation.pt.barcodescanner.adapter.repereApadter;
import com.nextinnovation.pt.barcodescanner.database.DatabaseHelper;
import com.nextinnovation.pt.barcodescanner.model.JsonProduct;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;


/**
 * A simple {@link Fragment} subclass.
 */
public class ResumeFragment extends Fragment {
    //   @BindView(R.id.imageGrade)
    ImageView img;
    ImageView img2;
    ImageView img3;
    ImageView img4;
    ImageView img5;
    TextView txt1;
    TextView txt2;
    TextView txt3;
    TextView txt4;
    TextView txt6;

    TextView txt5;

    PieChart pieChart ;
    ArrayList<PieEntry> entries ;
    ArrayList<String> PieEntryLabels ;
    PieDataSet pieDataSet ;
    PieData pieData ;
    public String fat;
    public String sucre;
    public String sel;
    public String satured_fat;
    ImageView imageViewIngredients;
    JsonProduct produit;
    ImageButton add_favoris_btn;
    SharedPreferences prf;
    String user_id;
    RecyclerView recyclerView;
    DatabaseHelper db;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        db = new DatabaseHelper(getContext());
        prf = getActivity().getSharedPreferences("user_details",MODE_PRIVATE);
        user_id = prf.getString("user_id",null);
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView =inflater.inflate(R.layout.fragment_resume, container, false);
        produit = (JsonProduct) getArguments().getSerializable("prod");


        String product_name=produit.getProduct().getLabels();
        String fournisseur=produit.getProduct().getBrands();
        String nutri_grade=produit.getProduct().getNutriscore_grade();

        String nova_group=produit.getProduct().getNova_group();
        String product_image=produit.getProduct().getProduct_image();
        String ingredients_image=produit.getProduct().getImageIngredientsUrl();
        String quantity = produit.getProduct().getQuantity();

        ArrayList<String> a1=new ArrayList<String>();
        a1=produit.getProduct().getadditives();
        HashMap<String, String> Nutrient_levels = (HashMap<String, String>)produit.getProduct().getNutrient_levels();

        String   countries=produit.getProduct().getCountries();

        List<NutrientLevelItem> levelItem = new ArrayList<>();

     RecyclerView rv = rootView.findViewById(R.id.item_list1);
        add_favoris_btn = rootView.findViewById(R.id.action_add_to_list_button);

        txt5 = rootView.findViewById(R.id.textPays);
        txt6=rootView.findViewById(R.id.qualité);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        rv.setAdapter(new repereApadter(getContext(), levelItem));
        txt1=rootView.findViewById(R.id.textNameProduct);
        txt2=rootView.findViewById(R.id.textBrandProduct);
        txt2.setText(fournisseur);
        txt3 =rootView.findViewById(R.id.textAdditiveProduct);

        if (quantity!=null){

            txt5.setText(quantity);
        }

        if(!a1.isEmpty()){
        for(int i=0;i<a1.size();i++){

            txt3.append(a1.get(i));
            txt3.append(",");}}
        else{
            txt3.setText("Ce produit ne contient pas des additives");

        }
        txt1.setText(product_name);

        setHasOptionsMenu(true);
        View list =inflater.inflate(R.layout.list_nutri, container, false);

        img = (ImageView) rootView.findViewById(R.id.imageGrade);
        img2 = (ImageView) rootView.findViewById(R.id.nova_group);
        img3 =(ImageView)list.findViewById(R.id.imgLevel);
        img4=rootView.findViewById(R.id.imageViewFront);
        img5=rootView.findViewById(R.id.imgScore);

        Picasso.with(getActivity()).load(product_image).into(img4);


        HashMap<String, String> hashMap = (HashMap<String, String>) produit.getProduct().getMap();
        Iterator it = hashMap.entrySet().iterator();
        if(nutri_grade!=null){
                nutriscorefct(nutri_grade);}
        else{
        }
        if(nova_group!=null){
        novagroupfct(nova_group);}
        else{

        }

        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();

            if (pair.getKey().toString().equals("saturated-fat")) {
                levelItem.add(new NutrientLevelItem(getActivity().getString(R.string.txtSaturatedFat), pair.getValue().toString(),labellevel(Nutrient_levels,"saturated-fat"), getImageLevel(labellevel(Nutrient_levels,"saturated-fat"))));
                satured_fat = pair.getValue().toString();



            }
            if (pair.getKey().toString().equals("fat")) {
                levelItem.add(new NutrientLevelItem(getActivity().getString(R.string.txtFat), pair.getValue().toString(), labellevel(Nutrient_levels,"fat"), getImageLevel(labellevel(Nutrient_levels,"fat"))));
                fat = pair.getValue().toString();

            }
            if (pair.getKey().toString().equals("sugars_value")) {
                levelItem.add(new NutrientLevelItem(getActivity().getString(R.string.txtSugars), pair.getValue().toString(), labellevel(Nutrient_levels,"sugars"), getImageLevel(labellevel(Nutrient_levels,"sugars"))));
                sucre = pair.getValue().toString();

            }
            if (pair.getKey().toString().equals("salt")) {
                levelItem.add(new NutrientLevelItem(getActivity().getString(R.string.txtSalt), pair.getValue().toString(), labellevel(Nutrient_levels,"salt"), getImageLevel(labellevel(Nutrient_levels,"salt"))));
                sel = pair.getValue().toString();

            }

        }
        pieChart = (PieChart) rootView.findViewById(R.id.chart);

        entries = new ArrayList<>();

        PieEntryLabels = new ArrayList<String>();

        AddValuesToPIEENTRY();


        pieDataSet = new PieDataSet(entries, "");


        pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        pieData = new PieData(pieDataSet);


        pieChart.setData(pieData);

        pieChart.animateY(3000);

        add_to_favoris();

        return rootView;
    }

    public void AddValuesToPIEENTRY(){
        if(!fat.equals("0")){

            entries.add(new PieEntry(Float.parseFloat(fat), "fat"));}
        if(!satured_fat.equals("0")){

            entries.add(new PieEntry(Float.parseFloat(satured_fat), "satured_fat"));}
        if(!sucre.equals("0")){

            entries.add(new PieEntry(Float.parseFloat(sucre), "sucre"));}
        if(!sel.equals("0")){

            entries.add(new PieEntry(Float.parseFloat(sel), "sel"));}



    }
    public int getImageLevel(String level) {
        int drawable = 0;

        switch (level) {
            case "moderate":
                drawable = R.drawable.moderate;
                break;
            case "low":
                drawable = R.drawable.low;
                break;
            case "high":
                drawable = R.drawable.high;
                break;
            default:
                // no job
                break;
        }

        return drawable;
    }
    public String labellevel(HashMap<String, String> Nutrient_levels ,String nutri){
        String result="";
        Iterator it =Nutrient_levels.entrySet().iterator();

        while (it.hasNext()) {
            Map.Entry pair1 = (Map.Entry) it.next();
            if(pair1.getKey().toString().equals(nutri)){
                result=pair1.getValue().toString();
            }



        }
        return result;
    }



    public void novagroupfct(String group){
        switch(group){
            case  "1" :{
                img2.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.ic_nova_group_1));
                break;

            }
            case  "2" :{
                img2.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.ic_nova_group_2));
                break;
            }
            case  "3" :{
                img2.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.ic_nova_group_3));
                break;
            }
            case  "4" :{
                img2.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.ic_nova_group_4));
                break;
            }

        }
    }

    public void nutriscorefct(String grade){
        switch(grade){
            case  "a" :{
                img.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.nnc_a));
                img5.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.low));
                txt6.setText("bon");

                break;

            }
            case  "b" :{
                img.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.nnc_b));
                img5.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.low));
                txt6.setText("bon");

                break;
            }
            case  "c" :{
                img.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.nnc_c));
                img5.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.moderate));
                txt6.setText("Pas mal");

                break;
            }
            case  "d" :{
                img.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.nnc_d));
                img5.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.high));
                txt6.setText("A eviter");

                break;
            }
            case  "e" :{
                img.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.nnc_e));
                img5.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.high));
                txt6.setText("dangeureux");


            }
        }

    }

    public void add_to_favoris()
    {
        add_favoris_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(user_id == null)
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setMessage("Veuillez vous connecter pour ajouter ce produit en favoris.")
                            .setTitle(R.string.exit_favoris);
                    builder.setPositiveButton(R.string.unelevated_button_label_enabled, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Fragment LoginFragment = new LoginFragment();
                            replaceFragments(LoginFragment);
                        }
                    });
                    builder.setNegativeButton(R.string.annuler, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.dismiss();
                        }
                    });

                    builder.show();
                }
                else
                {
                    //db.deleteAllFavoris();
                    Cursor favoris =  db.getFavorisByCodeAndUserId(produit.getProduct().getCode(),user_id);
                    if(favoris.getCount() == 0)
                    {
                        db.insertFavoris(user_id,produit.getProduct().getCode());
                        replaceFragments(new YourListFragment());

                    }
                    else
                    {
                        Toast.makeText(getContext(),"Already exists",Toast.LENGTH_SHORT).show();
                    }

                }

            }
        });
    }

    public void replaceFragments(Fragment fragment) {

        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.setCustomAnimations(R.anim.enter_from_right,R.anim.exit_to_left);
        ft.replace(R.id.frame_container, fragment)
                .commit();
    }


}
