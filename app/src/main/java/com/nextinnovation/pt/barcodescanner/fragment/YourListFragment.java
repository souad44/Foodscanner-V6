package com.nextinnovation.pt.barcodescanner.fragment;


import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.nextinnovation.pt.barcodescanner.BuildConfig;
import com.nextinnovation.pt.barcodescanner.R;
import com.nextinnovation.pt.barcodescanner.adapter.NewsAdapter;
import com.nextinnovation.pt.barcodescanner.adapter.YourListAdapter;
import com.nextinnovation.pt.barcodescanner.database.DatabaseHelper;
import com.nextinnovation.pt.barcodescanner.model.JsonProduct;
import com.nextinnovation.pt.barcodescanner.model.Product;
import com.nextinnovation.pt.barcodescanner.rest.ApiInterface;
import com.nextinnovation.pt.barcodescanner.rest.ApiProduct;
import com.nextinnovation.pt.barcodescanner.utils.Utils;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;


public class YourListFragment extends Fragment {


    DatabaseHelper mydb;
    String user_id;
    SharedPreferences preferences;
    RecyclerView recyclerView;
    YourListAdapter favorisAdapter;
    ConstraintLayout rootLayout;
    EditText searchInput;
    ArrayList<JsonProduct> produits;
    CharSequence search="";


    private OnFragmentInteractionListener mListener;

    public YourListFragment() {
    }

    public interface OnFragmentInteractionListener
    {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Ma Liste");
        View view = inflater.inflate(R.layout.fragment_your_list, container, false);
        setHasOptionsMenu(true);

        rootLayout = view.findViewById(R.id.layout_parent);
        searchInput = view.findViewById(R.id.recherche_token);
        recyclerView = view.findViewById(R.id.recyclerview);

        produits = new ArrayList<>();
        mydb = new DatabaseHelper(getActivity());
        preferences = getActivity().getSharedPreferences("user_details",MODE_PRIVATE);
        user_id = preferences.getString("user_id",null);
        produits = getFavorisPerUser(user_id);


        ItemTouchHelper helper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }


            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder target, int i) {
                if (i == ItemTouchHelper.RIGHT) {
                    ProductDetails(produits.get(target.getAdapterPosition()).getCode());

                } else if (i == ItemTouchHelper.LEFT) {
                    int position = target.getAdapterPosition();
                    mydb.deleteFavoris(produits.get(position).getCode());
                    produits.remove(position);
                    Toast.makeText(getContext(), "Produit supprimé", Toast.LENGTH_LONG).show();

                }

                favorisAdapter.notifyDataSetChanged();

            }

        });
        helper.attachToRecyclerView(recyclerView);


        if(produits != null)
        {
            favorisAdapter = new YourListAdapter(getContext(),produits);
            recyclerView.setAdapter(favorisAdapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        }
        else
        {
            Toast.makeText(getContext(),"null",Toast.LENGTH_SHORT).show();
        }

        searchInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                favorisAdapter.getFilter().filter(s);
                search = s;
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        return view;

    }

    public void getAll()
    {
        mydb.getFavorisByUserId(user_id);
    }


    public ArrayList<JsonProduct> getFavorisPerUser(String user_id)
    {
        if (user_id != null)
        {
            Cursor cursor = mydb.getFavorisByUserId(user_id);

            if (cursor != null) {
                if (cursor.moveToFirst())
                {
                    do{
                        String code = cursor.getString(1);
                        ApiInterface api = ApiProduct.getProduct().create(ApiInterface.class);
                        Call<JsonProduct> call;
                        call = api.getProduct(code);
                        call.enqueue(new Callback<JsonProduct>() {
                            @Override
                            public void onResponse(Call<JsonProduct> call, Response<JsonProduct> response) {
                                if(response.body() != null)
                                {
                                    produits.add(response.body());
                                    favorisAdapter.notifyDataSetChanged();
                                }
                            }
                            @Override
                            public void onFailure(Call<JsonProduct> call, Throwable t) {
                            }
                        });
                    }while(cursor.moveToNext());
                }
            }
            else
            {
                produits = null;
                favorisAdapter.clear();
                favorisAdapter.notifyDataSetChanged();
            }

            cursor.close();
        }
        return produits;
    }

    public void ProductDetails(String code)
    {
        ConnectivityManager connectivity = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivity.getActiveNetwork()!=null) {
            ApiInterface api = ApiProduct.getProduct().create(ApiInterface.class);
            Call<JsonProduct> call;
            call = api.getProduct(code);
            call.enqueue(new Callback<JsonProduct>() {
                JsonProduct produit = new JsonProduct();

                @Override
                public void onResponse(Call<JsonProduct> call, Response<JsonProduct> response) {
                    if(response.body() != null)
                    {
                        produit = response.body();
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("prod",produit);
                        IngredientsFragment fragobj=new IngredientsFragment();
                        fragobj.setArguments(bundle);
                        getFragmentManager().beginTransaction()
                                .replace(((ViewGroup) getView().getParent()).getId(), fragobj)
                                .addToBackStack(null)
                                .commitAllowingStateLoss();

                    }
                }

                @Override
                public void onFailure(Call<JsonProduct> call, Throwable t) {
                    Toast.makeText(getContext() ,"Connection failed" +t,Toast.LENGTH_LONG).show();

                }
            });
        }
        else{
            Toast.makeText(getContext(), "You need connection", Toast.LENGTH_LONG).show();

        }
    }



    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }


    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        inflater.inflate(R.menu.menu_history,menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_remove_all_history:
                new MaterialDialog.Builder(getContext())
                        .title(R.string.title_clear_history_dialog)
                        .content(R.string.text_clear_history_dialog)
                        .onPositive((dialog, which) -> {
                            mydb.deleteAllFavoris();
                            produits.clear();
                            recyclerView.getAdapter().notifyDataSetChanged();

                        })
                        .positiveText(R.string.txtYes)
                        .negativeText(R.string.txtNo)
                        .show();
                return true;
            case R.id.action_export_all_history:

                if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                        new MaterialDialog.Builder(getContext())
                                .title(R.string.action_about)
                                .content(R.string.permision_write_external_storage)
                                .neutralText(R.string.txtOk)
                                .show();
                    } else {
                        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, Utils
                                .MY_PERMISSIONS_REQUEST_STORAGE);
                    }

                    return true;

                }
        }
        return true;

    }

}
