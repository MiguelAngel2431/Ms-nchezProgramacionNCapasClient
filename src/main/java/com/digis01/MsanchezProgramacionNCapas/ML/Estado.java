
package com.digis01.MsanchezProgramacionNCapas.ML;

import java.util.List;

public class Estado {
    private int IdEstado;
    private String Nombre;
    //private int IdPais;
    
    //public Pais Pais; //Propiedad de navegacion (no ocupa setter ni getter, porque es public)
    
    public Pais Pais;
    
    //Constructores
    public Estado() {}
    
    public Estado(int idEstado, String nombre) {
        this.IdEstado = idEstado;
        this.Nombre = nombre;
        //this.IdPais = IdPais;
    }

    public Pais getPais() {
        return Pais;
    }

    public void setPais(Pais Pais) {
        this.Pais = Pais;
    }
    
    
    
    //Getter y Setter de idEstado
    public void setIdEstado(int idEstado) {
        this.IdEstado = idEstado;
    }
    
    public int getIdEstado() {
        return this.IdEstado;
    }
    
    //Getter y Setter de nombre
    public void setNombre(String nombre) {
        this.Nombre = nombre;
    }
    
    public String getNombre() {
        return this.Nombre;
    }
    
    //Getter y Setter de idPais
    /*public void setIdPais(int idPais) {
        this.IdPais = idPais;
    }
    
    public int getIdPais() {
        return this.IdPais;
    }*/
    
}
