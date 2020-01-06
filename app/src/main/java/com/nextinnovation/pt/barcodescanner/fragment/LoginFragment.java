package com.nextinnovation.pt.barcodescanner.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.nextinnovation.pt.barcodescanner.R;
import com.nextinnovation.pt.barcodescanner.activity.PrincipalActivity;
import com.nextinnovation.pt.barcodescanner.database.DatabaseHelper;
import com.nextinnovation.pt.barcodescanner.model.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

import static android.content.Context.MODE_PRIVATE;


public class LoginFragment extends Fragment {

    static public GoogleSignInClient mGoogleSignInClient;
    private static final int RC_SIGN_IN = 9001;
    private ProgressDialog pDialog;
    EditText password;
    EditText email;
    private CallbackManager callbackManager;
    TextView sign_up;
    ImageView GoogleSignInButton;
    ImageView HideShowIcon;
    private SignUpFragment.OnFragmentInteractionListener mListener;
    Button SeConnecter_btn;
    RelativeLayout textInputLayoutEmail, textInputLayoutPasword;
    TextView error_message;
    SharedPreferences pref;
    ImageButton FbSignINnButton;


    DatabaseHelper databaseHelper;

    public LoginFragment() {
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof SignUpFragment.OnFragmentInteractionListener) {
            mListener = (SignUpFragment.OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(FacebookSdk.getApplicationContext());
        pref = getActivity().getSharedPreferences("user_details",MODE_PRIVATE);

        databaseHelper = new DatabaseHelper(getContext());
        pDialog = new ProgressDialog(getActivity());
        callbackManager = CallbackManager.Factory.create();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestId()
                .requestId()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(getContext(), gso);

    }

    @Override
    public void onStart() {
        super.onStart();
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(getContext());
        updateUI(account);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Connexion");
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        return view;

    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        GoogleSignInButton = view.findViewById(R.id.sign_in_google);
        HideShowIcon = view.findViewById(R.id.imageView_passwordIcon);
        SeConnecter_btn = view.findViewById(R.id.login_btn);
        password = view.findViewById(R.id.edit_password);
        email = view.findViewById(R.id.edit_email);
        textInputLayoutEmail = view.findViewById(R.id.relativeLayout1);
        textInputLayoutPasword = view.findViewById(R.id.relativeLayout2);
        error_message = view.findViewById(R.id.error_msg);
        FbSignINnButton =  view.findViewById(R.id.sign_in_facebook);
        sign_up = view.findViewById(R.id.Sign_up_link);



        HideShowIcon.setOnClickListener(new View.OnClickListener() {
            Boolean show = false;
            @Override
            public void onClick(View v) {
                if(!show)
                {
                    show = true;
                    HideShowIcon.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.eye));
                    password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());

                    // password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
                else
                {
                    show = false;
                    HideShowIcon.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.eye_off));
                    password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    //password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }

            }
        });



        facebook_sign_in();
        google_sign_in();
        Sign_up();
        basic_login();

    }



    private void google_sign_in(){
        GoogleSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayProgressDialog();
                Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, RC_SIGN_IN);
            }
        });
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);

        }

    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {

            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            SharedPreferences.Editor editor = pref.edit();
            editor.putString("username",account.getEmail());
            editor.putString("user_id",account.getId());
            editor.apply();

            updateUI(account);
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w("google services", "signInResult:failed code=" + e.getStatusCode());
            updateUI(null);
        }
    }


    private void updateUI(GoogleSignInAccount account) {
        hideProgressDialog();
        if(account != null)
        {
            replaceFragments(new HomeFragment());
        }

    }

    private void displayProgressDialog() {
        pDialog.setMessage("Logging In.. Please wait...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();

    }

    private void hideProgressDialog() {
        pDialog.dismiss();
    }


    private void useLoginInformation(AccessToken accessToken) {

        GraphRequest request = GraphRequest.newMeRequest(accessToken, new GraphRequest.GraphJSONObjectCallback() {
            //OnCompleted is invoked once the GraphRequest is successful
            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {
                try {
                    Log.i("user",": "+object);
                    Toast.makeText(getContext(),"user :"+ object,Toast.LENGTH_SHORT).show();
                    String name = object.getString("name");
                    String email = object.getString("email");
                    String id = object.getString("id");
                    Log.i("hna :","name: "+name +" , email :" +email+ " ,id :"+id);
                    SharedPreferences.Editor editor = pref.edit();
                    editor.putString("username",email);
                    editor.putString("user_id",id);
                    editor.apply();

                    Toast.makeText(getContext(), "Name : " + name + " Email :" +email, Toast.LENGTH_SHORT).show();
                    replaceFragments(new HomeFragment());
                    LoginManager.getInstance().logOut();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        // We set parameters to the GraphRequest using a Bundle.
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,email,picture.width(200)");
        request.setParameters(parameters);
        // Initiate the GraphRequest
        request.executeAsync();
    }

    private void Sign_up()
    {
        SignUpFragment signUpFragment = new SignUpFragment();
        sign_up.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                replaceFragments(signUpFragment);
            }


        });

    }

    public void replaceFragments(Fragment fragment) {

        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.setCustomAnimations(R.anim.enter_from_right,R.anim.exit_to_left);
        ft.replace(R.id.frame_container, fragment)
                .commit();
    }

    private void facebook_sign_in()
    {
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                AccessToken accessToken = loginResult.getAccessToken();
                Log.i("yeees","wllah");
                useLoginInformation(accessToken);
            }

            @Override
            public void onCancel() {
                Toast.makeText(getContext(),"Canceled",Toast.LENGTH_SHORT);
            }

            @Override
            public void onError(FacebookException exception) {
                Toast.makeText(getContext(),"Error connecting",Toast.LENGTH_SHORT);

            }
        });

        FbSignINnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginManager.getInstance().logInWithReadPermissions(getActivity(), Arrays.asList("public_profile", "user_friends"));

            }
        });
    }


    private void basic_login() {
        SeConnecter_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Check user input is correct or not
                if (validate()) {
                    //Get values from EditText fields
                    String Email = email.getText().toString();
                    String Password = password.getText().toString();

                    //Authenticate user
                    User currentUser = databaseHelper.Authenticate(new User(null, null, Email, Password));

                    //Check Authentication is successful or not
                    if (currentUser != null) {
                        replaceFragments(new HomeFragment());
                        SharedPreferences.Editor editor = pref.edit();
                        editor.putString("username",currentUser.email);
                        editor.putString("user_id",currentUser.id);
                        editor.apply();
                        Toast.makeText(getContext(),"Successfully Logged in!", Toast.LENGTH_SHORT).show();

                        //User Logged in Successfully Launch You home screen activity
                           /* Intent intent=new Intent(LoginActivity.this,HomeScreenActivity.class);
                            startActivity(intent);
                            finish();*/
                    } else {

                        //User Logged in Failed
                        error_message.setText("Failed to log in , please try again");
                    }
                }
            }
        });
    }


    private boolean validate() {
        boolean valid = false;

        //Get values from EditText fields
        String Email = email.getText().toString();
        String Password = password.getText().toString();


        //Handling validation for Password field
        if (Password.isEmpty() || Email.isEmpty()) {
            valid = false;
            error_message.setText("Field required!");
        } else {
            if (Password.length() > 5) {
                valid = true;
                error_message.setText(null);
                if (!Patterns.EMAIL_ADDRESS.matcher(Email).matches()) {
                    error_message.setText("Please enter valid email!");
                } else {
                    valid = true;
                    error_message.setText(null);
                }

            } else {
                valid = false;
                error_message.setText("Password is to short!");
            }
        }

        return valid;
    }

}



