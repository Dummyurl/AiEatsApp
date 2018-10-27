package comp5216.sydney.edu.au.aieatsapp;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import org.apache.commons.validator.routines.EmailValidator;

import java.util.ArrayList;

public class RegisterActivity extends AppCompatActivity {
    private EditText registerUsername;
    private EditText registerPassword;
    private EditText registerEmail;
    private EditText registerIdentityName;
    private EditText registerIdentityNum;
    private Button registerConfirm;
    private TextView backForSignIn;
    private FirebaseAuth mAuth;

    String userName,password,eMail,identityName,identityNum;

    //set room database
    ArrayList<customerIdentityFormat> items;
    CustomerIdentityDB db;
    CustomerIdentityDao customerIdentityDao;
    public int position=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        //set views
        backForSignIn = (TextView) findViewById(R.id.login_backWarning);
        registerUsername = (EditText) findViewById(R.id.register_input_name);
        registerPassword = (EditText) findViewById(R.id.register_input_password);
        registerIdentityName = (EditText) findViewById(R.id.register_input_identity_name);
        registerIdentityNum = (EditText) findViewById(R.id.register_input_identity_number);
        registerEmail = (EditText) findViewById(R.id.register_input_email);
        registerConfirm = (Button)findViewById(R.id.btn_register);
        //set Firebase
        mAuth = FirebaseAuth.getInstance();
        //set Room Database
        items = new ArrayList<customerIdentityFormat>();
        db = CustomerIdentityDB.getDatabase(this.getApplication().getApplicationContext());
        customerIdentityDao = db.customerIdentityDao();
        //set onclick listener of register confirm
        registerConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userName = registerUsername.getText().toString().trim();
                password = registerPassword.getText().toString().trim();
                eMail = registerEmail.getText().toString().trim();
                identityName = registerIdentityName.getText().toString().trim();
                identityNum = registerIdentityNum.getText().toString().trim();
                if (userName.isEmpty() || eMail.isEmpty() || password.isEmpty()){
                    Toast emptyMessage =  Toast.makeText(RegisterActivity.this,
                            "Your username, email, password or user type is empty, please re-enter " +
                                    "all these necessary fields."
                            ,Toast.LENGTH_LONG);
                    emptyMessage.show();
                }
                else if (password.length() < 6){
                    Toast shortPasswordMessage =  Toast.makeText(RegisterActivity.this,
                            "Your password is too short, please reset your " +
                                    "password with longer than 6 characters."
                            ,Toast.LENGTH_LONG);
                    shortPasswordMessage.show();
                }
                else if (EmailValidator.getInstance().isValid(eMail) == false){
                    Toast invalidEmailMessage =  Toast.makeText(RegisterActivity.this,
                            "Your email format is invalid, please re-enter a " +
                                    "valid email address."
                            ,Toast.LENGTH_LONG);
                    invalidEmailMessage.show();
                }
                else{
                    //sign up users
                    signUpUsers(eMail,password);
                }
            }
        });
        //set onclick listener of back Main Page
        backForSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterActivity.this,LoginActivity.class));
                finish();
            }
        });
    }

    private void signUpUsers(final String email, final String password){
        mAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()){
                            Toast errorMessage = Toast.makeText(RegisterActivity.this,
                                    "Sorry,your registration process has failed.",
                                    Toast.LENGTH_LONG);
                            errorMessage.show();
                        }
                        else{
                            userProfile();
                            Toast successfulMessage = Toast.makeText(RegisterActivity.this,
                                    "You have successfully registered!",Toast.LENGTH_LONG);
                            successfulMessage.show();
                            //save user information in database
                            customerIdentityFormat newCustomer = new customerIdentityFormat(userName,password,eMail,identityName,identityNum);
                            items.add(position,newCustomer);
                            saveCustomerItemIntoDatabase();
                        }
                    }
                });
    }

    private void userProfile() {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null){
            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                    .setDisplayName(userName).build();
            user.updateProfile(profileUpdates).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()){
                        Log.d("TESTING", "Username has set for register user.");
                    }
                }
            });
        }
    }

    private void saveCustomerItemIntoDatabase() {
        //Use asynchronous task to run query on the background to avoid locking UI
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                for (customerIdentityFormat todo : items) {
                    String editedUsername = todo.getUserName();
                    String editedPassword = todo.getUserPassword();
                    String editedEmail = todo.getUserEmail();
                    String editedIdentityName = todo.getUserIdentityName();
                    String editedIdentityPass = todo.getUserIdentityPassword();
                    CustomerIdentityItem item = new CustomerIdentityItem(editedUsername,editedPassword,editedEmail,
                            editedIdentityName,editedIdentityPass);
                    customerIdentityDao.insert(item);
                    Log.i("SQLite saved item", "ID: " + item.getCustomerID() + " Username: " + item.getCustomerUsername());
                }
                return null;
            }
        }.execute();
    }
}
