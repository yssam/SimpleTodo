package com.example.sam.simpletodo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class EditItemActivity extends AppCompatActivity {
    int pos = 0;
    EditText etText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);
        String item = getIntent().getStringExtra("item");
        pos = getIntent().getIntExtra("pos", 0);
        etText = (EditText) findViewById(R.id.txEdit);
        etText.setText(item);
    }

    public void onEditItem(View v){
        Intent data = new Intent();
        data.putExtra("item", etText.getText().toString());
        data.putExtra("pos", pos);
        setResult(RESULT_OK, data);
        finish();
    }
}
