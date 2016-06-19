package com.codepath.simpletodo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Text;

public class EditItemActivity extends AppCompatActivity {
    int pos;
    String text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);
        EditText editText = (EditText) findViewById(R.id.editText);
        pos = getIntent().getIntExtra("pos", -1);
        text = getIntent().getStringExtra("text");
        editText.setText(text);
        editText.setSelection(text.length()); // Set cursor to end of text
    }

    public void onSave(View v) {
        Intent item = new Intent();
        EditText editText = (EditText) findViewById(R.id.editText);
        item.putExtra("text", editText.getText().toString());
        item.putExtra("pos", pos);
        setResult(RESULT_OK, item);
        this.finish();
    }
}
