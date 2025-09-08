
package com.digis01.MsanchezProgramacionNCapas.ML;

import java.util.List;

public class Municipio {
    private int IdMunicipio;
    private String Nombre;
    //private int idEstado;
    
    //public Estado Estado; //Propiedad de navegacion (no ocupa setter ni getter, porque es public)
    
    public Estado Estado;

    
    //Constructores
    public Municipio() {}
    
    public Municipio(int idMunicipio, String nombre) {
        this.IdMunicipio = idMunicipio;
        this.Nombre = nombre;
        //this.idEstado = idEstado;
    }

    public Estado getEstado() {
        return Estado;
    }

    public void setEstado(Estado Estado) {
        this.Estado = Estado;
    }
    
    
    
    //Getter y Setter de idMunicipio
    public void setIdMunicipio(int idMunicipio) {
        this.IdMunicipio = idMunicipio;
    }
    
    public int getIdMunicipio() {
        return this.IdMunicipio;
    }
    
    //Getter y Setter de nombre
    public void setNombre(String nombre) {
        this.Nombre = nombre;
    }
    
    public String getNombre() {
        return this.Nombre;
    }
    
    //Getter y Setter de idEstado
    /*public void setIdEstado(int idEstado) {
        this.idEstado = idEstado;
    }
    
    public int getIdEstado() {
        return this.idEstado;
    }*/
    
}
