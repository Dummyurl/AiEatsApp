package comp5216.sydney.edu.au.aieatsapp;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class sellerOrderAdapter extends ArrayAdapter<OrderFormat> {
    public sellerOrderAdapter(Context context, ArrayList<OrderFormat> orderRecords) {
        super(context, 0, orderRecords);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        OrderFormat orderRecords = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.order_seller_list_layout, parent, false);
        }
        // Lookup view for data population
        TextView foodOrderID = (TextView) convertView.findViewById(R.id.foodOrderID);
        TextView foodOrderStatus = (TextView) convertView.findViewById(R.id.foodOrderStatus);
        TextView foodOrderName = (TextView) convertView.findViewById(R.id.foodOrderName);
        // Populate the data into the template view using the data object
        foodOrderID.setText("Order ID: " + orderRecords.getOrderID());
        foodOrderStatus.setText(orderRecords.getOrderStatus());
        foodOrderStatus.setTextColor(Color.BLUE);
        foodOrderName.setText("Order Food Name: " + orderRecords.getOrderFoodName());

        // Return the completed view to render on screen
        return convertView;
    }

}
