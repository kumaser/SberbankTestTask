package com.sberbank.model.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "sberbank.db";

    private final Object mLock = new Object();

    private List<Class> mTables;

    public DBHelper(Context context, List<Class> tables) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        mTables = tables;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        for (Class clazz : mTables) {
            String sqlCreateTable = Table.create(clazz);
            db.execSQL(sqlCreateTable);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        for (Class clazz : mTables) {
            String sqlDropTable = Table.drop(clazz);
            db.execSQL(sqlDropTable);
        }
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    public <T> List<T> getAll(Class<T> clazz) {
        synchronized (mLock) {
            SQLiteDatabase db = getWritableDatabase();
            DBTable table = clazz.getAnnotation(DBTable.class);
            List<T> items = new ArrayList<>();
            if (doesTableExist(db, table.name())) {
                Cursor cursor = db.query(table.name(), null, null, null, null, null, null);
                try {
                    if (cursor.moveToFirst()) {
                        T firstItem = Table.getFromCursor(clazz, cursor);
                        items.add(firstItem);
                        while (cursor.moveToNext()) {
                            T item = Table.getFromCursor(clazz, cursor);
                            items.add(item);
                        }
                    }
                    return items;
                } finally {
                    cursor.close();
                    close();
                }
            }
            return items;
        }
    }

    public <T> void updateTable(List<T> items, Class clazz) {
        synchronized (mLock) {
            SQLiteDatabase database = getWritableDatabase();
            DBTable table = (DBTable) clazz.getAnnotation(DBTable.class);
            String tableName = table.name();
            if (doesTableExist(database, tableName)) {
                try {
                    for (T item : items) {
                        ContentValues contentValues = Table.insert(clazz, item);
                        database.insert(tableName, null, contentValues);
                    }
                } finally {
                    close();
                }
            }
        }
    }

    public void clearTable(Class clazz) {
        synchronized (mLock) {
            try {
                DBTable table = (DBTable) clazz.getAnnotation(DBTable.class);
                SQLiteDatabase database = getWritableDatabase();
                if (doesTableExist(database, table.name())) {
                    database.delete(table.name(), null, null);
                }
            } finally {
                close();
            }
        }
    }

    private boolean doesTableExist(SQLiteDatabase database, String tableName) {
        Cursor cursor = null;
        try {
            cursor = database.rawQuery("select DISTINCT tbl_name from sqlite_master where tbl_name = '" + tableName + "'", null);
            return cursor != null && cursor.getCount() > 0;
        } finally {
            assert cursor != null;
            cursor.close();
        }
    }
}
