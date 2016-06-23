package com.codepath.simpletodo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by lester on 6/20/16.
 */
public class TodoDatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "simpleTodoDatabase";
    private static final String TABLE_NAME = "todoItem";
    private static final int DATABASE_VERSION = 1;
    private static TodoDatabaseHelper sInstance;

    private TodoDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static synchronized TodoDatabaseHelper getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new TodoDatabaseHelper(context);
        }
        return sInstance;
    }

    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.setForeignKeyConstraintsEnabled(true);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TODO_ITEMS_TABLE = "CREATE TABLE " + TABLE_NAME +
                "( pos INTEGER PRIMARY KEY, text TEXT, date DATE, priority INTEGER )";
        db.execSQL(CREATE_TODO_ITEMS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion != newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
            onCreate(db);
        }
    }

    public void addTodoItem(TodoItem todoItem) {
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            values.put("pos", todoItem.pos);
            values.put("text", todoItem.text);
            //values.put("priority", todoItem.priority);
            // values.put("date"...
            db.insertOrThrow(TABLE_NAME, null, values);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            throw e;
        } finally {
            db.endTransaction();
        }
    }

    public void updateTodoItem(TodoItem todoItem) {
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            values.put("pos", todoItem.pos);
            values.put("text", todoItem.text);
            //values.put("priority", todoItem.priority);
            // values.put("date"....
            db.update(TABLE_NAME, values, "pos=" + Integer.toString(todoItem.pos), null);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            throw e;
        } finally {
            db.endTransaction();
        }
    }

    public void deleteTodoItem(int pos) {
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        try {
            db.delete(TABLE_NAME, "pos=" + Integer.toString(pos), null);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            throw e;
        } finally {
            db.endTransaction();
        }
    }

    public ArrayList<TodoItem> getAllTodos() {
        SQLiteDatabase db = getReadableDatabase();
        ArrayList<TodoItem> todoItems = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
        try {
            if (cursor.moveToFirst()) {
                do {
                    TodoItem todoItem = new TodoItem();
                    todoItem.pos = cursor.getInt(cursor.getColumnIndex("pos"));
                    todoItem.text = cursor.getString(cursor.getColumnIndex("text"));
                    // todoItem.date = cursor.get
                    // todoItem.priority = cursor.getInt(cursor.getColumnIndex("priority"));
                    todoItems.add(todoItem);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            throw e;
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return todoItems;
    }
}
