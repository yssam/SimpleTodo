package com.example.sam.simpletodo;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    ArrayList<String> items;
    ArrayAdapter<String> itemsAdapter;
    ListView lvItems;
    private final int REQUEST_CODE = 20;
    private int cur_pos = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lvItems = (ListView)findViewById(R.id.lvItems);
        readItems();
        itemsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, items);
        lvItems.setAdapter(itemsAdapter);
        setupListVewListener();
    }

    public void onAddItem(View v){
        EditText etNewItem = (EditText) findViewById(R.id.etNewItem);
        String itemText = etNewItem.getText().toString();
        itemsAdapter.add(itemText);
        etNewItem.setText("");
        //writeItems();
        Item i = new Item(cur_pos++, itemText);
        ItemDatabaseHelper databaseHelper = ItemDatabaseHelper.getInstance(this);
        databaseHelper.addItem(i);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if(resultCode == RESULT_OK && requestCode == REQUEST_CODE){
            String editedItem = data.getExtras().getString("item");
            int editedPos = data.getExtras().getInt("pos", 0);
            items.set(editedPos, editedItem);
            itemsAdapter.notifyDataSetChanged();
            //writeItems();
            Item i = new Item(editedPos, editedItem);
            ItemDatabaseHelper databaseHelper = ItemDatabaseHelper.getInstance(this);
            databaseHelper.updateItemText(i);
        }
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
                        i.putExtra("item", items.get(pos));
                        i.putExtra("pos", pos);
                        startActivityForResult(i, REQUEST_CODE);
                        return;
                    }
                }
        );
    }

    private void readItems(){
        //File read

        /* File fileDir = getFilesDir();
        File todoFile = new File(fileDir, "todo.txt");
        try{
            items = new ArrayList<String>(FileUtils.readLines(todoFile));
        }catch(IOException e) {
            items = new ArrayList<String>();
        }
        */

        //SQLite read
        ItemDatabaseHelper databaseHelper = ItemDatabaseHelper.getInstance(this);
        List<Item> items2 = databaseHelper.getAllItems();
        items = new ArrayList<String>();
        for (int i=0; i<items2.size(); ++i) {
            //System.out.println(item2.pos + " " + item2.text);
            Item item2 = items2.get(i);
            items.add(i, item2.text);
            cur_pos++;
        }
    }

    private void writeItems(){
        //File write

       /* File fileDir = getFilesDir();
        File todoFile = new File(fileDir, "todo.txt");
        try{
            FileUtils.writeLines(todoFile, items);
        }catch(IOException e){
            e.printStackTrace();
        }*/

        //SQListe write
        cur_pos = 0;
        ItemDatabaseHelper databaseHelper = ItemDatabaseHelper.getInstance(this);
        databaseHelper.deleteAllItems();
        Item temp = new Item(0, "");
        for(int i=0; i< items.size(); ++i)
        {
            String text = items.get(i);
            System.out.println(text);
            temp.pos = cur_pos++;
            temp.text = text;
            databaseHelper.addItem(temp);
        }
    }
}