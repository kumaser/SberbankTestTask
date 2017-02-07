package com.sberbank.model.database;

import android.content.ContentValues;
import android.database.Cursor;

import java.lang.reflect.Field;

class Table {

    private static final String COMMA_SEP = ",";
    private static final String SPACE = " ";

    static String create(Class clazz) {
        if (!clazz.isAnnotationPresent(DBTable.class)) {
            throw new RuntimeException("Annotation DBTable not found");
        }
        DBTable table = (DBTable) clazz.getAnnotation(DBTable.class);
        String tableName = table.name();
        String primaryKey = getPrimaryKey(clazz);
        String fields = appendAllFields(clazz);
        return "CREATE TABLE " + tableName + " (" +
                primaryKey + " INTEGER PRIMARY KEY" +
                fields +
                ")";
    }

    private static String getPrimaryKey(Class clazz) {
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(PrimaryKey.class) &&
                    field.isAnnotationPresent(DBColumn.class)) {
                return field.getAnnotation(DBColumn.class).name();
            }
        }
        throw new RuntimeException("Primary key not found");
    }

    private static String appendAllFields(Class clazz) {
        StringBuilder result = new StringBuilder();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            if (!field.isAnnotationPresent(PrimaryKey.class) &&
                    field.isAnnotationPresent(DBColumn.class)) {
                DBColumn annotation = field.getAnnotation(DBColumn.class);
                result.append(COMMA_SEP)
                        .append(annotation.name())
                        .append(SPACE)
                        .append(annotation.type().getValue());
            }
        }
        return result.toString();
    }

    static String drop(Class clazz) {
        DBTable table = (DBTable) clazz.getAnnotation(DBTable.class);
        String tableName = table.name();
        return "DROP TABLE IF EXISTS " + tableName;
    }

    static <T> T getFromCursor(Class<T> clazz, Cursor cursor) {
        try {
            T item = clazz.newInstance();
            Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields) {
                if (field.isAnnotationPresent(DBColumn.class)) {
                    field.setAccessible(true);
                    DBColumn annotation = field.getAnnotation(DBColumn.class);
                    int columnIndex = cursor.getColumnIndex(annotation.name());
                    int type = cursor.getType(columnIndex);
                    if (type == Cursor.FIELD_TYPE_INTEGER) {
                        field.set(item, cursor.getInt(columnIndex));
                    } else if (type == Cursor.FIELD_TYPE_STRING) {
                        field.set(item, cursor.getString(columnIndex));
                    } else if (type == Cursor.FIELD_TYPE_FLOAT) {
                        field.set(item, cursor.getFloat(columnIndex));
                    }
                }
            }
            return item;
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    static <T> ContentValues insert(Class clazz, T object) {
        try {
            ContentValues values = new ContentValues();
            Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields) {
                if (field.isAnnotationPresent(DBColumn.class)) {
                    field.setAccessible(true);
                    String fieldName = field.getAnnotation(DBColumn.class).name();
                    Class<?> fieldType = field.getType();
                    if (fieldType == Integer.class) {
                        values.put(fieldName, (Integer) field.get(object));
                    } else if (fieldType == String.class) {
                        values.put(fieldName, (String) field.get(object));
                    } else if (fieldType == Double.class) {
                        values.put(fieldName, (Double) field.get(object));
                    } else if (fieldType == Boolean.class) {
                        values.put(fieldName, (Boolean) field.get(object));
                    } else if (fieldType == Long.class) {
                        values.put(fieldName, (Long) field.get(object));
                    } else if (fieldType == Byte.class) {
                        values.put(fieldName, (Byte) field.get(object));
                    } else if (fieldType == Short.class) {
                        values.put(fieldName, (Short) field.get(object));
                    }
                }
            }
            return values;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return new ContentValues();
    }
}