package comp5216.sydney.edu.au.aieatsapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

public class CustomerOrderList extends AppCompatActivity {
    ListView order;
    ArrayList<OrderFormat> orders;
    customerOrderAdapter orderAdapter;
    String orderID;
    OrderDB orderdb;
    OrderDao orderDao;
    Button back;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_order_list);
        //set Views
        order = (ListView)findViewById(R.id.orderView_customer);
        orders = new ArrayList<OrderFormat>();
        back = (Button)findViewById(R.id.btn_back_foodOrder);
        //init FireBase
        mAuth = FirebaseAuth.getInstance();
        //set order database
        orderdb = OrderDB.getDatabase(this.getApplication().getApplicationContext());
        orderDao = orderdb.foodOrderDao();
        //set Adapter
        orderAdapter = new customerOrderAdapter(this,orders);
        //read food from database
        readOrderItemsFromDatabase();
        order.setAdapter(orderAdapter);
        setupListViewListener();
        //back onclick listener
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CustomerOrderList.this,foodAppCustomer.class));
                finish();
            }
        });
    }

    private void setupListViewListener() {

        order.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long rowId) {
                Log.i("MainActivity", "Long Clicked item " + position);
                TextView selectedOrderStatus = (TextView)order.getChildAt(position).findViewById(R.id.foodOrderStatus);
                String orderStatus = selectedOrderStatus.getText().toString().trim();
                if (!orderStatus.equals("Delivered") || !orderStatus.equals("Cancelled")){
                    TextView selectedOrderID = (TextView)order.getChildAt(position).findViewById(R.id.foodOrderID);
                    orderID = selectedOrderID.getText().toString().trim().replace("Order ID: ","");
                    AlertDialog.Builder builder = new AlertDialog.Builder(CustomerOrderList.this);
                    builder.setTitle("Cancel Order")
                            .setMessage("Would you like to cancel this order?")
                            .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    setOrderInDatabaseCancelled(orderID);
                                    Toast.makeText(CustomerOrderList.this,"Your order has been cancelled!",
                                            Toast.LENGTH_SHORT).show();
                                }
                            })
                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    // User cancelled the dialog
                                    // Nothing happens
                                }
                            });
                    builder.create().show();
                }

                return true;
            }
        });
    }

    private void readOrderItemsFromDatabase() {
        //Use asynchronous task to run query on the background and wait for result
        try {
            new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... voids) {
                    //read items from database
                    String currentUser = mAuth.getCurrentUser().getDisplayName();
                    List<OrderItem> recordsFromDB = orderDao.listAll();
                    if (recordsFromDB != null & recordsFromDB.size() > 0) {
                        for (OrderItem item : recordsFromDB) {
                            String recordedUser = item.getOrderCustomer();
                            if (recordedUser.equals(currentUser)){
                                int recordedFoodID = item.getOrderID();
                                String recordFoodID = String.valueOf(recordedFoodID);
                                String recordedFoodName = item.getOrderFoodName();
                                String recordedFoodStatus = item.getOrderStatus();
                                orderAdapter.add(new OrderFormat(recordFoodID,recordedFoodName,recordedFoodStatus));
                                Log.i("SQLite read item", "ID: " + item.getOrderID() + " Name: " + item.getOrderFoodName());
                            }

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

    private void setOrderInDatabaseCancelled(final String orderNum) {
        //Use asynchronous task to run query on the background and wait for result
        try {
            new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... voids) {
                    int orderNumInt = Integer.parseInt(orderNum.replace("0",""));
                    List<OrderItem> recordsFromDB = orderDao.listAll();
                    if (recordsFromDB != null & recordsFromDB.size() > 0) {
                        for (OrderItem item : recordsFromDB) {
                            if (orderNumInt == item.getOrderID()){
                                orderDao.update("Cancelled",item.getOrderID());
                            }
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
