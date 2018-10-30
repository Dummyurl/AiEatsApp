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

public class CustomerFoodCourt extends AppCompatActivity {
    ListView food;
    ArrayList<foodFormat> foodRecords;
    ArrayList<OrderFormat> orders;
    customerFoodAdapter foodAdapter;
    FoodDB db;
    foodDao foodDao;
    OrderDB orderdb;
    OrderDao orderDao;
    Button back;
    FirebaseAuth mAuth;

    public final int EDIT_ITEM_REQUEST_CODE_1 = 992;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_food_court);
        //set Views
        food = (ListView)findViewById(R.id.foodView_footCourt);
        foodRecords = new ArrayList<foodFormat>();
        orders = new ArrayList<OrderFormat>();
        back = (Button)findViewById(R.id.btn_back_foodCourt);
        //init FireBase
        mAuth = FirebaseAuth.getInstance();
        //set food Database
        db = FoodDB.getDatabase(this.getApplication().getApplicationContext());
        foodDao = db.foodDao();
        //set order database
        orderdb = OrderDB.getDatabase(this.getApplication().getApplicationContext());
        orderDao = orderdb.foodOrderDao();
        //set Adapter
        foodAdapter = new customerFoodAdapter(this,foodRecords);
        //read food from database
        readFoodItemsFromDatabase();
        food.setAdapter(foodAdapter);
        //Set List View Listener
        setupListViewListener();
        //back onclick listener
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CustomerFoodCourt.this,foodAppSeller.class));
                finish();
            }
        });
    }

    private void setupListViewListener() {
        food.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView selectedFoodNamePlace = (TextView)food.getChildAt(position).findViewById(R.id.foodName);
                String selectedFoodName = selectedFoodNamePlace.getText().toString().trim();
                TextView selectedFoodPricePlace = (TextView)food.getChildAt(position).findViewById(R.id.foodPrice);
                String selectedFoodPrice = selectedFoodPricePlace.getText().toString().trim();
                TextView selectedFoodDescPlace = (TextView)food.getChildAt(position).findViewById(R.id.foodDescription);
                String selectedFoodDesc = selectedFoodDescPlace.getText().toString().trim();
                TextView selectedFoodProviderPlace = (TextView)food.getChildAt(position).findViewById(R.id.foodProvider);
                String selectedFoodProvider = selectedFoodProviderPlace.getText().toString().trim();

                Log.i("CustomerFoodCourt", "Clicked item " + position);

                Intent intent = new Intent(CustomerFoodCourt.this, customerOrderPage.class);
                if (intent != null) {
                    // put "extras" into the bundle for access in the edit activity
                    intent.putExtra("Name", selectedFoodName);
                    intent.putExtra("Price",selectedFoodPrice);
                    intent.putExtra("Desc", selectedFoodDesc);
                    intent.putExtra("Provider", selectedFoodProvider);
                    intent.putExtra("position", position);
                    // brings up the second activity
                    startActivityForResult(intent, EDIT_ITEM_REQUEST_CODE_1);
                    foodAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == EDIT_ITEM_REQUEST_CODE_1 && resultCode == RESULT_OK){
            // Extract name value from result extras
            String orderCustomer = mAuth.getCurrentUser().getDisplayName();
            String orderFoodName = data.getExtras().getString("Name");
            String orderPhone = data.getExtras().getString("Phone");
            String orderAddress = data.getExtras().getString("Address");
            String orderStatus = "Confirmed";
            String orderProvider = data.getExtras().getString("Provider");
            OrderFormat newOrder = new OrderFormat(orderCustomer,orderFoodName,orderPhone,orderAddress,
                    orderStatus,orderProvider);
            int position = data.getIntExtra("position", -1);
            orders.add(position,newOrder);
            Log.i("Updated Item in list:", newOrder + ",position:" + position);
            Toast.makeText(this, "updated:" + newOrder, Toast.LENGTH_SHORT).show();
            saveOrdersToDatabase();
            Toast.makeText(CustomerFoodCourt.this,"Your order has been successfully created!",
                    Toast.LENGTH_SHORT).show();
        }
    }

    private void readFoodItemsFromDatabase() {
        //Use asynchronous task to run query on the background and wait for result
        try {
            new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... voids) {
                    //read items from database
                    List<FoodItem> recordsFromDB = foodDao.listAll();
                    if (recordsFromDB != null & recordsFromDB.size() > 0) {
                        for (FoodItem item : recordsFromDB) {
                            String recordedProvider = item.getFoodProvider();
                            String recordedFoodName = item.getFoodName();
                            String recordedFoodPrice = item.getFoodPrice();
                            String recordedFoodDesc = item.getFoodDescription();
                            foodAdapter.add(new foodFormat(recordedFoodName,recordedFoodPrice,recordedFoodDesc,recordedProvider));
                            Log.i("SQLite read item", "ID: " + item.getFoodID() + " Name: " + item.getFoodName());
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

    private void saveOrdersToDatabase() {
        //Use asynchronous task to run query on the background to avoid locking UI
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                for (OrderFormat todo : orders) {
                    String newOrderFoodName = todo.getOrderFoodName();
                    String newOrderCustomer = todo.getOrderCustomer();
                    String newOrderPhone = todo.getOrderPhone();
                    String newOrderAddress = todo.getOrderAddress();
                    String newOrderStatus = todo.getOrderStatus();
                    String newOrderProvider = todo.getOrderProvider();
                    OrderItem item = new OrderItem(newOrderFoodName,newOrderCustomer,newOrderPhone,
                            newOrderAddress,newOrderStatus,newOrderProvider);
                    orderDao.insert(item);
                    Log.i("SQLite saved item", "ID: " + item.getOrderID() + " Name: " + item.getOrderFoodName());
                }
                return null;
            }
        }.execute();
    }
}
