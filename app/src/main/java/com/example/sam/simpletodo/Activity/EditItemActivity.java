package com.example.sam.simpletodo.Activity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;

import com.example.sam.simpletodo.R;

import java.util.Calendar;
import java.util.Date;

public class EditItemActivity extends AppCompatActivity {
    int pos = 0;
    EditText etText;
    Date date;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);
        String text = getIntent().getStringExtra("text");
        pos = getIntent().getIntExtra("pos", 0);
        date = (Date)getIntent().getSerializableExtra("dueDate");
        //set text
        etText = (EditText) findViewById(R.id.txEdit);
        etText.setText(text);
        //set date
        DatePicker dueDatePicker = (DatePicker)findViewById(R.id.datePicker2);
        try {
            System.out.println("Activity " +text+ " " + pos + " " + date);
            System.out.println("Activity Date " +date.getYear()+ " " + date.getMonth() + " " + date.getDate());
            //dueDatePicker.init(date.getYear(), date.getMonth(), date.getDate(), null);
            //dueDatePicker.setCalendarViewShown(true);
        }catch(Exception e){
            System.err.println("EditItemActivity onCreate Caught IOException: " + e.getMessage());
        }

    }

    public void onEditItem(View v){
        String AddedItem = etText.getText().toString();
        final Calendar calendar = Calendar.getInstance();
        DatePicker dueDatePicker = (DatePicker)findViewById(R.id.datePicker2);
        calendar.set(dueDatePicker.getYear(),
                dueDatePicker.getMonth(),
                dueDatePicker.getDayOfMonth());
        Intent data = new Intent();
        data.putExtra("text", etText.getText().toString());
        data.putExtra("pos", pos);
        data.putExtra("dueDate", calendar.getTime());
        setResult(RESULT_OK, data);
        finish();
    }

    public void onCancel(View v){
        setResult(RESULT_CANCELED);
        finish();
    }
}
