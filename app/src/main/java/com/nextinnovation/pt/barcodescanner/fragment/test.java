package com.nextinnovation.pt.barcodescanner.fragment;

import android.os.Bundle;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

public class test extends Fragment {
    public void lolo(){
        Bundle bundle = new Bundle();
        // String myMessage = "Stackoverflow is cool!";
        IngredientsFragment fragobj=new IngredientsFragment();
        fragobj.setArguments(bundle);
        //loadFragment(fragobj);

        getFragmentManager().beginTransaction()
                .replace(((ViewGroup) getView().getParent()).getId(), fragobj)
                .addToBackStack(null)
                .commitAllowingStateLoss();
    }
}
