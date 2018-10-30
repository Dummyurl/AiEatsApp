package comp5216.sydney.edu.au.aieatsapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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

public class deliveryOrderList extends AppCompatActivity {
    ListView order;
    ArrayList<OrderFormat> orders;
    deliveryOrderAdapter deliveryAdapter;
    OrderDB orderdb;
    OrderDao orderDao;
    Button back;
    FirebaseAuth mAuth;
    String orderID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_order_list);
        //set Views
        order = (ListView)findViewById(R.id.orderView_delivery);
        orders = new ArrayList<OrderFormat>();
        back = (Button)findViewById(R.id.btn_back_foodDelivery);
        //init FireBase
        mAuth = FirebaseAuth.getInstance();
        //set order database
        orderdb = OrderDB.getDatabase(this.getApplication().getApplicationContext());
        orderDao = orderdb.foodOrderDao();
        //set Adapter
        deliveryAdapter = new deliveryOrderAdapter(this,orders);
        //read food from database
        readOrderItemsFromDatabase();
        order.setAdapter(deliveryAdapter);
        //Set up onclick Listener
        setupListViewListener();
        //back onclick listener
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(deliveryOrderList.this,foodAppDelivery.class));
                finish();
            }
        });
    }

    private void setupListViewListener() {
        order.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                Log.i("MainActivity", "Long Clicked item " + position);
                TextView selectedOrderID = (TextView)order.getChildAt(position).findViewById(R.id.foodOrderID);
                orderID = selectedOrderID.getText().toString().trim().replace("Order ID: ","");
                AlertDialog.Builder builder = new AlertDialog.Builder(deliveryOrderList.this);
                builder.setTitle("Delivery Selection")
                        .setMessage("Would you like to deliver this good?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogInterface, int i) {
                                setDeliverInDatabaseReady(orderID);
                                Toast.makeText(deliveryOrderList.this,"Your order has been delivered!",
                                        Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogInterface, int i) {
                                // User cancelled the dialog
                                // Nothing happens
                            }
                        });
                builder.create().show();
            }
        });

        order.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long rowId) {
                Log.i("MainActivity", "Long Clicked item " + position);
                TextView selectedOrderStatus = (TextView)order.getChildAt(position).findViewById(R.id.foodOrderStatus);
                String orderStatus = selectedOrderStatus.getText().toString().trim();
                if (orderStatus.equals("On Delivery")){
                    TextView selectedOrderID = (TextView)order.getChildAt(position).findViewById(R.id.foodOrderID);
                    orderID = selectedOrderID.getText().toString().trim().replace("Order ID: ","");
                    AlertDialog.Builder builder = new AlertDialog.Builder(deliveryOrderList.this);
                    builder.setTitle("Finish Delivery")
                            .setMessage("Are you sure to finish this delivery?")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    setFinishInDatabaseReady(orderID);
                                    Toast.makeText(deliveryOrderList.this,"You have confirmed to receive your order!",
                                            Toast.LENGTH_SHORT).show();
                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
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
                    List<OrderItem> recordsFromDB = orderDao.listAll();
                    if (recordsFromDB != null & recordsFromDB.size() > 0) {
                        for (OrderItem item : recordsFromDB) {
                            String recordedFoodStatus = item.getOrderStatus();
                            if (recordedFoodStatus.toLowerCase().equals("ready") || recordedFoodStatus.equals("On Delivery")){
                                int recordedFoodID = item.getOrderID();
                                String recordFoodID = String.valueOf(recordedFoodID);
                                String recordedFoodName = item.getOrderFoodName();
                                String recordedFoodAddress = item.getOrderAddress();
                                String recordedFoodPhone = item.getOrderPhone();
                                deliveryAdapter.add(new OrderFormat(recordFoodID,recordedFoodName,recordedFoodPhone,
                                        recordedFoodAddress,recordedFoodStatus));
                                Log.i("SQLite read item", "ID: " + item.getOrderID() + " Name: " + item.getOrderFoodName());
                            }
                        }
                    }
                    return null;
                }
            }.execute().get();
        }catch(Exception ex) {
            Log.e("readItemsFromDatabase", ex.getStackTrace().toString());
        }
    }

    private void setDeliverInDatabaseReady(final String orderNum) {
        //Use asynchronous task to run query on the background and wait for result
        try {
            new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... voids) {
                    int orderNumInt = Integer.parseInt(orderNum);
                    List<OrderItem> recordsFromDB = orderDao.listAll();
                    if (recordsFromDB != null & recordsFromDB.size() > 0) {
                        for (OrderItem item : recordsFromDB) {
                            if (orderNumInt == item.getOrderID()){
                                orderDao.update("On Delivery",orderNumInt);
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

    private void setFinishInDatabaseReady(final String orderNum) {
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
                                orderDao.update("Delivered",item.getOrderID());
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
