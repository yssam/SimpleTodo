package com.example.sam.simpletodo.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.sam.simpletodo.Item;
import com.example.sam.simpletodo.ItemDatabaseHelper;
import com.example.sam.simpletodo.ItemsAdapter;
import com.example.sam.simpletodo.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    ArrayList<Item> items;
    ArrayAdapter<Item> itemsAdapter;
    ListView lvItems;
    private final int REQUEST_CODE1 = 20;
    private final int REQUEST_CODE2 = 21;
    private int cur_pos = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lvItems = (ListView)findViewById(R.id.lvItems);
        //ItemDatabaseHelper databaseHelper = ItemDatabaseHelper.getInstance(this);
        //databaseHelper.onUpgrade(databaseHelper.getWritableDatabase(), 0, 1);
        readItems();
        itemsAdapter = new ItemsAdapter(this, items);
        lvItems.setAdapter(itemsAdapter);
        setupListVewListener();
    }

    public void onAddItem(View v){
        Intent i = new Intent(MainActivity.this, AddItemActivity.class);
        i.putExtra("pos", cur_pos);
        startActivityForResult(i, REQUEST_CODE2);
        return;
    }
    private void setupListVewListener(){

        lvItems.setOnItemLongClickListener(
                new AdapterView.OnItemLongClickListener(){
                    @Override
                    public boolean onItemLongClick(AdapterView<?> adapter, View item, int pos, long id) {
                        items.remove(pos);
                        itemsAdapter.notifyDataSetChanged();
                        writeItems();
                        return true;
                    }
                }
        );

        lvItems.setOnItemClickListener(
                new AdapterView.OnItemClickListener(){
                    @Override
                    public void onItemClick(AdapterView<?> adapter, View item, int pos, long id){
                        //first parameter is the context, second is the class of the activity to launch
                        Intent i = new Intent(MainActivity.this, EditItemActivity.class);
                        i.putExtra("text", items.get(pos).text);
                        i.putExtra("pos", pos);
                        i.putExtra("dueDate", items.get(pos).dueDate);
                        startActivityForResult(i, REQUEST_CODE1);
                        System.out.println("MainActivity " +items.get(pos).text+ " " + pos + " " + items.get(pos).dueDate);
                        return;
                    }
                }
        );
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if(resultCode == RESULT_OK && requestCode == REQUEST_CODE1){
            String editedItem = data.getExtras().getString("text");
            int editedPos = data.getExtras().getInt("pos", 0);
            Date editedDate = (Date)data.getSerializableExtra("dueDate");
            Item i = new Item(editedPos, editedItem, editedDate);
            items.set(editedPos, i);
            itemsAdapter.notifyDataSetChanged();
            ItemDatabaseHelper databaseHelper = ItemDatabaseHelper.getInstance(this);
            databaseHelper.updateItemText(i);
        }

        else if(resultCode == RESULT_OK && requestCode == REQUEST_CODE2){
            String AddedItem = data.getExtras().getString("text");
            int AddedPos = data.getExtras().getInt("pos", 0);
            Date AddedDate = (Date)data.getSerializableExtra("dueDate");
            System.out.println("MainActivity "+AddedItem+ " " + AddedPos + " " + AddedDate);
            Item i = new Item(AddedPos, AddedItem, AddedDate);
            cur_pos = AddedPos+1;
            itemsAdapter.add(i);
            itemsAdapter.notifyDataSetChanged();
            ItemDatabaseHelper databaseHelper = ItemDatabaseHelper.getInstance(this);
            databaseHelper.addItem(i);
        }
    }



    private void readItems(){
        //SQLite read
        ItemDatabaseHelper databaseHelper = ItemDatabaseHelper.getInstance(this);
        List<Item> items2 = databaseHelper.getAllItems();
        items = new ArrayList<Item>();
        for (int i=0; i<items2.size(); ++i) {
            //System.out.println(item2.pos + " " + item2.text);
            Item item2 = items2.get(i);
            items.add(i, item2);
            cur_pos++;
        }
    }

    private void writeItems(){
        //SQListe write
        cur_pos = 0;
        ItemDatabaseHelper databaseHelper = ItemDatabaseHelper.getInstance(this);
        databaseHelper.deleteAllItems();
        Item temp = new Item(0, "");
        for(int i=0; i< items.size(); ++i)
        {
            String text = items.get(i).text;
            System.out.println(text);
            temp.pos = cur_pos++;
            temp.text = text;
            temp.dueDate = items.get(i).dueDate;
            databaseHelper.addItem(temp);
        }
    }
}
