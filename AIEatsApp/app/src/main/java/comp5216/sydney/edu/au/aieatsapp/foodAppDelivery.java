package comp5216.sydney.edu.au.aieatsapp;

import android.content.Intent;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class foodAppDelivery extends AppCompatActivity {

    private EditText changeNewPassword;
    private TextView welcome;
    private Button changePassword,logout;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_main_app);
        //set views
        changeNewPassword = (EditText)findViewById(R.id.new_password);
        welcome = (TextView) findViewById(R.id.welcome_user);
        changePassword = (Button)findViewById(R.id.btn_new_password);
        logout = (Button)findViewById(R.id.btn_logout);
        mAuth = FirebaseAuth.getInstance();
        //Session Check
        if (mAuth.getCurrentUser() != null){
            welcome.setText("Welcome, " + mAuth.getCurrentUser().getDisplayName());
        }
        //change password onclick listener
        changePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newPassword = changeNewPassword.getText().toString().trim();
                setNewPassword(newPassword);
            }
        });
        //logout onclick listener
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                if (mAuth.getCurrentUser() == null){
                    Toast.makeText(foodAppDelivery.this,"You have successfully logout !",
                            Toast.LENGTH_LONG).show();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            startActivity(new Intent(foodAppDelivery.this,LoginActivity.class));
                            finish();
                        }
                    },1000);
                }
            }
        });
    }

    private void setNewPassword(String newPassword) {
        FirebaseUser user = mAuth.getCurrentUser();
        user.updatePassword(newPassword).addOnCompleteListener(this, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Toast.makeText(foodAppDelivery.this,"You have successfully change " +
                            "your password!",Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
