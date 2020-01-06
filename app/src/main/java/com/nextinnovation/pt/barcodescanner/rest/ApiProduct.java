package com.nextinnovation.pt.barcodescanner.rest;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiProduct {

    private static final String BASE_URL = "http://world.openfoodfacts.org/";

    private static Retrofit retrofit = null;

    public static Retrofit getProduct()
    {
        if(retrofit == null)
        {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }

        return retrofit;
    }
}
