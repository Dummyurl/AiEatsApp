package comp5216.sydney.edu.au.aieatsapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class customerFoodAdapter extends ArrayAdapter<foodFormat> {
    public customerFoodAdapter(Context context, ArrayList<foodFormat> records) {
        super(context, 0, records);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        foodFormat foodRecord = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.customer_list_layout, parent, false);
        }
        // Lookup view for data population
        TextView foodName = (TextView) convertView.findViewById(R.id.foodName);
        TextView foodPrice = (TextView) convertView.findViewById(R.id.foodPrice);
        TextView foodDescription = (TextView) convertView.findViewById(R.id.foodDescription);
        TextView foodProvider = (TextView) convertView.findViewById(R.id.foodProvider);
        // Populate the data into the template view using the data object
        foodName.setText(foodRecord.getFoodName());
        double foodprice = Double.parseDouble(foodRecord.getFoodPrice());
        DecimalFormat decimalFormat = new DecimalFormat("$0.00");
        foodPrice.setText(decimalFormat.format(foodprice));
        foodDescription.setText("Short Description: " + foodRecord.getFoodDescription());
        foodProvider.setText("Provider: " + foodRecord.getFoodProvider());

        // Return the completed view to render on screen
        return convertView;
    }

}
