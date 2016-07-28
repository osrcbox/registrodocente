package com.unl.lapc.registrodocente.dao;

/**
 * Created by Usuario on 11/07/2016.
 */
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import android.util.Log;

import com.unl.lapc.registrodocente.modelo.PeriodoAcademico;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DBHandler extends SQLiteOpenHelper {

    // Database Version
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "registroDocente.db";


    public DBHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        SimpleDateFormat sf = new SimpleDateFormat("yyyy");
        String syear = sf.format(new Date());

        String CREATE_PA = "CREATE TABLE periodoacademico(id INTEGER PRIMARY KEY AUTOINCREMENT, nombre TEXT)";
        String CREATE_CL = "CREATE TABLE clase(id INTEGER PRIMARY KEY AUTOINCREMENT, periodo_id INTEGER, nombre TEXT, activa BOOLEAN DEFAULT 1)";
        String CREATE_ES = "CREATE TABLE estudiante(id INTEGER PRIMARY KEY AUTOINCREMENT, codigo TEXT, nombres TEXT, apellidos TEXT, sexo TEXT, email TEXT, celular TEXT)";
        String CREATE_ES_CL = "CREATE TABLE clase_estudiante(id INTEGER PRIMARY KEY AUTOINCREMENT, clase_id INTEGER NOT NULL, estudiante_id INTEGER NOT NULL, orden INTEGER)";
        String CREATE_AS = "CREATE TABLE asistencia(id INTEGER PRIMARY KEY AUTOINCREMENT, claseestudiante_id INTEGER NOT NULL, clase_id INTEGER NOT NULL, estudiante_id INTEGER NOT NULL, fecha DATE, estado TEXT)";

        db.execSQL(CREATE_PA);
        db.execSQL(CREATE_CL);
        db.execSQL(CREATE_ES);
        db.execSQL(CREATE_ES_CL);
        db.execSQL(CREATE_AS);

        db.execSQL("insert into periodoacademico (nombre) values ('Periodo " + syear +"')");
        db.execSQL("insert into clase (nombre, activa, periodo_id) values ('Clase 1', 1, 1)");
        db.execSQL("insert into estudiante (codigo, nombres, apellidos, sexo) values ('001','Jhon', 'Doe', 'Hombre')");
        db.execSQL("insert into clase_estudiante (clase_id, estudiante_id) values (1, 1)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 2)
        {
            //db.execSQL("ALTER TABLE clase_estudiante  ADD COLUMN orden INTEGER");
            //Log.i("Actualización", "Versión 2");
        }

    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        if (!db.isReadOnly()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                db.setForeignKeyConstraintsEnabled(true);
            } else {
                db.execSQL("PRAGMA foreign_keys=ON");
            }
        }
    }

}
