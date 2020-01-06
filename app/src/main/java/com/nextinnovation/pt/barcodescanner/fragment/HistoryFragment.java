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
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.os.Environment;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.nextinnovation.pt.barcodescanner.BuildConfig;
import com.nextinnovation.pt.barcodescanner.R;
import com.nextinnovation.pt.barcodescanner.adapter.NewsAdapter;
import com.nextinnovation.pt.barcodescanner.database.DatabaseHelper;
import com.nextinnovation.pt.barcodescanner.model.JsonProduct;
import com.nextinnovation.pt.barcodescanner.model.Product;
import com.nextinnovation.pt.barcodescanner.rest.ApiInterface;
import com.nextinnovation.pt.barcodescanner.rest.ApiProduct;
import com.nextinnovation.pt.barcodescanner.utils.Utils;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;


public class HistoryFragment extends Fragment{

    DatabaseHelper db ;
    TextView infoView;
    Button scanFirst;
static Context myc;
    RecyclerView NewsRecyclerview;
    NewsAdapter newsAdapter;
    List<Product> mData;
    FloatingActionButton fabSwitcher;
    boolean isDark = false;
    ConstraintLayout rootLayout;
    EditText searchInput ;
    JsonProduct produit;


    CharSequence search="";

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        inflater.inflate(R.menu.menu_history,menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_remove_all_history:
                new MaterialDialog.Builder(getContext())
                        .title(R.string.title_clear_history_dialog)
                        .content(R.string.text_clear_history_dialog)
                        .onPositive((dialog, which) -> {
                            db.deleteAll();
                            mData.clear();
                            NewsRecyclerview.getAdapter().notifyDataSetChanged();

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
                } else {
                    exportCSV();
                }
                return true;



        }

        return true;
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case Utils.MY_PERMISSIONS_REQUEST_STORAGE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    exportCSV();
                } else {

                }
                break;
            }
        }
    }



    public void exportCSV() {
        String folderMain = "Food";
        Toast.makeText(getContext(), R.string.txt_exporting_history, Toast.LENGTH_LONG).show();
        File baseDir = new File(Environment.getExternalStorageDirectory(), folderMain);
        if (!baseDir.exists()) {
            baseDir.mkdirs();
        }
        Log.d("dir", String.valueOf(baseDir));
        String fileName = BuildConfig.FLAVOR.toUpperCase() + "-" + new SimpleDateFormat("yyyy-MM-dd").format(new Date()) + ".csv";
        File csvFile= new File(baseDir ,fileName);
        boolean isDownload = false;
        try (CSVPrinter writer = new CSVPrinter(new FileWriter(csvFile), CSVFormat.DEFAULT.withHeader(getResources().getStringArray(R.array.headers)))) {
            for (Product hp : db.getData()) {
                writer.printRecord(hp.getCode(),hp.getLabels(),hp.getBrands());
            }
            Toast.makeText(getContext(), R.string.txt_history_exported, Toast.LENGTH_LONG).show();
            isDownload = true;
        } catch (IOException e) {
            //Log.e(HistoryScanActivity.class.getSimpleName(), "can export to " + csvFile, e);
        }

        NotificationManager notificationManager = (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
        Intent downloadIntent = new Intent(Intent.ACTION_VIEW);
        downloadIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri csvUri = FileProvider.getUriForFile(getContext(), getActivity().getPackageName() + ".provider", csvFile);
        downloadIntent.setDataAndType(csvUri, "text/csv");
        downloadIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel notificationChannel = new NotificationChannel("downloadChannel", "ChannelCSV", importance);
            notificationManager.createNotificationChannel(notificationChannel);
        }

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {

            String channelId = "export_channel";
            CharSequence channelName = getString(R.string.notification_channel_name);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel notificationChannel = new NotificationChannel(channelId, channelName, importance);
            notificationChannel.setDescription(getString(R.string.notify_channel_description));
            notificationManager.createNotificationChannel(notificationChannel);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(getContext(), "export_channel")
                .setContentTitle(getString(R.string.notify_title))
                .setContentText(getString(R.string.notify_content))
                .setContentIntent(PendingIntent.getActivity(getContext(), 4, downloadIntent, 0))
                .setSmallIcon(R.drawable.barcode_icon_2_png);

        if (isDownload) {
            notificationManager.notify(7, builder.build());
        }
    }



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Historique");

        View view = inflater.inflate(R.layout.fragment_history, container, false);
        setHasOptionsMenu(true);

        rootLayout = view.findViewById(R.id.root_layout);
        searchInput = view.findViewById(R.id.search_input);
        NewsRecyclerview = view.findViewById(R.id.news_rv);

        mData = new ArrayList<>();

        // load theme state

        isDark = getThemeStatePref();
        if(isDark) {
            // dark theme is on

            searchInput.setBackgroundResource(R.drawable.search_input_dark_style);
            rootLayout.setBackgroundColor(getResources().getColor(R.color.black));

        }
        else
        {
            // light theme is on
            searchInput.setBackgroundResource(R.drawable.search_input_style);
            rootLayout.setBackgroundColor(getResources().getColor(R.color.white));

        }



        db = new DatabaseHelper(getActivity());
        Cursor cursor = db.getAllData();
        if (cursor.moveToFirst()){
            do{
                String data = cursor.getString(cursor.getColumnIndex("product_label"));
                String imagestring = cursor.getString(cursor.getColumnIndex("imagestring"));
                mData.add(new Product(data,imagestring));

            }while(cursor.moveToNext());
        }
        cursor.close();

        ItemTouchHelper helper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder target, int i) {
                if (i == ItemTouchHelper.RIGHT) {
Callapi(db.getData1(mData.get(target.getAdapterPosition()).getLabels()));
                }
                if (i == ItemTouchHelper.LEFT) {
                    int position = target.getAdapterPosition();
                    db = new DatabaseHelper(getActivity());
                    db.deleteProduct(mData.get(position));
                    mData.remove(position);

                    Toast.makeText(getContext(), "Produit supprimé", Toast.LENGTH_LONG).show();

                }
                newsAdapter.notifyDataSetChanged();



            }





        });
        helper.attachToRecyclerView(NewsRecyclerview);

        // adapter ini and setup
        // initilize adapter for first time6

        newsAdapter = new NewsAdapter(getContext(),mData,isDark);
        NewsRecyclerview.setAdapter(newsAdapter);
        NewsRecyclerview.setLayoutManager(new LinearLayoutManager(getContext()));



       /* fabSwitcher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isDark = !isDark ;
                if (isDark) {

                    rootLayout.setBackgroundColor(getResources().getColor(R.color.black));
                    searchInput.setBackgroundResource(R.drawable.search_input_dark_style);

                }
                else {
                    rootLayout.setBackgroundColor(getResources().getColor(R.color.white));
                    searchInput.setBackgroundResource(R.drawable.search_input_style);
                }

                newsAdapter = new NewsAdapter(getContext(),mData,isDark);
                // newsAdapter.updateData(mData,isDark);
                if (!search.toString().isEmpty()){

                    newsAdapter.getFilter().filter(search);

                }
                NewsRecyclerview.setAdapter(newsAdapter);
                saveThemeStatePref(isDark);
            }
        });*/





        searchInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


                newsAdapter.getFilter().filter(s);
                search = s;


            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        return view;
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    private void Callapi(final String scanContent) {
        ConnectivityManager connectivity = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivity.getActiveNetwork()!=null) {
        ApiInterface api = ApiProduct.getProduct().create(ApiInterface.class);
        Call<JsonProduct> call;
        call = api.getProduct(scanContent);
        call.enqueue(new Callback<JsonProduct>() {
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


    }
        else{
            Toast.makeText(getContext(), "You need connection", Toast.LENGTH_LONG).show();

        }
    }



    private void saveThemeStatePref(boolean isDark) {

        SharedPreferences pref = getContext().getSharedPreferences("myPref",MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean("isDark",isDark);
        editor.commit();
    }

    private boolean getThemeStatePref () {

        SharedPreferences pref = getContext().getSharedPreferences("myPref",MODE_PRIVATE);
        boolean isDark = pref.getBoolean("isDark",false) ;
        return isDark;

    }




}