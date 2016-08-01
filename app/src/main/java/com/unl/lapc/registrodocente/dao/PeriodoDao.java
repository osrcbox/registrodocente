package com.unl.lapc.registrodocente.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.unl.lapc.registrodocente.modelo.Periodo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Usuario on 15/07/2016.
 */
public class PeriodoDao extends DBHandler {

    public PeriodoDao(Context context){
        super(context);
    }

    public void add(Periodo periodo) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("nombre", periodo.getNombre());
        values.put("inicio", toShortDate(periodo.getInicio()));
        values.put("fin", toShortDate(periodo.getFin()));
        values.put("escala", periodo.getEscala());

        db.insert("periodo", null, values);
        db.close();
    }

    public int update(Periodo periodo) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("nombre", periodo.getNombre());
        values.put("inicio", toShortDate(periodo.getInicio()));
        values.put("fin", toShortDate(periodo.getFin()));
        values.put("escala", periodo.getEscala());

        return db.update("periodo", values, "id = ?", new String[]{String.valueOf(periodo.getId())});
    }

    public Periodo get(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query("periodo", new String[] { "id", "nombre", "inicio", "fin", "escala"}, "id=?", new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        Periodo per = new Periodo(cursor.getInt(0), cursor.getString(1), toDate(cursor.getString(2)), toDate(cursor.getString(3)), cursor.getDouble(4));

        return per;
    }

    public void delete(Periodo periodo) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("periodo", "id = ?", new String[] { String.valueOf(periodo.getId()) });
        db.close();
    }

    public List<Periodo> getAll() {
        List<Periodo> lista = new ArrayList<>();

        String selectQuery = "SELECT id, nombre, inicio, fin, escala FROM periodo";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Periodo per = new Periodo(cursor.getInt(0), cursor.getString(1), toDate(cursor.getString(2)), toDate(cursor.getString(3)), cursor.getDouble(4));
                lista.add(per);
            } while (cursor.moveToNext());
        }

        return lista;
    }

    public boolean existe(Periodo per){
        String selectQuery = "SELECT count(*) FROM periodo where lower(trim(nombre)) =? and id <> ?";
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(selectQuery, new  String[]{per.getNombre().toLowerCase(), ""+per.getId()});
        cursor.moveToFirst();

        return cursor.getInt(0) > 0;
    }

}
