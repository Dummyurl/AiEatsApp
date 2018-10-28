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
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class SellerFoodList extends AppCompatActivity {
    ListView food;
    ArrayList<foodFormat> foodRecords;
    sellerFoodAdapter foodAdapter;
    FoodDB db;
    foodDao foodDao;
    FirebaseAuth mAuth;
    Button back,addFood;

    public final int EDIT_ITEM_REQUEST_CODE_1 = 647;
    public final int EDIT_ITEM_REQUEST_CODE_2 = 992;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_food_list);
        //set Views & array
        food = (ListView)findViewById(R.id.foodView);
        foodRecords = new ArrayList<foodFormat>();
        back = (Button) findViewById(R.id.btn_back);
        addFood = (Button)findViewById(R.id.btn_addFood);
        //set Database
        db = FoodDB.getDatabase(this.getApplication().getApplicationContext());
        foodDao = db.foodDao();
        //set FireBase
        mAuth = FirebaseAuth.getInstance();
        //set Adapter
        foodAdapter = new sellerFoodAdapter(this,foodRecords);
        //read food from database
        readFoodItemsFromDatabase();
        food.setAdapter(foodAdapter);
        //Set List View Listener
        setupListViewListener();
        //set Add Food Listener
        addFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SellerFoodList.this,SellerAddFood.class);
                intent.putExtra("Name", "");
                intent.putExtra("Price","");
                intent.putExtra("Desc","");
                intent.putExtra("position", 0);
                startActivityForResult(intent,EDIT_ITEM_REQUEST_CODE_2);
                foodAdapter.notifyDataSetChanged();
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SellerFoodList.this,foodAppSeller.class));
                finish();
            }
        });
    }

    private void setupListViewListener() {
        food.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long rowId) {
                Log.i("MainActivity", "Long Clicked item " + position);
                AlertDialog.Builder builder = new AlertDialog.Builder(SellerFoodList.this);
                builder.setTitle(R.string.dialog_delete_title)
                        .setMessage(R.string.dialog_delete_msg)
                        .setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogInterface, int i) {
                                foodRecords.remove(position); // Remove item from the ArrayList
                                foodAdapter.notifyDataSetChanged(); // Notify listView adapter to update list
                                saveFoodsToDatabase();
                                Toast.makeText(SellerFoodList.this,"Your food has been successfully deleted!",
                                        Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogInterface, int i) {
                                // User cancelled the dialog
                                // Nothing happens
                            }
                        });
                builder.create().show();
                return true;
            }
        });

        food.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                foodFormat updateItem = (foodFormat) foodAdapter.getItem(position);
                String foodName = updateItem.foodName;
                String foodPrice = updateItem.foodPrice;
                String foodDesc = updateItem.foodDescription;
                Log.i("MainActivity", "Clicked item " + position + ": " + updateItem);

                Intent intent = new Intent(SellerFoodList.this, SellerAddFood.class);
                if (intent != null) {
                    // put "extras" into the bundle for access in the edit activity
                    intent.putExtra("Name", foodName);
                    intent.putExtra("Price",foodPrice);
                    intent.putExtra("Desc",foodDesc);
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
            String newFoodName = data.getExtras().getString("Name");
            String newFoodPrice = data.getExtras().getString("Price");
            String newFoodDesc = data.getExtras().getString("Desc");
            String newProvider = mAuth.getCurrentUser().getDisplayName();
            foodFormat newFood = new foodFormat(newFoodName,newFoodPrice,newFoodDesc,newProvider);
            int position = data.getIntExtra("position", -1);
            foodRecords.set(position, newFood);
            Log.i("Updated Item in list:", newFood + ",position:" + position);
            Toast.makeText(this, "updated:" + newFood, Toast.LENGTH_SHORT).show();
            foodAdapter.notifyDataSetChanged();
            saveFoodsToDatabase();
            Toast.makeText(SellerFoodList.this,"Your food has been successfully changed!",
                    Toast.LENGTH_SHORT).show();
        }
        if (requestCode == EDIT_ITEM_REQUEST_CODE_2 && resultCode == RESULT_OK){
            // Extract name value from result extras
            String newFoodName = data.getExtras().getString("Name");
            String newFoodPrice = data.getExtras().getString("Price");
            String newFoodDesc = data.getExtras().getString("Desc");
            String newProvider = mAuth.getCurrentUser().getDisplayName();
            foodFormat newFood = new foodFormat(newFoodName,newFoodPrice,newFoodDesc,newProvider);
            int position = data.getIntExtra("position", -1);
            foodRecords.add(position, newFood);
            //itemsAdapter.add(editedItem);
            Log.i("Updated Item in list:", newFood + ",position:" + position);
            Toast.makeText(this, "updated:" + newFood, Toast.LENGTH_SHORT).show();
            foodAdapter.notifyDataSetChanged();
            saveFoodsToDatabase();
            Toast.makeText(SellerFoodList.this,"Your food has been successfully added!",
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
                    String provider = mAuth.getCurrentUser().getDisplayName();
                    List<FoodItem> recordsFromDB = foodDao.listAll();
                    if (recordsFromDB != null & recordsFromDB.size() > 0) {
                        for (FoodItem item : recordsFromDB) {
                            String recordedProvider = item.getFoodProvider();
                            if (provider.equals(recordedProvider)){
                                String recordedFoodName = item.getFoodName();
                                String recordedFoodPrice = item.getFoodPrice();
                                String recordedFoodDesc = item.getFoodDescription();
                                foodAdapter.add(new foodFormat(recordedFoodName,recordedFoodPrice,recordedFoodDesc));
                            }
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

    private void saveFoodsToDatabase() {
        //Use asynchronous task to run query on the background to avoid locking UI
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                foodDao.deleteAll();
                for (foodFormat todo : foodRecords) {
                    String newFoodName = todo.getFoodName();
                    String newFoodPrice = todo.getFoodPrice();
                    String newFoodDesc = todo.getFoodDescription();
                    String newFoodProvider = mAuth.getCurrentUser().getDisplayName();
                    FoodItem item = new FoodItem(newFoodName,newFoodPrice,newFoodDesc,newFoodProvider);
                    foodDao.insert(item);
                    Log.i("SQLite saved item", "ID: " + item.getFoodID() + " Name: " + item.getFoodName());
                }
                return null;
            }
        }.execute();
    }
}
