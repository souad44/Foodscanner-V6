package com.nextinnovation.pt.barcodescanner.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.Toast;

import com.nextinnovation.pt.barcodescanner.database.DatabaseHelper;
import com.nextinnovation.pt.barcodescanner.fragment.IngredientsFragment;
import com.nextinnovation.pt.barcodescanner.model.JsonProduct;
import com.nextinnovation.pt.barcodescanner.model.Product;
import com.nextinnovation.pt.barcodescanner.rest.ApiInterface;
import com.nextinnovation.pt.barcodescanner.rest.ApiProduct;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CallApi {
    static JsonProduct produit;
public static JsonProduct call(String codebarre){
    ApiInterface api = ApiProduct.getProduct().create(ApiInterface.class);
    Call<JsonProduct> call;
    call = api.getProduct(codebarre);
        call.enqueue(new Callback<JsonProduct>() {
        @Override
        public void onResponse(Call<JsonProduct> call, Response<JsonProduct> response) {
            if(response.body() != null)
            {
                produit = response.body();

            }

        }


        @Override
        public void onFailure(Call<JsonProduct> call, Throwable t) {



        }

    });
return produit;
}}
