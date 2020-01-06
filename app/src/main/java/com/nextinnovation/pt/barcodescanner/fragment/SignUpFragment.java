package com.nextinnovation.pt.barcodescanner.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.nextinnovation.pt.barcodescanner.R;
import com.nextinnovation.pt.barcodescanner.database.DatabaseHelper;
import com.nextinnovation.pt.barcodescanner.model.User;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SignUpFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SignUpFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SignUpFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    EditText editTextUserName;
    EditText editTextEmail;
    EditText editTextPassword;
    Button Register_btn;
    DatabaseHelper databaseHelper;
    TextView messages,back;
    ImageView HideShowIcon;


    private OnFragmentInteractionListener mListener;

    public SignUpFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SignUpFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SignUpFragment newInstance(String param1, String param2) {
        SignUpFragment fragment = new SignUpFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        databaseHelper = new DatabaseHelper(getContext());

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        editTextEmail = view.findViewById(R.id.email_text);
        editTextPassword = view.findViewById(R.id.password_text);
        editTextUserName = view.findViewById(R.id.username_text);
        Register_btn = view.findViewById(R.id.register_btn);
        messages = view.findViewById(R.id.messages);
        HideShowIcon = view.findViewById(R.id.pass_show_hide);
        back = view.findViewById(R.id.back_to_login);

        inscription();
        Login();
        show_hide_pass();

        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Inscription");
        return inflater.inflate(R.layout.fragment_sign_up, container, false);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    private void inscription(){
        Register_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validate()) {
                    String UserName = editTextUserName.getText().toString();
                    String Email = editTextEmail.getText().toString();
                    String Password = editTextPassword.getText().toString();

                    //Check in the database is there any user associated with  this email
                    if (!databaseHelper.isEmailExists(Email)) {

                        //Email does not exist now add new user to database
                        databaseHelper.addUser(new User(null, UserName, Email, Password));
                        Toast.makeText(getContext(),"User created successfully! Please Login ",Toast.LENGTH_SHORT).show();
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                //finish();
                            }
                        }, Snackbar.LENGTH_LONG);
                    }else {

                        //Email exists with email input provided so show error user already exist
                        Snackbar.make(Register_btn, "User already exists with same email ", Snackbar.LENGTH_LONG).show();
                    }


                }
            }
        });
    }

    private boolean validate() {
        boolean valid ;

        //Get values from EditText fields
        String UserName = editTextUserName.getText().toString();
        String Email = editTextEmail.getText().toString();
        String Password = editTextPassword.getText().toString();

        //Handling validation for UserName field

        if (UserName.isEmpty() || Password.isEmpty() || Email.isEmpty()) {
            Log.i("hana","khawi");
            valid = false;
            messages.setText("Field is empty");
        } else {
            if (UserName.length() > 5 && Password.length() > 5 ) {
                messages.setText(null);
                if (!android.util.Patterns.EMAIL_ADDRESS.matcher(Email).matches()) {
                    valid = false;
                    messages.setText("Please enter valid email!");
                } else {
                    valid = true;
                    messages.setText(null);
                }
            } else {
                valid = false;
                messages.setText("Username or Password is too short!");
            }
        }

            /*

            //Handling validation for Password field
            if (Password.isEmpty()) {
                valid = false;
                messages.setText("Please enter valid password!");
            }
            }*/





        //Handling validation for Email field


        return valid;
    }

    private void show_hide_pass(){
        HideShowIcon.setOnClickListener(new View.OnClickListener() {
            Boolean show = false;
            @Override
            public void onClick(View v) {
                if(!show)
                {
                    show = true;
                    HideShowIcon.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.eye));
                    editTextPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());

                    // password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
                else
                {
                    show = false;
                    HideShowIcon.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.eye_off));
                    editTextPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    //password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }

            }
        });
    }

    private void Login()
    {
        LoginFragment loginFragment = new LoginFragment();
        back.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                replaceFragments(loginFragment);
            }


        });

    }


    public void replaceFragments(Fragment fragment) {

        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.setCustomAnimations(R.anim.enter_from_right,R.anim.exit_to_left,R.anim.enter_from_left, R.anim.exit_to_right);
        ft.replace(R.id.frame_container, fragment)
                .commit();
    }
}
