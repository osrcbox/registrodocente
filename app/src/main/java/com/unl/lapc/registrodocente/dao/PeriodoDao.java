package com.unl.lapc.registrodocente.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.unl.lapc.registrodocente.modelo.PeriodoAcademico;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Usuario on 15/07/2016.
 */
public class PeriodoDao extends DBHandler {

    public static final String TABLE_NAME = "periodoacademico";
    public static final String ID = "id";
    public static final String NOMBRE = "nombre";

    public PeriodoDao(Context context){
        super(context);
    }

    public void add(PeriodoAcademico periodo) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(NOMBRE, periodo.getNombre());

        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    public int update(PeriodoAcademico periodo) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(NOMBRE, periodo.getNombre());

        return db.update(TABLE_NAME, values, ID + " = ?", new String[]{String.valueOf(periodo.getId())});
    }

    public PeriodoAcademico get(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, new String[] { ID, NOMBRE}, ID + "=?", new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        PeriodoAcademico contact = new PeriodoAcademico(Integer.parseInt(cursor.getString(0)), cursor.getString(1));

        return contact;
    }

    public void delete(PeriodoAcademico periodo) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, ID + " = ?", new String[] { String.valueOf(periodo.getId()) });
        db.close();
    }

    public List<PeriodoAcademico> getAll() {
        List<PeriodoAcademico> shopList = new ArrayList<>();

        String selectQuery = "SELECT * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                PeriodoAcademico shop = new PeriodoAcademico();
                shop.setId(Integer.parseInt(cursor.getString(0)));
                shop.setNombre(cursor.getString(1));
                shopList.add(shop);
            } while (cursor.moveToNext());
        }

        return shopList;
    }

    public boolean existe(PeriodoAcademico per){
        String selectQuery = "SELECT count(*) FROM " + TABLE_NAME + " where lower(trim(" + NOMBRE + ")) =? and " + ID +" <> ?";
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(selectQuery, new  String[]{per.getNombre().toLowerCase(), ""+per.getId()});
        cursor.moveToFirst();

        return cursor.getInt(0) > 0;
    }

}
