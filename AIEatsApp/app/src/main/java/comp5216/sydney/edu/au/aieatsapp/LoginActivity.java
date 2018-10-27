package comp5216.sydney.edu.au.aieatsapp;

import android.content.Intent;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {
    private Spinner userTypeSelection;
    private EditText loginEmail;
    private EditText loginPassword;
    private Button login;
    private TextView signInWithStudentCard;
    private TextView register;
    private TextView forgotPassWord;
    //Firebase setup
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private String email,password,userType;
    private final String TAG = "LoginActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //set views
        register = (TextView) findViewById(R.id.login_register);
        loginEmail = (EditText) findViewById(R.id.login_input_email);
        loginPassword = (EditText) findViewById(R.id.login_input_password);
        forgotPassWord = (TextView) findViewById(R.id.login_forgetPassword);
        signInWithStudentCard = (TextView) findViewById(R.id.login_loginByFace);
        login = (Button)findViewById(R.id.btn_login);
        userTypeSelection = (Spinner)findViewById(R.id.userTypeSelection);
        //Set up FireBase
        mAuth = FirebaseAuth.getInstance();
        //Set up firebase state listener
        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null){
                    //User is signed in
                    Log.d(TAG,"OnAuthStateChanged:signed_in" + user.getUid());
                    Toast.makeText(LoginActivity.this,"Successfully signed in with: "
                    + user.getEmail(),Toast.LENGTH_LONG).show();
                    startActivity(new Intent(LoginActivity.this,foodAppCustomer.class));
                    finish();
                }
            }
        };
        //Set up Spinner
        ArrayAdapter<CharSequence> userTypeAdapter = ArrayAdapter.createFromResource(this,
                R.array.user_type_array,android.R.layout.simple_spinner_dropdown_item);
        userTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        userTypeSelection.setAdapter(userTypeAdapter);
        //Set onclick listener of registration
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this,RegisterActivity.class));
            }
        });
        //set onclick listener of forgot password
        forgotPassWord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this,ForgotPassword.class));
                finish();
            }
        });
        //set onclick listener for signInByStudentCard
        signInWithStudentCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this,identityCardLoginActivity.class));
            }
        });
        //set onclick listener of login button
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = loginEmail.getText().toString().trim();
                password = loginPassword.getText().toString().trim();
                userType = userTypeSelection.getSelectedItem().toString().trim();
                if (email.isEmpty() || password.isEmpty() || userType.equals("You Will Login As...")){
                    Toast emptyMessage =  Toast.makeText(LoginActivity.this,
                            "Your email or username is empty, please type your correct " +
                                    "username and password."
                            ,Toast.LENGTH_LONG);
                    emptyMessage.show();
                }
                else{
                    loginUser(email,password);
                }
            }
        });

    }

    @Override
    public void onStart(){
        super.onStart();
        mAuth.addAuthStateListener(mAuthStateListener);
    }

    @Override
    public void onStop(){
        super.onStop();
        mAuth.removeAuthStateListener(mAuthStateListener);
    }

    private void loginUser(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()){
                            Toast resetImage =  Toast.makeText(LoginActivity.this,
                                    "Your username or password is incorrect, please check again."
                            ,Toast.LENGTH_LONG);
                            resetImage.show();
                        }
                        else{
                            Toast successImage =  Toast.makeText(LoginActivity.this,
                                    "You have successfully sign in!"
                                    ,Toast.LENGTH_LONG);
                            successImage.show();
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    if (userType.equals("Customer")){
                                        startActivity(new Intent(LoginActivity.this,foodAppCustomer.class));
                                        finish();
                                    }
                                    else if (userType.equals("Seller")){
                                        startActivity(new Intent(LoginActivity.this,foodAppSeller.class));
                                        finish();
                                    }
                                    else if (userType.equals("Delivery Driver")){
                                        startActivity(new Intent(LoginActivity.this,foodAppSeller.class));
                                        finish();
                                    }
                                }
                            },1000);
                        }
                    }
                });
    }
}
