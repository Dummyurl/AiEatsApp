package comp5216.sydney.edu.au.aieatsapp;

import android.content.Intent;
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

public class ForgotPassword extends AppCompatActivity {
    private EditText registeredEmail;
    private Button resetPass;
    private TextView back;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        //set Views
        registeredEmail = (EditText)findViewById(R.id.input_registered_email);
        resetPass = (Button)findViewById(R.id.btn_send_reset_email);
        back = (TextView)findViewById(R.id.btn_reset_back);
        //Init FireBase
        mAuth = FirebaseAuth.getInstance();
        //Reset password onclick listener
        resetPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = registeredEmail.getText().toString().trim();
                resetPassword(email);
            }
        });
        //back onclick listener
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ForgotPassword.this,LoginActivity.class));
                finish();
            }
        });
    }

    private void resetPassword(final String email) {
        mAuth.sendPasswordResetEmail(email).addOnCompleteListener(this, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast successfulMessage = Toast.makeText(ForgotPassword.this,
                            "Your reset password email has been successfully sent to " +
                                   email + ". " + "Please check your email!",Toast.LENGTH_LONG);
                    successfulMessage.show();
                }
                else{
                    Toast errorMessage = Toast.makeText(ForgotPassword.this,
                            "Sorry,your reset password process has failed, " +
                                    "Please try again.",
                            Toast.LENGTH_LONG);
                    errorMessage.show();
                }
            }
        });
    }
}
