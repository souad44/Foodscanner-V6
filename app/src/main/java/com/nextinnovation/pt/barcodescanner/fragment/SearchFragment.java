package com.nextinnovation.pt.barcodescanner.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.vision.barcode.Barcode;
import com.nextinnovation.pt.barcodescanner.R;
import com.nextinnovation.pt.barcodescanner.database.DatabaseHelper;
import com.nextinnovation.pt.barcodescanner.model.JsonProduct;
import com.nextinnovation.pt.barcodescanner.model.Product;
import com.nextinnovation.pt.barcodescanner.rest.ApiInterface;
import com.nextinnovation.pt.barcodescanner.rest.ApiProduct;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;



public class SearchFragment extends Fragment implements View.OnClickListener{


    DatabaseHelper db ;
    SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "MyPref" ;


    Button search ;
    EditText editText;
    JsonProduct produit;
    private ViewPager viewPager;
    String barcode;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        View v =inflater.inflate(R.layout.fragment_search, container, false);
        editText = v.findViewById(R.id.editTextBarcode);
        search = v.findViewById(R.id.buttonBarcode);
        search.setOnClickListener(this);



        return v;
    }



    /* @Override
     public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
         inflater.inflate(R.menu.menu_history, menu);


         super.onCreateOptionsMenu(menu, inflater);
     }
 */
    public String getScanTime() {
        DateFormat timeFormat = new SimpleDateFormat("hh:mm a" , Locale.getDefault());
        return  timeFormat.format(new Date());
    }

    public String getScanDate() {
        DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yy",Locale.getDefault());
        return dateFormat.format(new Date());
    }
    @Override
    public void onClick(View v) {
        barcode = editText.getText().toString();
        showDialog(barcode , getScanTime(),getScanDate());



    }


    private void showDialog(final String scanContent, final String currentTime, final String currentDate) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        ApiInterface api = ApiProduct.getProduct().create(ApiInterface.class);
        Call<JsonProduct> call;
        call = api.getProduct(scanContent);
        call.enqueue(new Callback<JsonProduct>() {
            @Override
            public void onResponse(Call<JsonProduct> call, Response<JsonProduct> response) {
                if(response.body() != null)
                {
                    db = new DatabaseHelper(getActivity());
                    produit = response.body();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("prod", produit);
                    IngredientsFragment fragobj = new IngredientsFragment();
                    fragobj.setArguments(bundle);

                    getFragmentManager().beginTransaction()
                            .replace(((ViewGroup) getView().getParent()).getId(), fragobj)
                            .addToBackStack(null)
                            .commitAllowingStateLoss();
                    db = new DatabaseHelper(getActivity());
                    produit = response.body();
                    db.addProduct(produit);

                }
            }

            @Override
            public void onFailure(Call<JsonProduct> call, Throwable t) {
                Toast.makeText(getContext() ,"Connection failed" +t,Toast.LENGTH_LONG).show();



            }
        });




        builder.show();
    }
}
