
package com.digis01.MsanchezProgramacionNCapas.ML;

public class Pais {
    private int IdPais;
    private String Nombre;
    
    //Constructores
    public Pais() {}
    
    public Pais(int idPais, String nombre) {
        this.IdPais = idPais;
        this.Nombre = nombre;
    }
    
    //Getter y Setter de idPais
    public void setIdPais(int idPais) {
        this.IdPais = idPais;
    }
    
    public int getIdPais() {
        return this.IdPais;
    }
    
    //Getter y Setter de nombre
    public void setNombre(String nombre) {
        this.Nombre =nombre;
    }
    
    public String getNombre() {
        return this.Nombre;
    }
}
