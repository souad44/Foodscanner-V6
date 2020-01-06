package com.nextinnovation.pt.barcodescanner.activity;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager.widget.ViewPager;

import com.edwardvanraak.materialbarcodescanner.MaterialBarcodeScanner;
import com.edwardvanraak.materialbarcodescanner.MaterialBarcodeScannerBuilder;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.nextinnovation.pt.barcodescanner.R;
import com.nextinnovation.pt.barcodescanner.database.DatabaseHelper;
import com.nextinnovation.pt.barcodescanner.fragment.BarcodeFragment;
import com.nextinnovation.pt.barcodescanner.fragment.SearchFragment;
import com.nextinnovation.pt.barcodescanner.model.JsonProduct;
import com.nextinnovation.pt.barcodescanner.rest.ApiInterface;
import com.nextinnovation.pt.barcodescanner.rest.ApiProduct;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NavigationDrawerActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, BarcodeFragment.ScanRequest {
    private DrawerLayout drawer;

    private Context context ;
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    public static final String BARCODE_KEY = "BARCODE";
    private Barcode barcodeResult;
    private final String TAG = PrincipalActivity.class.getSimpleName() ;
    private final int MY_PERMISSION_REQUEST_CAMERA = 1001;
   // private PrincipalActivity.ItemScanned itemScanned ;
    JsonProduct produit;
    SharedPreferences sharedpreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation_drawer);
        sharedpreferences = getPreferences(Context.MODE_PRIVATE);


        /*Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new BarcodeFragment()).commit();
            navigationView.setCheckedItem(R.id.nav_home);
        }*/
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_home:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new BarcodeFragment()).commit();
                break;
            case R.id.nav_search:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new SearchFragment()).commit();
                break;

            case R.id.nav_list:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new SearchFragment()).commit();
                break;

          /*
            case R.id.nav_profile:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new ProfileFragment()).commit();
                break;
/*            case R.id.nav_share:
                Toast.makeText(this, "Share", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_send:
                Toast.makeText(this, "Send", Toast.LENGTH_SHORT).show();
                break;

            */
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void scanBarcode() {
        /** This method will listen the button clicked passed form the fragment **/
        checkPermission();
    }
    private void checkPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG , getResources().getString(R.string.camera_permission_granted));
            startScanningBarcode();
        } else {
            requestCameraPermission();

        }
    }
    private void requestCameraPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {

            ActivityCompat.requestPermissions(NavigationDrawerActivity.this,  new String[] {Manifest.permission.CAMERA}, MY_PERMISSION_REQUEST_CAMERA);

        } else{
            ActivityCompat.requestPermissions(NavigationDrawerActivity.this,new String[] {Manifest.permission.CAMERA}, MY_PERMISSION_REQUEST_CAMERA);
        }
    }
    public String getScanTime() {
        DateFormat timeFormat = new SimpleDateFormat("hh:mm a" , Locale.getDefault());
        return  timeFormat.format(new Date());
    }
    //public static final String MyPREFERENCES = "MyPrefs" ;




    private void showDialog(final String scanContent, final String currentTime, final String currentDate) {
        AlertDialog.Builder builder = new AlertDialog.Builder(NavigationDrawerActivity.this);


        ApiInterface api = ApiProduct.getProduct().create(ApiInterface.class);
        Call<JsonProduct> call;
        call = api.getProduct(scanContent);
        call.enqueue(new Callback<JsonProduct>() {
            @Override
            public void onResponse(Call<JsonProduct> call, Response<JsonProduct> response) {
                if(response.body() != null)
                {
                    produit = response.body();

                    sharedpreferences.edit().putString("label",produit.getProduct().getLabels());


                    Log.i("here",produit.getProduct().getLabels());
                    Log.i("ingredients",produit.getProduct().getIngredients());


                }
            }

            @Override
            public void onFailure(Call<JsonProduct> call, Throwable t) {
                Toast.makeText(NavigationDrawerActivity.this ,"Connection failed" +t,Toast.LENGTH_LONG).show();



            }
        });

        builder.setMessage(scanContent)
                .setTitle(R.string.dialog_title);
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                DatabaseHelper databaseHelper = new DatabaseHelper(context);
                //databaseHelper.addProduct(new Product(scanContent,currentTime,currentDate));
                Toast.makeText(NavigationDrawerActivity.this, "Saved", Toast.LENGTH_SHORT).show();
                viewPager.setCurrentItem(1);


            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Toast.makeText(NavigationDrawerActivity.this, "Not Saved", Toast.LENGTH_SHORT).show();
            }
        });

        builder.show();
    }


    public String getScanDate() {
        DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yy",Locale.getDefault());
        return dateFormat.format(new Date());
    }

    private void startScanningBarcode() {
        /**
         * Build a new MaterialBarcodeScanner
         */
        final MaterialBarcodeScanner materialBarcodeScanner = new MaterialBarcodeScannerBuilder()
                .withActivity(NavigationDrawerActivity.this)
                .withEnableAutoFocus(true)
                .withBleepEnabled(true)
                .withBackfacingCamera()
                .withCenterTracker()
                .withText("Scanning...")
                .withResultListener(new MaterialBarcodeScanner.OnResultListener() {
                    @Override
                    public void onResult(Barcode barcode) {
                        barcodeResult = barcode;
                        showDialog(barcode.rawValue , getScanTime(),getScanDate());
                    }
                })
                .build();
        materialBarcodeScanner.startScan();
    }

}
