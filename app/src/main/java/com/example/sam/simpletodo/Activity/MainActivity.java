package com.example.sam.simpletodo.Activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

import com.afollestad.materialdialogs.MaterialDialog;
import com.codetroopers.betterpickers.calendardatepicker.CalendarDatePickerDialogFragment;

import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.afollestad.materialdialogs.simplelist.MaterialSimpleListAdapter;
import com.afollestad.materialdialogs.simplelist.MaterialSimpleListItem;
import com.example.sam.simpletodo.Model.Item;
import com.example.sam.simpletodo.Database.ItemDatabaseHelper;
import com.example.sam.simpletodo.Adapter.ItemsAdapter;
import com.example.sam.simpletodo.R;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Date;

public class MainActivity extends AppCompatActivity{
    ArrayList<Item> items;
    ArrayAdapter<Item> itemsAdapter;
    ListView lvItems;
    SimpleDateFormat SDF;
    int mode = 2;
    private final int REQUEST_CODE_ADD_ITEM = 20;
    private final int REQUEST_CODE_EDIT_ITEM = 21;
    private int cur_pos = 0;
    
    private static final String TAG_CODE = "2431"; // DialogFragment Tag

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lvItems = (ListView)findViewById(R.id.lvItems);
        //ItemDatabaseHelper databaseHelper = ItemDatabaseHelper.getInstance(this);
        //databaseHelper.onUpgrade(databaseHelper.getWritableDatabase(), 0, 1);
        readItems();
        System.out.println("abcdefg");
        SDF = new SimpleDateFormat("MM/dd/yyyy");
        itemsAdapter = new ItemsAdapter(this, items);
        lvItems.setAdapter(itemsAdapter);
        setupListVewListener();
    }

    public void onAddItem(View v){
            Intent i = new Intent(MainActivity.this, AddItemActivity.class);
            i.putExtra("pos", cur_pos);
            startActivityForResult(i, REQUEST_CODE_ADD_ITEM);
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
                       if(mode == 1) {
                           Intent i = new Intent(MainActivity.this, EditItemActivity.class);
                           i.putExtra("text", items.get(pos).text);
                           i.putExtra("pos", pos);
                           i.putExtra("dueDate", items.get(pos).dueDate);
                           startActivityForResult(i, REQUEST_CODE_EDIT_ITEM);
                           System.out.println("MainActivity " + items.get(pos).text + " " + pos + " " + items.get(pos).dueDate);
                           return;
                       }
                       else if(mode == 2)
                       {
                           showMaterialDialog(pos);
                       }

                    }
                }
        );
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if(resultCode == RESULT_OK && requestCode == REQUEST_CODE_EDIT_ITEM){
            String editedItem = data.getExtras().getString("text");
            int editedPos = data.getExtras().getInt("pos");
            Date editedDate = (Date)data.getSerializableExtra("duDate");
            int priority = data.getExtras().getInt("priority");
            Item i = new Item(editedPos, editedItem, editedDate, priority);
            items.set(editedPos, i);
            itemsAdapter.notifyDataSetChanged();
            ItemDatabaseHelper databaseHelper = ItemDatabaseHelper.getInstance(this);
            databaseHelper.updateItem(i);
        }

        else if(resultCode == RESULT_OK && requestCode == REQUEST_CODE_ADD_ITEM){
            String AddedItem = data.getExtras().getString("text");
            int AddedPos = data.getExtras().getInt("pos", 0);
            Date AddedDate = (Date)data.getSerializableExtra("dueDate");

            int priority = data.getExtras().getInt("priority");
            System.out.println("MainActivity get priority = " + priority);
            Item i = new Item(AddedPos, AddedItem, AddedDate, priority);
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

            Item item2 = items2.get(i);
            System.out.println("readItems: pos=" + item2.pos + " text=" + item2.text + " priority="+item2.priority);
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


    public void showMaterialDialog(int position){
        // TODO: find a better way instead of final int
        final int tag_position = position;
        final Item item3 = items.get(position);
        final MaterialSimpleListAdapter adapter = new MaterialSimpleListAdapter(this);
        final ItemDatabaseHelper databaseHelper = ItemDatabaseHelper.getInstance(this);
        String str_priority = "";
        switch(item3.priority){
            case 0:
                str_priority = "Low Priority";
                break;
            case 1:
                str_priority = "Mid Priority";
                break;
            case 2:
                str_priority = "High Priority";
                break;
        }

        adapter.add(new MaterialSimpleListItem.Builder(this)
                .content(item3.text)
                //.icon(R.drawable.ic_bookmark_border_black_48dp)
                //.backgroundColor(ContextCompat.getColor(MainActivity.this, R.color.colorPrimaryLight))
                .build());
        adapter.add(new MaterialSimpleListItem.Builder(this)
                .content(SDF.format(item3.dueDate))
                //.icon(R.drawable.ic_date_range_black_48dp)
                //.backgroundColor(ContextCompat.getColor(MainActivity.this, R.color.colorDateLight))
                .build());
        adapter.add(new MaterialSimpleListItem.Builder(this)
                .content(str_priority)
                //.icon(R.drawable.ic_priority_high_black_48dp)
                //.backgroundColor(ContextCompat.getColor(MainActivity.this, R.color.colorAccentLight))
                .build());

        new MaterialDialog.Builder(this)
                .title(getString(R.string.edit_item_title))
                .adapter(adapter, new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog dialog, View itemView, int which, CharSequence text) {
                        if (which == 0) {
                            String itemText = items.get(tag_position).text.toString();
                            MaterialDialog theDialog = new MaterialDialog.Builder(MainActivity.this)
                                    .title(R.string.edit_item_title)
                                    .inputType(InputType.TYPE_CLASS_TEXT)
                                    .input("", itemText, false, new MaterialDialog.InputCallback() {
                                        @Override
                                        public void onInput(@NonNull MaterialDialog dialog,
                                                            CharSequence input) {

                                            items.get(tag_position).text =
                                                    input.toString(); // need to notify
                                            itemsAdapter.notifyDataSetChanged();
                                            databaseHelper.updateItem(item3);
                                        }
                                    }).build();

                            theDialog.getInputEditText().setSingleLine(false);
                            theDialog.show();
                        } else if (which == 1) {
                            Calendar cal = Calendar.getInstance();
                            cal.setTime(item3.dueDate);
                            CalendarDatePickerDialogFragment cdp =
                                    new CalendarDatePickerDialogFragment()
                                            .setThemeCustom(R.style.DateTheme)
                                            //.setOnDateSetListener(MainActivity.this)
                                            .setPreselectedDate(cal.get(Calendar.YEAR),
                                                    cal.get(Calendar.MONTH),
                                                    cal.get(Calendar.DATE));
                            //Bundle args = new Bundle();
                            //args.putInt("position", tag_position);
                            //cdp.setArguments(args);
                            System.out.println("dueDate: " + item3.dueDate + " " + cal.getTime()+ " ");
                            item3.dueDate = cal.getTime();
                            itemsAdapter.notifyDataSetChanged();
                            databaseHelper.updateItem(item3);
                            cdp.show(getSupportFragmentManager(), TAG_CODE);
                        } else {
                            new MaterialDialog.Builder(MainActivity.this)
                                    .title(R.string.edit_item_priority)
                                    .items(R.array.priority)
                                    .itemsCallbackSingleChoice(-1, new MaterialDialog.ListCallbackSingleChoice() {
                                        @Override
                                        public boolean onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {

                                            if(which == 0)
                                                item3.priority = 0;
                                            else if(which == 1)
                                                item3.priority = 1;
                                            else if(which == 2)
                                                item3.priority = 2;
                                            itemsAdapter.notifyDataSetChanged();
                                            databaseHelper.updateItem(item3);
                                            return true;
                                        }
                                    })
                                    .positiveText(R.string.choose)
                                    .negativeText(R.string.cancel)
                                    .show();
                        }dialog.dismiss();
                    }
                })
                .negativeText(R.string.cancel)
                .show();








    }

}
