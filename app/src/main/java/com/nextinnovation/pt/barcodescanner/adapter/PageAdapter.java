package com.nextinnovation.pt.barcodescanner.adapter;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;


import com.nextinnovation.pt.barcodescanner.fragment.IngredientsdetailsFragment;
import com.nextinnovation.pt.barcodescanner.fragment.NutritionFragment;
import com.nextinnovation.pt.barcodescanner.fragment.ResumeFragment;
import com.nextinnovation.pt.barcodescanner.model.JsonProduct;

/**
 * Created by abdalla on 2/18/18.
 */

public class PageAdapter extends FragmentStatePagerAdapter {

    public int numOfTabs;
    public FragmentActivity myContext;
public JsonProduct produit;
    public PageAdapter(FragmentManager fm, int numOfTabs,JsonProduct produit) {
        super(fm);
        this.numOfTabs = numOfTabs;
        this.produit=produit;
    }

    @Override
    public Fragment getItem(int position) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("prod",produit);


        switch (position) {
            case 0:
                ResumeFragment resumeFragment=new ResumeFragment();
                resumeFragment.setArguments(bundle);
                return resumeFragment;
            case 1:
                IngredientsdetailsFragment ingredientsdetailsFragment=new IngredientsdetailsFragment();
                ingredientsdetailsFragment.setArguments(bundle);
                return  ingredientsdetailsFragment;
            case 2:
                NutritionFragment nutritionFragment=new NutritionFragment();
                nutritionFragment.setArguments(bundle);
                return nutritionFragment;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return numOfTabs;
    }
}
