package comp5216.sydney.edu.au.aieatsapp;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import java.util.List;

public class CustomerProfile extends AppCompatActivity {
    private TextView Welcome;
    private EditText newUserName,newPass;
    private Button submit,back;
    private Boolean changeSuccess = false;

    private FirebaseAuth mAuth;
    //set database
    CustomerIdentityDB db;
    CustomerIdentityDao customerIdentityDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_profile);
        //set views
        Welcome = (TextView)findViewById(R.id.user_name);
        newUserName = (EditText)findViewById(R.id.input_new_username);
        newPass = (EditText)findViewById(R.id.input_new_customer_password);
        submit = (Button)findViewById(R.id.customer_profile_submit);
        back =(Button) findViewById(R.id.customer_profile_back);
        //Init FireBase
        mAuth = FirebaseAuth.getInstance();
        //set database
        db = CustomerIdentityDB.getDatabase(this.getApplication().getApplicationContext());
        customerIdentityDao = db.customerIdentityDao();
        //set welcome page
        //Session Check
        if (mAuth.getCurrentUser() != null){
            Welcome.setText("Welcome, " + mAuth.getCurrentUser().getDisplayName());
        }
        //set back listener
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CustomerProfile.this,foodAppCustomer.class));
                finish();
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newUsername = newUserName.getText().toString().trim();
                String newPassword = newPass.getText().toString().trim();
                if (newUsername.equals("") && newPassword.equals("")){
                    Toast.makeText(CustomerProfile.this,"Please edit your username or password!",
                            Toast.LENGTH_SHORT).show();
                }
                else {
                    if (!newUsername.equals("")){
                        changeUsernameInFireBase(newUsername);
                        if (changeSuccess == true){
                            changeUsernameInDatabase(newUsername);
                        }
                    }
                    if (!newPassword.equals("")){
                        changePasswordInFireBase(newPassword);
                        if (changeSuccess == true){
                            changePasswordInDatabase(newPassword);
                        }
                    }
                    Toast.makeText(CustomerProfile.this,"Your Profile has successfully updated!",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void changeUsernameInFireBase(String newUserName) {
        FirebaseUser user = mAuth.getCurrentUser();
        user.updateProfile(new UserProfileChangeRequest.Builder().setDisplayName(newUserName)
                .build()).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    changeSuccess = true;
                }
            }
        });
    }

    private void changeUsernameInDatabase(final String newUserName){
        //Use asynchronous task to run query on the background and wait for result
        try {
            new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... voids) {
                    String currentUserEmail = mAuth.getCurrentUser().getEmail();
                    //read items from database
                    List<CustomerIdentityItem> recordsFromDB = customerIdentityDao.listAll();
                    if (recordsFromDB != null & recordsFromDB.size() > 0) {
                        for (CustomerIdentityItem item : recordsFromDB) {
                            String userEmail = item.getCustomerEmail();
                            if (userEmail.equals(currentUserEmail)){
                                item.setCustomerUsername(newUserName);
                            }
                            Log.i("SQLite item changed", "Changed ID: " + item.getCustomerID());
                        }
                    }
                    return null;
                }
            }.execute().get();
        }
        catch(Exception ex) {
            Log.e("readItemsFromDatabase", ex.getStackTrace().toString());
        }
    }

    private void changePasswordInFireBase(String newPassword) {
        FirebaseUser user = mAuth.getCurrentUser();
        user.updatePassword(newPassword).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    changeSuccess = true;
                }
                else{
                    changeSuccess = false;
                }
            }
        });
    }

    private void changePasswordInDatabase(final String newPassword){
        //Use asynchronous task to run query on the background and wait for result
        try {
            new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... voids) {
                    String currentUserEmail = mAuth.getCurrentUser().getEmail();
                    //read items from database
                    List<CustomerIdentityItem> recordsFromDB = customerIdentityDao.listAll();
                    if (recordsFromDB != null & recordsFromDB.size() > 0) {
                        for (CustomerIdentityItem item : recordsFromDB) {
                            String userEmail = item.getCustomerEmail();
                            if (userEmail.equals(currentUserEmail)){
                                item.setCustomerPassword(newPassword);
                            }
                            Log.i("SQLite item changed", "Changed ID: " + item.getCustomerID());
                        }
                    }
                    return null;
                }
            }.execute().get();
        }
        catch(Exception ex) {
            Log.e("readItemsFromDatabase", ex.getStackTrace().toString());
        }
    }
}
