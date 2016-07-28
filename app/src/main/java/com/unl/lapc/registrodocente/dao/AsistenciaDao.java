package com.unl.lapc.registrodocente.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.unl.lapc.registrodocente.modelo.Asistencia;
import com.unl.lapc.registrodocente.modelo.Clase;
import com.unl.lapc.registrodocente.modelo.ClaseEstudiante;
import com.unl.lapc.registrodocente.modelo.Estudiante;
import com.unl.lapc.registrodocente.modelo.PeriodoAcademico;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Usuario on 15/07/2016.
 */
public class AsistenciaDao extends DBHandler {

    public static final String TABLE_NAME = "asistencia";
    SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");

    public AsistenciaDao(Context context){
        super(context);
    }

    public void add(Asistencia asistencia) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("fecha", sd.format(asistencia.getFecha()));
        values.put("estado", asistencia.getEstado());
        values.put("claseestudiante_id", asistencia.getClaseEstudiante().getId());
        values.put("clase_id", asistencia.getClase().getId());
        values.put("estudiante_id", asistencia.getEstudiante().getId());

        long id = db.insert(TABLE_NAME, null, values);
        asistencia.setId((int)id);

        db.close();
    }

    public int update(Asistencia asistencia) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("estado", asistencia.getEstado());

        return db.update(TABLE_NAME, values, "id = ?", new String[]{String.valueOf(asistencia.getId())});
    }

    public Asistencia get(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, new String[] { "id"}, "id=?", new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        Asistencia contact = new Asistencia(Integer.parseInt(cursor.getString(0)));

        return contact;
    }


    /*public List<PeriodoAcademico> getAll() {
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
    }*/

    /*public boolean existe(PeriodoAcademico per){
        String selectQuery = "SELECT count(*) FROM " + TABLE_NAME + " where lower(trim(" + NOMBRE + ")) =? and " + ID +" <> ?";
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(selectQuery, new  String[]{per.getNombre().toLowerCase(), ""+per.getId()});
        cursor.moveToFirst();

        return cursor.getInt(0) > 0;
    }*/

    public List<Asistencia> getAsistencias(Clase clase, Date fecha) {
        List<Asistencia> shopList = new ArrayList<>();

        String selectQuery = "SELECT a.id, a.estado, a.claseestudiante_id, a.clase_id, a.estudiante_id, ce.orden, e.nombres, e.apellidos FROM asistencia a, clase_estudiante ce, estudiante e where a.claseestudiante_id=ce.id and a.estudiante_id=e.id and a.clase_id = " + clase.getId() + " and a.fecha = date('" + sd.format(fecha) + "')";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Asistencia a = new Asistencia();
                a.setId(cursor.getInt(0));
                a.setEstado(cursor.getString(1));
                a.setFecha(fecha);

                ClaseEstudiante ce = new ClaseEstudiante(cursor.getInt(2));
                Clase c = new Clase(cursor.getInt(3));
                Estudiante e = new Estudiante(cursor.getInt(4));
                ce.setClase(c);
                ce.setEstudiante(e);

                a.setClaseEstudiante(ce);
                a.setClase(c);
                a.setEstudiante(e);

                ce.setOrden(cursor.getInt(5));
                e.setNombres(cursor.getString(6));
                e.setApellidos(cursor.getString(7));

                shopList.add(a);
            } while (cursor.moveToNext());
        }

        return shopList;
    }

    public void borrarAsistencias(Clase clase, Date fecha) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("asistencia", "clase_id = ? and fecha=date(?)", new String[] { String.valueOf(clase.getId()), sd.format(fecha) });
        db.close();
    }

}
