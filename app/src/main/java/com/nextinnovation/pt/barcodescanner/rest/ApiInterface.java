package com.nextinnovation.pt.barcodescanner.rest;

import com.nextinnovation.pt.barcodescanner.model.JsonProduct;
import com.nextinnovation.pt.barcodescanner.model.Product;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiInterface {

    @GET("api/v0/product/{bar_code}.json")
    Call<JsonProduct> getProduct(@Path(value="bar_code",encoded = true) String bar_code);
}
