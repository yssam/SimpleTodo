package com.example.sam.simpletodo.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.sam.simpletodo.Model.Item;
import com.example.sam.simpletodo.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Created by Sam on 6/24/16.
 */
public class ItemsAdapter extends ArrayAdapter<Item>{
    public ItemsAdapter(Context context, ArrayList<Item> users) {
        super(context, 0, users);
    }
    public static final SimpleDateFormat SDF = new SimpleDateFormat("MM/dd/yyyy");
    String textPriority;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Item item = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_todo, parent, false);
        }
        // Lookup view for data population
        TextView tvItem = (TextView) convertView.findViewById(R.id.tvItem);
        TextView tvDueDate = (TextView) convertView.findViewById(R.id.tvDueDate);
        TextView tvPriority = (TextView) convertView.findViewById(R.id.tvPriority);
        System.out.println("ItemsAdapter priority = "+item.priority);

        switch(item.priority){
            case 0:
                textPriority = "Low Priority";
                break;
            case 1:
                textPriority = "Mid Priority";
                break;
            case 2:
                textPriority = "High Priority";
                break;
            default:
                textPriority = "Empty";
        }
        // Populate the data into the template view using the data object
        try {
            tvItem.setText(item.text);
        }catch(Exception e){
            System.err.println("Caught IOException: item.text" + e.getMessage());
        }
        try {
            tvDueDate.setText(SDF.format(item.dueDate));
        }catch(Exception e){
            System.err.println("Caught IOException: item.dueDate" + e.getMessage());
        }
        try{
            tvPriority.setText(textPriority);
        }
        catch(Exception e){
            System.err.println("Caught IOException: item.priority" + e.getMessage());
        }
        // Return the completed view to render on screen
        return convertView;
    }

}
