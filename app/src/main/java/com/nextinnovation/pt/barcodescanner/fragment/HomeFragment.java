package com.nextinnovation.pt.barcodescanner.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.nextinnovation.pt.barcodescanner.R;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import static android.content.Context.MODE_PRIVATE;


public class HomeFragment extends Fragment {

    SharedPreferences prf;


    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prf = getActivity().getSharedPreferences("user_details",MODE_PRIVATE);
        String user_id = prf.getString("user_id",null);
        Toast.makeText(getContext(),"id : " + user_id, Toast.LENGTH_SHORT).show();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Accueil");
        return inflater.inflate(R.layout.fragment_home, container, false);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            // scanRequest = (ScanRequest) context;
        }
        catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement retryConnectionListener");
        }

    }


}
