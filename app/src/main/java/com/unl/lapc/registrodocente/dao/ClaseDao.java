package com.unl.lapc.registrodocente.dao;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.unl.lapc.registrodocente.modelo.Clase;
import com.unl.lapc.registrodocente.modelo.ClaseEstudiante;
import com.unl.lapc.registrodocente.modelo.Estudiante;
import com.unl.lapc.registrodocente.modelo.PeriodoAcademico;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Usuario on 15/07/2016.
 */
public class ClaseDao extends DBHandler {

    public static final String TABLE_NAME = "clase";
    public static final String ID = "id";
    public static final String NOMBRE = "nombre";
    public static final String ACTIVA = "activa";
    public static final String PERIODO_ID = "periodo_id";

    public ClaseDao(Context context){
        super(context);
    }

    public void add(Clase clase) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(NOMBRE, clase.getNombre());
        values.put(ACTIVA, clase.isActiva() ? 1 : 0);
        values.put(PERIODO_ID, clase.getPeriodo().getId());

        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    public void addEstudiante(ClaseEstudiante cls) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("clase_id", cls.getClase().getId());
        values.put("estudiante_id", cls.getEstudiante().getId());
        values.put("orden", cls.getOrden());

        if(cls.getId() == 0){
            int id = (int) db.insert("clase_estudiante", null, values);
            cls.setId(id);
        }else{
            db.update("clase_estudiante", values, ID + " = ?", new String[]{String.valueOf(cls.getId())});
        }
        db.close();
    }

    public void ordernarApellidos(Clase cls) {
        SQLiteDatabase db = this.getWritableDatabase();

        String selectQuery = "SELECT ce.id FROM clase_estudiante ce, estudiante e where ce.estudiante_id = e.id and ce.clase_id = " + cls.getId() + " order by e.apellidos asc, e.nombres asc";

        Cursor cursor = db.rawQuery(selectQuery, null);
        List<Integer> lista = new ArrayList<>();

        if (cursor.moveToFirst()) {

            do {
                lista.add(cursor.getInt(0));
            } while (cursor.moveToNext());
        }

        int orden = 1;
        for (int id: lista){
            ContentValues values = new ContentValues();
            values.put("orden", orden);
            db.update("clase_estudiante", values, ID + " = ?", new String[]{String.valueOf(id)});
            orden++;
        }

        db.close();
    }

    public void removerEstudiante(ClaseEstudiante cls) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("clase_estudiante", ID + " = ?", new String[] { String.valueOf(cls.getId()) });
        db.close();
    }

    public int update(Clase clase) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(NOMBRE, clase.getNombre());
        values.put(ACTIVA, clase.isActiva() ? 1 : 0);
        values.put(PERIODO_ID, clase.getPeriodo().getId());


        return db.update(TABLE_NAME, values, ID + " = ?", new String[]{String.valueOf(clase.getId())});
    }

    public Clase getClase(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, new String[] { ID, NOMBRE, ACTIVA, PERIODO_ID}, ID + "=?", new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();



        Clase contact = new Clase(Integer.parseInt(cursor.getString(0)), cursor.getString(1), cursor.getInt(2)>0);

        String spid = cursor.getString(3);
        if(spid != null){
            PeriodoAcademico p = new PeriodoAcademico();
            p.setId(Integer.parseInt(spid));
            contact.setPeriodo(p);
        }


        return contact;
    }

    public void deleteClase(Clase clase) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, ID + " = ?", new String[] { String.valueOf(clase.getId()) });
        db.close();
    }

    public List<Clase> getAll() {
        List<Clase> shopList = new ArrayList<>();

        String selectQuery = "SELECT c.id, c.nombre, c.activa, c.periodo_id, p.nombre periodo_nombre FROM clase c, periodoAcademico p where c.periodo_id = p.id";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Clase cls = new Clase();
                cls.setId(Integer.parseInt(cursor.getString(0)));
                cls.setNombre(cursor.getString(1));
                cls.setActiva(cursor.getInt(2) > 0);

                PeriodoAcademico p = new PeriodoAcademico(cursor.getInt(3), cursor.getString(4));
                cls.setPeriodo(p);

                shopList.add(cls);
            } while (cursor.moveToNext());
        }

        return shopList;
    }

    public List<ClaseEstudiante> getEstudiantes(Clase clase) {
        List<ClaseEstudiante> shopList = new ArrayList<>();

        String selectQuery = "SELECT e.id estudiante_id, e.codigo, e.nombres, e.apellidos, ce.id, ce.orden FROM clase_estudiante ce, estudiante e where ce.estudiante_id = e.id and ce.clase_id = " + clase.getId() + " order by ce.orden asc";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                ClaseEstudiante cls = new ClaseEstudiante();
                cls.setClase(clase);

                Estudiante e = new Estudiante(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3));
                cls.setEstudiante(e);

                cls.setId(cursor.getInt(4));
                cls.setOrden(cursor.getInt(5));

                shopList.add(cls);
            } while (cursor.moveToNext());
        }

        return shopList;
    }

    public List<Clase> getMainClases() {
        List<Clase> shopList = new ArrayList<>();

        String selectQuery = "SELECT c.id, c.nombre, c.activa, (select count(ce.id) from clase_estudiante ce where ce.clase_id = c.id) as numeroEstudiantes, periodo_id FROM clase c WHERE c.activa = 1";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Clase cls = new Clase();
                cls.setId(Integer.parseInt(cursor.getString(0)));
                cls.setNombre(cursor.getString(1));
                cls.setActiva(cursor.getInt(2) > 0);
                cls.setNumeroEstudiantes(cursor.getInt(3));

                PeriodoAcademico p = new PeriodoAcademico();
                p.setId(cursor.getInt(3));
                cls.setPeriodo(p);
                shopList.add(cls);

            } while (cursor.moveToNext());
        }

        return shopList;
    }

    public boolean existe(Clase per){
        String selectQuery = "SELECT count(*) FROM " + TABLE_NAME + " where lower(trim(" + NOMBRE + ")) =? and " + ID +" <> ?";
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(selectQuery, new  String[]{per.getNombre().toLowerCase(), ""+per.getId()});
        cursor.moveToFirst();

        return cursor.getInt(0) > 0;
    }

}
