package com.unl.lapc.registrodocente.modelo;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Usuario on 19/07/2016.
 */
public class ClaseEstudiante implements Parcelable {

    private int id;
    private Clase clase;
    private Estudiante estudiante;
    private int orden;

    public ClaseEstudiante() {
    }

    public ClaseEstudiante(int id) {
        this.id = id;
    }

    public ClaseEstudiante(Clase clase, Estudiante estudiante) {
        this.clase = clase;
        this.estudiante = estudiante;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    private ClaseEstudiante(Parcel in) {
        super();
        this.id = in.readInt();
        this.clase = new Clase(in.readInt());
        this.estudiante = new Estudiante(in.readInt());
        this.orden = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(getId());
        parcel.writeInt(clase.getId());
        parcel.writeInt(estudiante.getId());
        parcel.writeInt(orden);
    }

    public static final Creator<ClaseEstudiante> CREATOR = new Creator<ClaseEstudiante>() {
        public ClaseEstudiante createFromParcel(Parcel in) {
            return new ClaseEstudiante(in);
        }

        public ClaseEstudiante[] newArray(int size) {
            return new ClaseEstudiante[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Clase getClase() {
        return clase;
    }

    public void setClase(Clase clase) {
        this.clase = clase;
    }

    public Estudiante getEstudiante() {
        return estudiante;
    }

    public void setEstudiante(Estudiante estudiante) {
        this.estudiante = estudiante;
    }

    public int getOrden() {
        return orden;
    }

    public void setOrden(int orden) {
        this.orden = orden;
    }
}
