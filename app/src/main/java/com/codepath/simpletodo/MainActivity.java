package com.codepath.simpletodo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ArrayList<TodoItem> items;
    TodoItemsAdapter itemsAdapter;
    ListView lvItems;
    private final int EDIT_REQUEST_CODE = 13;
    TodoDatabaseHelper todoDatabaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        items = new ArrayList<>();
        todoDatabaseHelper = TodoDatabaseHelper.getInstance(this);
        items = todoDatabaseHelper.getAllTodos();
        itemsAdapter = new TodoItemsAdapter(this, items);
        lvItems = (ListView)findViewById(R.id.lvItems);
        lvItems.setAdapter(itemsAdapter);
        setupListViewListener();
    }

    public void onAddItem(View v) {
        EditText etNewItem = (EditText) findViewById(R.id.etNewItem);
        TodoItem newItem = new TodoItem();
        newItem.text = etNewItem.getText().toString();
        newItem.pos = items.size();
        items.add(newItem);
        etNewItem.setText("");
        todoDatabaseHelper.addTodoItem(newItem);
    }

    public void setupListViewListener(){
        lvItems.setOnItemLongClickListener(
                new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> adapter, View item, int pos, long id) {
                        items.remove(pos);
                        itemsAdapter.notifyDataSetChanged();
                        todoDatabaseHelper.deleteTodoItem(pos);
                        return true;
                    }
                }
        );
        lvItems.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapter, View item, int pos, long id) {
                        Intent editItem = new Intent(MainActivity.this, EditItemActivity.class);
                        String text = items.get(pos).text;
                        editItem.putExtra("pos", pos);
                        editItem.putExtra("text", text);
                        startActivityForResult(editItem, EDIT_REQUEST_CODE);
                    }
                }
        );
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == EDIT_REQUEST_CODE && resultCode == RESULT_OK) {
            String text = data.getStringExtra("text");
            int pos = data.getIntExtra("pos", -1);
            TodoItem updatedItem = items.get(pos);
            updatedItem.text = text;
            items.set(pos, updatedItem);
            itemsAdapter.notifyDataSetChanged();
            todoDatabaseHelper.updateTodoItem(updatedItem);
        }
    }
}
