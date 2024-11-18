package com.example.ad2_lab1.dao;

import static com.example.ad2_lab1.database.DbHelper.COLUMN_NAME_CONTENT;
import static com.example.ad2_lab1.database.DbHelper.COLUMN_NAME_DATE;
import static com.example.ad2_lab1.database.DbHelper.COLUMN_NAME_ID;
import static com.example.ad2_lab1.database.DbHelper.COLUMN_NAME_STATUS;
import static com.example.ad2_lab1.database.DbHelper.COLUMN_NAME_TITLE;
import static com.example.ad2_lab1.database.DbHelper.COLUMN_NAME_TYPE;
import static com.example.ad2_lab1.database.DbHelper.TABLE_TODO_NAME;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.ad2_lab1.database.DbHelper;
import com.example.ad2_lab1.model.TodoModel;

import java.util.ArrayList;


public class ToDoDAO {
    private SQLiteDatabase db;
    private DbHelper dbHelper;

    public ToDoDAO(Context context) {
        dbHelper = new DbHelper(context);
        db = dbHelper.getWritableDatabase();
    }

    @SuppressLint("Range")
    public ArrayList<TodoModel> getListToDo() {
        ArrayList<TodoModel> lists = new ArrayList<>();
        db = dbHelper.getReadableDatabase();

        Cursor cursor = null;
        try {
            cursor = db.rawQuery("SELECT * FROM " + TABLE_TODO_NAME, null);
            if (cursor.moveToFirst()) {
                do {
                    TodoModel todoModel = new TodoModel();
                    todoModel.setId(cursor.getInt(cursor.getColumnIndex(COLUMN_NAME_ID)));
                    todoModel.setTitle(cursor.getString(cursor.getColumnIndex(COLUMN_NAME_TITLE)));
                    todoModel.setContent(cursor.getString(cursor.getColumnIndex(COLUMN_NAME_CONTENT)));
                    todoModel.setDate(cursor.getString(cursor.getColumnIndex(COLUMN_NAME_DATE)));
                    todoModel.setType(cursor.getString(cursor.getColumnIndex(COLUMN_NAME_TYPE)));
                    todoModel.setStatus(cursor.getInt(cursor.getColumnIndex(COLUMN_NAME_STATUS)));
                    lists.add(todoModel);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return lists;
    }



    public boolean addToDo(TodoModel todoModel) {
        SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();
        sqLiteDatabase.beginTransaction();

        try {
            ContentValues contentValues = new ContentValues();
            contentValues.put(COLUMN_NAME_TITLE, todoModel.getTitle());
            contentValues.put(COLUMN_NAME_CONTENT, todoModel.getContent());
            contentValues.put(COLUMN_NAME_DATE, todoModel.getDate());
            contentValues.put(COLUMN_NAME_TYPE, todoModel.getType());
            contentValues.put(COLUMN_NAME_STATUS, todoModel.getStatus());

            long check = sqLiteDatabase.insert(TABLE_TODO_NAME, null, contentValues);
            if (check != -1) {
                sqLiteDatabase.setTransactionSuccessful();
            }
            return check != -1;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            sqLiteDatabase.endTransaction();
        }
    }

    public boolean updateToDo(TodoModel todoModel) {
        SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();
        sqLiteDatabase.beginTransaction();
        try {
            ContentValues contentValues = new ContentValues();
            contentValues.put(COLUMN_NAME_TITLE, todoModel.getTitle());
            contentValues.put(COLUMN_NAME_CONTENT, todoModel.getContent());
            contentValues.put(COLUMN_NAME_DATE, todoModel.getDate());
            contentValues.put(COLUMN_NAME_TYPE, todoModel.getType());
            contentValues.put(COLUMN_NAME_STATUS, todoModel.getStatus());

            int rows = sqLiteDatabase.update(TABLE_TODO_NAME, contentValues, COLUMN_NAME_ID + " = ?", new String[]{String.valueOf(todoModel.getId())});
            if (rows > 0) {
                sqLiteDatabase.setTransactionSuccessful();
            }
            return rows > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            sqLiteDatabase.endTransaction();
        }
    }

    public boolean deleteToDo(int id) {
        SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();
        sqLiteDatabase.beginTransaction();
        try {
            int rows = sqLiteDatabase.delete(TABLE_TODO_NAME, COLUMN_NAME_ID + " = ?", new String[]{String.valueOf(id)});
            if (rows > 0) {
                sqLiteDatabase.setTransactionSuccessful();
            }
            return rows > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            sqLiteDatabase.endTransaction();
        }
    }

}
