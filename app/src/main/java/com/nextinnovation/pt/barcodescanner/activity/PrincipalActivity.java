package com.nextinnovation.pt.barcodescanner.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.nextinnovation.pt.barcodescanner.R;
import com.nextinnovation.pt.barcodescanner.fragment.BarcodeFragment;
import com.nextinnovation.pt.barcodescanner.fragment.HistoryFragment;
import com.nextinnovation.pt.barcodescanner.fragment.HomeFragment;
import com.nextinnovation.pt.barcodescanner.fragment.LoginFragment;
import com.nextinnovation.pt.barcodescanner.fragment.SearchFragment;
import com.nextinnovation.pt.barcodescanner.fragment.SignUpFragment;
import com.nextinnovation.pt.barcodescanner.fragment.YourListFragment;


import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.viewpager.widget.ViewPager;



public class PrincipalActivity extends AppCompatActivity implements SignUpFragment.OnFragmentInteractionListener {


    private Context context ;
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    public static final String BARCODE_KEY = "BARCODE";
    private Barcode barcodeResult;
    private final String TAG = PrincipalActivity.class.getSimpleName() ;
    private final int MY_PERMISSION_REQUEST_CAMERA = 1001;
    public Fragment active ;
    private DrawerLayout drawer;
    private NavigationView navViewDrawer;
    private SharedPreferences prefs;
    private TextView email;
    private Menu nav_Menu;
    static String user_email;
    GoogleSignInClient mGoogleSignInClient;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);
        context = this;
        prefs = getSharedPreferences("user_details",MODE_PRIVATE);
        final BottomNavigationView navView = findViewById(R.id.nav_view);
        navViewDrawer = findViewById(R.id.nav_view_drawer);

        View headerView = navViewDrawer.getHeaderView(0);
        email = headerView.findViewById(R.id.email_msg);
        user_email = prefs.getString("username",null);

        nav_Menu = navViewDrawer.getMenu();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestId()
                .requestId()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(getContext(), gso);

        navViewDrawer.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId())
                {
                    case R.id.nav_scanner :
                        loadFragment(new BarcodeFragment());
                        navView.setSelectedItemId(R.id.navigation_scan);

                        break;
                    case R.id.nav_search :
                        loadFragment(new SearchFragment());
                        navView.setSelectedItemId(R.id.navigation_search);


                        break;
                    case R.id.nav_home :
                        loadFragment(new HomeFragment());
                        navView.setSelectedItemId(R.id.navigation_home);

                        break;
                    case R.id.nav_history :
                        loadFragment(new HistoryFragment());
                        navView.setSelectedItemId(R.id.navigation_history);

                        break;
                    case R.id.nav_list :
                        loadFragment(new YourListFragment());
                        navView.setSelectedItemId(R.id.navigation_list);

                        break;
                    case R.id.nav_profile :
                        //signOut();
                        loadFragment(new LoginFragment());
                       // navView.setSelectedItemId(R.id.navigation_home);

                        break;
                    case R.id.nav_logout :
                        signOut();
                        prefs.edit().clear().apply();
                        break;
                }

                drawer.closeDrawer(GravityCompat.START);
                return true;
            }
        });

        drawer = findViewById(R.id.container);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        {
            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);

            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                prefs = getSharedPreferences("user_details",MODE_PRIVATE);
                user_email = prefs.getString("username",null);


                Toast.makeText(context,"user 2 :"+ user_email,Toast.LENGTH_LONG).show();
                if(user_email != null)
                {
                    email.setText(user_email);
                    nav_Menu.findItem(R.id.nav_logout).setVisible(true);
                }
                else
                {
                    email.setText("Log in Now!");
                    nav_Menu.findItem(R.id.nav_logout).setVisible(false);
                }
            }
        };
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.frame_container,
                    new HomeFragment()).commit();
            navViewDrawer.setCheckedItem(R.id.nav_home);

        }


        navView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId())
                {
                    case R.id.navigation_scan :
                        loadFragment(new BarcodeFragment());
                        navViewDrawer.setCheckedItem(R.id.nav_scanner);

                        return true;
                    case R.id.navigation_search :
                        loadFragment(new SearchFragment());
                        navViewDrawer.setCheckedItem(R.id.nav_search);

                        return true;
                    case R.id.navigation_home :
                        loadFragment(new HomeFragment());
                        navViewDrawer.setCheckedItem(R.id.nav_home);

                        return true;
                    case R.id.navigation_history :
                        loadFragment(new HistoryFragment());
                        navViewDrawer.setCheckedItem(R.id.nav_history);

                        return true;
                    case R.id.navigation_list :
                        loadFragment(new YourListFragment());
                        navViewDrawer.setCheckedItem(R.id.nav_list);

                        return true;
                    default:
                        return false;
                }
            }

        });

    }

    /*@Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu = navViewDrawer.getMenu();
        if(user_email != null)
        {
            email.setText(user_email);
            nav_Menu.findItem(R.id.nav_logout).setVisible(true);
        }
        else
        {
            email.setText("Log in Now!");
            nav_Menu.findItem(R.id.nav_logout).setVisible(false);
        }
        return super.onPrepareOptionsMenu(menu);
    }

     */



    private void signOut() {
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(PrincipalActivity.this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(getContext(), "disconected", Toast.LENGTH_SHORT).show();

                    }
                });
    }


    private boolean loadFragment(Fragment fragment) {
        //switching fragment
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction().setCustomAnimations(R.anim.nav_default_pop_enter_anim ,R.anim.nav_default_pop_exit_anim)
                    .replace(R.id.frame_container, fragment)
                    .commit();
            return true;
        }
        return false;
    }

    public Context getContext() {
        return context;
    }

    @Override
    protected void onResume() {
        super.onResume();
        prefs = getSharedPreferences("user_details",MODE_PRIVATE);
        user_email = prefs.getString("username",null);

        if(user_email != null)
        {
            email.setText(user_email);
            nav_Menu.findItem(R.id.nav_logout).setVisible(true);
        }
        else
        {
            email.setText("Log in Now!");
            nav_Menu.findItem(R.id.nav_logout).setVisible(false);
        }
    }


    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(PrincipalActivity.this);
        builder.setMessage("Are You Sure? ")
                .setTitle(R.string.exit_title);
        builder.setPositiveButton(R.string.ok_title, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                PrincipalActivity.this.finish();
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });

        builder.show();

        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @Override
    public void onFragmentInteraction(Uri uri) {
        System.out.println(uri);
    }


}