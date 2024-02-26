package com.example.appispc;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class UsuarioDAO {

    private SQLiteDatabase database;
    private final DatabaseHelper dbHelper;

    public UsuarioDAO(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    public void abrir() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void cerrar() {
        dbHelper.close();
    }

    public long obtenerIdPorUsername(String username) {
        Cursor cursor = database.query(DatabaseHelper.TABLE_USERS, new String[]{DatabaseHelper.COLUMN_ID},
                DatabaseHelper.COLUMN_USERNAME + " = ?", new String[]{username}, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            @SuppressLint("Range") long id = cursor.getLong(cursor.getColumnIndex(DatabaseHelper.COLUMN_ID));
            cursor.close();
            return id;
        }
        return -1; // Retornar -1 si no se encuentra el usuario
    }


    public long agregarUsuario(String username, String password) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_USERNAME, username);
        values.put(DatabaseHelper.COLUMN_PASSWORD, password);

        return database.insert(DatabaseHelper.TABLE_USERS, null, values);
    }

    public boolean usuarioExistente(String username) {
        Cursor cursor = database.query(DatabaseHelper.TABLE_USERS, null,
                DatabaseHelper.COLUMN_USERNAME + " = ?", new String[]{username}, null, null, null);

        int count = cursor.getCount();
        cursor.close();

        return count > 0;
    }

    public boolean validarCredenciales(String username, String password) {
        Cursor cursor = database.query(DatabaseHelper.TABLE_USERS, null,
                DatabaseHelper.COLUMN_USERNAME + " = ? AND " + DatabaseHelper.COLUMN_PASSWORD + " = ?",
                new String[]{username, password}, null, null, null);

        int count = cursor.getCount();
        cursor.close();

        return count > 0;
    }
}