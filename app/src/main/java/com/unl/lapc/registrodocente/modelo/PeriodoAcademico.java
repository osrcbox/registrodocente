package com.unl.lapc.registrodocente.modelo;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Usuario on 11/07/2016.
 */
public class PeriodoAcademico implements Parcelable{

    private int id;
    private String nombre;

    public PeriodoAcademico(){
    }

    public PeriodoAcademico(int id, String nombre){
        this.id = id;
        this.nombre = nombre;
    }

    private PeriodoAcademico(Parcel in) {
        super();
        this.id = in.readInt();
        this.nombre = in.readString();
    }

    @Override
    public String toString() {
        return nombre;
    }

    @Override
    public int hashCode() {
        return id;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj ==null || !(obj instanceof PeriodoAcademico)){
            return false;
        }

        PeriodoAcademico other = (PeriodoAcademico)obj;

        return (other.getId() == this.getId());
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(getId());
        parcel.writeString(getNombre());
    }

    public static final Parcelable.Creator<PeriodoAcademico> CREATOR = new Parcelable.Creator<PeriodoAcademico>() {
        public PeriodoAcademico createFromParcel(Parcel in) {
            return new PeriodoAcademico(in);
        }

        public PeriodoAcademico[] newArray(int size) {
            return new PeriodoAcademico[size];
        }
    };
}
