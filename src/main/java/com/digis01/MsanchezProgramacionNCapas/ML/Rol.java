
package com.digis01.MsanchezProgramacionNCapas.ML;

public class Rol {
    
    private int IdRol;
    private String Nombre;
    
    //Constructores
    public Rol(){}
    
    public Rol(int idRol, String nombre) {
        this.IdRol = idRol;
        this.Nombre = nombre;
    }
    
    //Getter y setter de idRol
    public void setIdRol(int idRol) {
        this.IdRol = idRol;
    }
    
    public int getIdRol() {
        return this.IdRol;
    }
    
    //Getter y setter de Nombre
    public void setNombre(String nombre) {
        this.Nombre = nombre;
    }
    
    public String getNombre() {
        return this.Nombre;
    }
    
}
