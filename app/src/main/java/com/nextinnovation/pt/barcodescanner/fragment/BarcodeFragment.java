package com.nextinnovation.pt.barcodescanner.fragment;
import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;


import com.edwardvanraak.materialbarcodescanner.MaterialBarcodeScanner;
import com.edwardvanraak.materialbarcodescanner.MaterialBarcodeScannerBuilder;

import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.material.snackbar.Snackbar;
import com.nextinnovation.pt.barcodescanner.R;
import com.nextinnovation.pt.barcodescanner.database.DatabaseHelper;
import com.nextinnovation.pt.barcodescanner.model.JsonProduct;
import com.nextinnovation.pt.barcodescanner.rest.ApiInterface;
import com.nextinnovation.pt.barcodescanner.rest.ApiProduct;
import com.nextinnovation.pt.barcodescanner.fragment.IngredientsFragment;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by PT on 2/9/2017.
 */

public class BarcodeFragment extends Fragment implements View.OnClickListener {
DatabaseHelper db;
    private static final String TAG= "BarcodeFragment";
    private Button btnScan ;
    private ScanRequest scanRequest ;
    private ItemScanned itemScanned ;
    JsonProduct produit;
    public static final String BARCODE_KEY = "BARCODE";
    private Barcode barcodeResult;
    private final int MY_PERMISSION_REQUEST_CAMERA = 1001;

    public BarcodeFragment(){

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_barcode_scanner, container, false);


    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        btnScan = view.findViewById(R.id.btnScan);
        btnScan.setOnClickListener(this);
        //   loadAdd(view);

    }

    /*private void loadAdd(View view) {
        AdView mAdView = view.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

    }*/
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


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnScan :
                // Pass the click event to activity to start the scanner .
                scanBarcode();
                break ;
        }
    }


    public interface  ScanRequest{
        void scanBarcode();
    }

    public String getScanTime() {
        DateFormat timeFormat = new SimpleDateFormat("hh:mm a" , Locale.getDefault());
        return  timeFormat.format(new Date());
    }

    public String getScanDate() {
        DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yy",Locale.getDefault());
        return dateFormat.format(new Date());
    }

    public void scanBarcode() {
        /** This method will listen the button clicked passed form the fragment **/
        checkPermission();
    }








    private void showDialog(final String scanContent, final String currentTime, final String currentDate) {
        ConnectivityManager connectivity = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivity.getActiveNetwork()!=null) {
            ApiInterface api = ApiProduct.getProduct().create(ApiInterface.class);
            Call<JsonProduct> call;
            call = api.getProduct(scanContent);
            Toast.makeText(getContext(), "ha" + scanContent, Toast.LENGTH_SHORT).show();

            call.enqueue(new Callback<JsonProduct>() {
                @Override
                public void onResponse(Call<JsonProduct> call, Response<JsonProduct> response) {
                    if (response.isSuccessful()) {

                        if (response.body().getStatus().equals("1")) {
                            produit = response.body();

                            Bundle bundle = new Bundle();
                            bundle.putSerializable("prod", produit);
                            IngredientsFragment fragobj = new IngredientsFragment();
                            fragobj.setArguments(bundle);

                            getFragmentManager().beginTransaction()
                                    .replace(((ViewGroup) getView().getParent()).getId(), fragobj)
                                    .addToBackStack(null)
                                    .commitAllowingStateLoss();
                            if(produit.getProduct().getLabels()!=null) {
                                db = new DatabaseHelper(getActivity());
                                produit = response.body();
                                db.addProduct(produit);
                            }

                        } else {
                            Toast.makeText(getContext(), "Produit n'existe pas", Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                @Override
                public void onFailure(Call<JsonProduct> call, Throwable t) {
                    Toast.makeText(getContext(), "Connection failed" + t, Toast.LENGTH_LONG).show();


                }
            });

        }
        else{
            Toast.makeText(getContext(), "You need connection", Toast.LENGTH_LONG).show();

        }
    }



    private void checkPermission() {
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG , getResources().getString(R.string.camera_permission_granted));
            startScanningBarcode();
        } else {
            requestCameraPermission();

        }
    }

    private void requestCameraPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.CAMERA)) {

            ActivityCompat.requestPermissions(getActivity(),  new String[] {Manifest.permission.CAMERA}, MY_PERMISSION_REQUEST_CAMERA);

        } else{
            ActivityCompat.requestPermissions(getActivity(),new String[] {Manifest.permission.CAMERA}, MY_PERMISSION_REQUEST_CAMERA);
        }
    }

    private void startScanningBarcode() {
        /**
         * Build a new MaterialBarcodeScanner
         */
        final MaterialBarcodeScanner materialBarcodeScanner = new MaterialBarcodeScannerBuilder()
                .withActivity(getActivity())
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode==MY_PERMISSION_REQUEST_CAMERA && grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            startScanningBarcode();
        } else {

        }

    }


    public interface  ItemScanned{
        void itemUpdated();
    }

}
