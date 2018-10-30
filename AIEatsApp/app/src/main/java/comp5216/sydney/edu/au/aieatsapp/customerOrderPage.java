package comp5216.sydney.edu.au.aieatsapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class customerOrderPage extends AppCompatActivity {
    public int position = 0;
    private TextView foodName,foodPrice,foodDesc,foodProvider;
    private Button order,back;
    String editName,editPrice,editDesc,editProvider;
    LinearLayout orderForm;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_order_page);
        //set View
        foodName = (TextView)findViewById(R.id.foodInfo_name);
        foodPrice = (TextView)findViewById(R.id.foodInfo_price);
        foodDesc = (TextView)findViewById(R.id.foodInfo_desc);
        foodProvider = (TextView)findViewById(R.id.foodInfo_provider);
        order = (Button)findViewById(R.id.btn_addOrder);
        back = (Button)findViewById(R.id.btn_backOrder);
        orderForm = new LinearLayout(customerOrderPage.this);
        orderForm.setOrientation(LinearLayout.VERTICAL);
        //get text view value
        editName = getIntent().getStringExtra("Name");
        editPrice = getIntent().getStringExtra("Price");
        editDesc = getIntent().getStringExtra("Desc");
        editProvider = getIntent().getStringExtra("Provider");
        position = getIntent().getIntExtra("position",-1);
        //setValue
        foodName.setText(editName);
        foodPrice.setText(editPrice);
        foodDesc.setText(editDesc);
        foodProvider.setText(editProvider);
        //set back on click listener
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(customerOrderPage.this,CustomerFoodCourt.class));
                finish();
            }
        });
        //set order onclick listener
        order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText setDeliveryAddress = new EditText(customerOrderPage.this);
                final EditText setDeliveryPhone = new EditText(customerOrderPage.this);
                setDeliveryAddress.setHint("Enter your home address");
                setDeliveryPhone.setHint("Enter your contact number");
                orderForm.addView(setDeliveryAddress);
                orderForm.addView(setDeliveryPhone);
                AlertDialog.Builder builder = new AlertDialog.Builder(customerOrderPage.this);
                builder.setTitle(R.string.create_order_title)
                        .setMessage(R.string.create_order_msg)
                        .setView(orderForm)
                        .setPositiveButton(R.string.create, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogInterface, int i) {
                                String orderAddress = setDeliveryAddress.getText().toString().trim();
                                String orderPhone = setDeliveryPhone.getText().toString().trim();
                                if (orderAddress.equals("") || orderPhone.equals("")){
                                    Toast.makeText(customerOrderPage.this,"Your " +
                                            "order detail is not complete, Please fill in your " +
                                            "delivery address and contact number.",Toast.LENGTH_LONG).show();
                                }
                                else{
                                    Intent data = new Intent(customerOrderPage.this,CustomerFoodCourt.class);
                                    data.putExtra("Name",editName);
                                    data.putExtra("Address",orderAddress);
                                    data.putExtra("Phone",orderPhone);
                                    data.putExtra("Provider",editProvider.replace("Provider: ",""));
                                    data.putExtra("position",0);

                                    setResult(RESULT_OK,data);
                                    finish();
                                }
                            }
                        })
                        .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogInterface, int i) {
                                // User cancelled the dialog
                                // Nothing happens
                            }
                        });
                builder.create().show();
            }
        });
    }
}
