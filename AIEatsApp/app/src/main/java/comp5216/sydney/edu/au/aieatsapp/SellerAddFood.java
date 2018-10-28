package comp5216.sydney.edu.au.aieatsapp;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class SellerAddFood extends AppCompatActivity {
    public int position = 0;
    private EditText food_name,food_price,food_desc;
    private Button food_add,back;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_add);
        //set Views
        food_name = (EditText) findViewById(R.id.input_food_name);
        food_price = (EditText) findViewById(R.id.input_food_price);
        food_desc = (EditText) findViewById(R.id.input_food_desc);
        food_add = (Button)findViewById(R.id.submit_added_food);
        back = (Button)findViewById(R.id.food_add_back);
        //set Value
        String editName = getIntent().getStringExtra("Name");
        String editPrice = getIntent().getStringExtra("Price");
        String editDesc = getIntent().getStringExtra("Desc");
        position = getIntent().getIntExtra("position",-1);
        food_name.setText(editName);
        food_price.setText(editPrice);
        food_desc.setText(editDesc);
        //set onclick listener of back
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SellerAddFood.this,SellerFoodList.class));
                finish();
            }
        });
        food_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String new_food_name = food_name.getText().toString().trim();
                String new_food_price = food_price.getText().toString().trim();
                String new_food_desc = food_desc.getText().toString().trim();
                if (new_food_name.equals("")){
                    Toast.makeText(SellerAddFood.this,"Your food name is empty, please add your" +
                            "food name.",Toast.LENGTH_SHORT).show();
                }
                else if (new_food_price.equals("")){
                    Toast.makeText(SellerAddFood.this,"Your food hasn't set a price, please add your" +
                            "food price.",Toast.LENGTH_SHORT).show();
                }
                else if (new_food_desc.equals("")){
                    Toast.makeText(SellerAddFood.this,"Your food hasn't set a description, please add your" +
                            "food description.",Toast.LENGTH_SHORT).show();
                }
                else{
                    Intent data = new Intent();
                    data.putExtra("Name",new_food_name);
                    data.putExtra("Price",new_food_price);
                    data.putExtra("Desc",new_food_desc);
                    data.putExtra("position",0);
                    setResult(RESULT_OK,data);
                    finish();
                }
            }
        });
    }
}
