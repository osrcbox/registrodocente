package com.unl.lapc.registrodocente.modelo;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Usuario on 11/07/2016.
 */
public class Estudiante implements Parcelable{

    private int id;

    private String codigo;
    private String nombres;
    private String apellidos;
    private String sexo = "Hombre";

    private String email;
    private String celular;

    public Estudiante(){
    }

    public Estudiante(int id){
        this.id = id;
    }

    public Estudiante(int id, String nombres, String apellidos){
        this.setId(id);
        this.setNombres(nombres);
        this.setApellidos(apellidos);
    }

    public Estudiante(int id, String codigo, String nombres, String apellidos){
        this.setId(id);
        this.setCodigo(codigo);
        this.setNombres(nombres);
        this.setApellidos(apellidos);
    }

    public Estudiante(int id, String codigo, String nombres, String apellidos, String email, String celular, String sexo){
        this.setId(id);
        this.setCodigo(codigo);
        this.setNombres(nombres);
        this.setApellidos(apellidos);
        this.setEmail(email);
        this.setCelular(celular);
        this.setSexo(sexo);
    }

    private Estudiante(Parcel in) {
        super();
        this.setId(in.readInt());
        this.setCodigo(in.readString());
        this.setNombres(in.readString());
        this.setApellidos(in.readString());
        this.setSexo(in.readString());
        this.setEmail(in.readString());
        this.setCelular(in.readString());
    }

    public String getNombresCompletos() {
        return apellidos + " " + nombres;
    }

    @Override
    public String toString() {
        return apellidos + " " + nombres;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(getId());
        parcel.writeString(getCodigo());
        parcel.writeString(getNombres());
        parcel.writeString(getApellidos());
        parcel.writeString(getSexo());
        parcel.writeString(getEmail());
        parcel.writeString(getCelular());
    }

    public static final Creator<Estudiante> CREATOR = new Creator<Estudiante>() {
        public Estudiante createFromParcel(Parcel in) {
            return new Estudiante(in);
        }
        public Estudiante[] newArray(int size) {
            return new Estudiante[size];
        }
    };

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCelular() {
        return celular;
    }

    public void setCelular(String celular) {
        this.celular = celular;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }
}
