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

public class foodAppCustomer extends AppCompatActivity {
    private TextView welcome;
    private Button edit_profile,logout,foodCourt,check_order;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_app_customer);
        //set views
        edit_profile = (Button) findViewById(R.id.edit_profile_customer);
        welcome = (TextView) findViewById(R.id.welcome_user);
        logout = (Button)findViewById(R.id.btn_logout);
        foodCourt = (Button) findViewById(R.id.food_court_customer);
        check_order = (Button)findViewById(R.id.manage_order_customer);
        mAuth = FirebaseAuth.getInstance();
        //Session Check
        if (mAuth.getCurrentUser() != null){
            welcome.setText("Welcome, " + mAuth.getCurrentUser().getDisplayName());
        }
        //edit profile onclick listener
        edit_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(foodAppCustomer.this,CustomerProfile.class));
            }
        });
        //food court onclick listener
        foodCourt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(foodAppCustomer.this,CustomerFoodCourt.class));
            }
        });
        check_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(foodAppCustomer.this,CustomerOrderList.class));
            }
        });
        //logout onclick listener
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                if (mAuth.getCurrentUser() == null){
                    Toast.makeText(foodAppCustomer.this,"You have successfully logout !",
                            Toast.LENGTH_LONG).show();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            startActivity(new Intent(foodAppCustomer.this,LoginActivity.class));
                            finish();
                        }
                    },1000);
                }
            }
        });
    }
}
