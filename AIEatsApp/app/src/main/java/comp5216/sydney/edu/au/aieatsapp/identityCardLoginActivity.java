package comp5216.sydney.edu.au.aieatsapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.text.FirebaseVisionText;
import com.google.firebase.ml.vision.text.FirebaseVisionTextRecognizer;
import com.google.firebase.ml.vision.text.RecognizedLanguage;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

public class identityCardLoginActivity extends AppCompatActivity {
    private Button textReco;
    private Spinner userTypeSelection_icLogin;
    //private String scannedText;
    private final int TEXT_RECO_REQ_CODE = 100;
    //set database
    private CustomerIdentityDB db;
    private CustomerIdentityDao customerIdentityDao;
    //Init Authentication
    private FirebaseAuth mAuth;
    String userType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_card_login);
        //set Views
        textReco = (Button)findViewById(R.id.textReco);
        userTypeSelection_icLogin = (Spinner) findViewById(R.id.userTypeSelection_icLogin);
        //set database
        db = CustomerIdentityDB.getDatabase(this.getApplication().getApplicationContext());
        customerIdentityDao = db.customerIdentityDao();
        //Set up Spinner
        ArrayAdapter<CharSequence> userTypeAdapter = ArrayAdapter.createFromResource(this,
                R.array.user_type_array,android.R.layout.simple_spinner_dropdown_item);
        userTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        userTypeSelection_icLogin.setAdapter(userTypeAdapter);
        //set Firebase
        mAuth = FirebaseAuth.getInstance();
        textReco.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userType = userTypeSelection_icLogin.getSelectedItem().toString().trim();
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if(intent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(intent, TEXT_RECO_REQ_CODE);
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == TEXT_RECO_REQ_CODE){
            if (resultCode == RESULT_OK){ //Successfully Capture Image
                if (data != null && data.getExtras() != null){
                    Bitmap photo = (Bitmap)data.getExtras().get("data");
                    textRecognition(photo);
                }
            } else if (resultCode == RESULT_CANCELED){
                Toast.makeText(this,"Operation Cancelled By User",Toast.LENGTH_SHORT).show();
            } else{
                Toast.makeText(this,"Failed to Capture Image",Toast.LENGTH_SHORT).show();
            }

        }
    }

    private void textRecognition(Bitmap photo) {
        FirebaseVisionImage image = FirebaseVisionImage.fromBitmap(photo);
        FirebaseVisionTextRecognizer textRecognizer = FirebaseVision.getInstance()
                .getOnDeviceTextRecognizer();
        textRecognizer.processImage(image)
                .addOnSuccessListener(new OnSuccessListener<FirebaseVisionText>() {
                    @Override
                    public void onSuccess(FirebaseVisionText result) {
                        String resultText = result.getText();
                        Toast.makeText(identityCardLoginActivity.this,
                                "Recognized Text is: " + resultText,Toast.LENGTH_SHORT)
                                .show();
                        for (FirebaseVisionText.TextBlock block: result.getTextBlocks()) {
                            String blockText = block.getText();
                            Float blockConfidence = block.getConfidence();
                            List<RecognizedLanguage> blockLanguages = block.getRecognizedLanguages();
                            Point[] blockCornerPoints = block.getCornerPoints();
                            Rect blockFrame = block.getBoundingBox();
                            for (FirebaseVisionText.Line line: block.getLines()) {
                                String lineText = line.getText();
                                Float lineConfidence = line.getConfidence();
                                List<RecognizedLanguage> lineLanguages = line.getRecognizedLanguages();
                                Point[] lineCornerPoints = line.getCornerPoints();
                                Rect lineFrame = line.getBoundingBox();
                                //scannedText += lineText + " ";
                            }
                        }
                        //scanText.setText(resultText);
                        //System.out.println(resultText);
                        if (resultText != null){
                            checkRecordWithDatabase(resultText);
                        }
                    }
                })
                .addOnFailureListener(
                        new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(identityCardLoginActivity.this,
                                        "Failed to Recognize Text",Toast.LENGTH_SHORT)
                                        .show();
                            }
                        });
    }

    private void checkRecordWithDatabase(final String resultText) {
        //Use asynchronous task to run query on the background and wait for result
        try {
            new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... voids) {
                    Boolean recordsMatched = false;
                    //read items from database
                    List<CustomerIdentityItem> recordsFromDB = customerIdentityDao.listAll();
                    if (recordsFromDB != null & recordsFromDB.size() > 0) {
                        for (CustomerIdentityItem item : recordsFromDB) {
                            String userIdentityName = item.getCustomerIdentityName();
                            String userIdentityNum = item.getCustomerIdentityNum();
                            String userEmail = item.getCustomerEmail();
                            String userPassword = item.getCustomerPassword();
                            if (resultText.toLowerCase().contains(userIdentityName.toLowerCase()) &&
                                    resultText.contains(userIdentityNum)){
                                loginUser(userEmail,userPassword);
                                recordsMatched = true;
                            }
                            Log.i("SQLite read item", "Scanned ID: " + item.getCustomerID());
                        }
                        if (recordsMatched == false){
                            Toast.makeText(identityCardLoginActivity.this,
                                    "Your identity card doesn't match our details, please " +
                                            "upload your identity card again.",Toast.LENGTH_SHORT)
                                    .show();
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

    private void loginUser(String userEmail, String userPassword) {
        mAuth.signInWithEmailAndPassword(userEmail, userPassword)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()) {
                            Toast resetImage = Toast.makeText(identityCardLoginActivity.this,
                                    "Your username or password is incorrect, please check again."
                                    , Toast.LENGTH_LONG);
                            resetImage.show();
                        } else {
                            Toast successImage = Toast.makeText(identityCardLoginActivity.this,
                                    "You have successfully sign in!"
                                    , Toast.LENGTH_LONG);
                            successImage.show();
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    if (userType.equals("Customer")) {
                                        startActivity(new Intent(identityCardLoginActivity.this, foodAppCustomer.class));
                                        finish();
                                    } else if (userType.equals("Seller")) {
                                        startActivity(new Intent(identityCardLoginActivity.this, foodAppSeller.class));
                                        finish();
                                    } else if (userType.equals("Delivery Driver")) {
                                        startActivity(new Intent(identityCardLoginActivity.this, foodAppSeller.class));
                                        finish();
                                    }
                                }
                            }, 1000);
                        }
                    }
                });
    }
}
