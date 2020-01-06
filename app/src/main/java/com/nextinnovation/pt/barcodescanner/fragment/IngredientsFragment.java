package com.nextinnovation.pt.barcodescanner.fragment;

import android.content.Context;
import android.os.Build;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;
import com.nextinnovation.pt.barcodescanner.R;
import com.nextinnovation.pt.barcodescanner.adapter.PageAdapter;
import com.nextinnovation.pt.barcodescanner.model.JsonProduct;

public class IngredientsFragment extends Fragment {

    Toolbar toolbar;
    public TabLayout tabLayout;
    ViewPager viewPager;
    public PageAdapter pageAdapter;
    public TabItem tabChats;
    public TabItem tabStatus;
    public TabItem tabCalls;
    TextView textView;
    public Context myContext;
    JsonProduct produit;

    public String txt;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View rootView =inflater.inflate(R.layout.fragment_ingredients, container, false);
        produit = (JsonProduct) getArguments().getSerializable("prod");



        tabLayout = rootView.findViewById(R.id.tablayout);
        tabChats = rootView.findViewById(R.id.tabChats);
        tabStatus = rootView.findViewById(R.id.tabStatus);
        tabCalls = rootView.findViewById(R.id.tabCalls);
        viewPager = rootView.findViewById(R.id.viewPager);
//textView = findViewById(R.id.call);

        pageAdapter = new PageAdapter(getFragmentManager(), tabLayout.getTabCount(),produit);
        viewPager.setAdapter(pageAdapter);


        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                if (tab.getPosition() == 1) {
/*                    toolbar.setBackgroundColor(ContextCompat.getColor(getContext(),
                            R.color.colorAccent));
                    tabLayout.setBackgroundColor(ContextCompat.getColor(getContext(),
                            R.color.colorAccent));*/


                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        //  getContext().getWindow().setStatusBarColor(ContextCompat.getColor(getContext(),R.color.colorAccent));
                    }
                } else if (tab.getPosition() == 2) {
                    /*toolbar.setBackgroundColor(ContextCompat.getColor(getContext(), android.R.color.darker_gray));
                    tabLayout.setBackgroundColor(ContextCompat.getColor(getContext(), android.R.color.darker_gray));*/

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        //    getActivity().getWindow().setStatusBarColor(ContextCompat.getColor(getContext(), android.R.color.darker_gray));

                    }
                } else {
                  /*  toolbar.setBackgroundColor(ContextCompat.getColor(getContext(),
                            R.color.colorPrimary));
                    tabLayout.setBackgroundColor(ContextCompat.getColor(getContext(),
                            R.color.colorPrimary));*/
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        //   getActivity().getWindow().setStatusBarColor(ContextCompat.getColor(getContext(), R.color.colorPrimaryDark));
                    }
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));


        return rootView;
    }




}
