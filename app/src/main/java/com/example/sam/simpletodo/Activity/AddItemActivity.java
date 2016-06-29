package com.example.sam.simpletodo.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;

import com.afollestad.materialdialogs.MaterialDialog;
import com.example.sam.simpletodo.R;

import java.util.Calendar;

public class AddItemActivity extends AppCompatActivity {
    int pos;
    EditText mtItemEdit;
    int priority = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);
        pos = getIntent().getIntExtra("pos", 0);
        mtItemEdit = (EditText)findViewById(R.id.mtNewItem);


    }

    public void onAddNewItem(View v) {
        String editedItem = mtItemEdit.getText().toString();

        final Calendar calendar = Calendar.getInstance();
        DatePicker dueDatePicker = (DatePicker)findViewById(R.id.datePicker);
        calendar.set(dueDatePicker.getYear(),
                dueDatePicker.getMonth(),
                dueDatePicker.getDayOfMonth());

        Intent data = new Intent();
        data.putExtra("text", editedItem);
        data.putExtra("pos", pos);
        data.putExtra("dueDate", calendar.getTime());
        data.putExtra("priority", priority);
        System.out.println("priority="+priority);
        //System.out.println("AddItemActivity "+editedItem+ " " + pos + " " + calendar.getTime());
        setResult(RESULT_OK, data);
        finish();
    }
    public void onPriorityClicked(View v){
        // Is the button now checked?
        boolean checked = ((RadioButton) v).isChecked();
        System.out.println("checked="+checked);
        switch(v.getId()){
            case R.id.rbLow:
                priority = 0;
                break;
            case R.id.rbMid:
                priority = 1;
                break;
            case R.id.rbHigh:
                priority = 2;
                break;
            default:
                priority = 0;
        }
    }

    public void onCancel(View v){
        setResult(RESULT_CANCELED);
        finish();
    }
}
