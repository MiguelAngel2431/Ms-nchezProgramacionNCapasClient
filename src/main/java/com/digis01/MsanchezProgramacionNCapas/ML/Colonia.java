
package com.digis01.MsanchezProgramacionNCapas.ML;

import java.util.List;

public class Colonia {
    private int IdColonia;
    private String Nombre;
    private String CodigoPostal;
    //private int IdMunicipio;
    
    public Municipio Municipio;
    
    //public Municipio Municipio; //Propiedad de navegacion (no ocupa setter ni getter, porque es public)
    
    //Costructores
    public Colonia() {}
    
    public Colonia (int idColonia, String nombre, String codigoPostal) {
        this.IdColonia = idColonia;
        this.Nombre = nombre;
        this.CodigoPostal=  codigoPostal;
        //this.IdMunicipio = idMunicipio;
    }

    public Municipio getMunicipio() {
        return Municipio;
    }

    public void setMunicipio(Municipio Municipio) {
        this.Municipio = Municipio;
    }
    
    
    //Getter y Setter de idColonia
    public void setIdColonia(int idColonia) {
        this.IdColonia = idColonia;
    }
    
    public int getIdColonia() {
        return this.IdColonia;
    }
    
    //Getter y Setter de nombre
    public void setNombre(String nombre) {
        this.Nombre = nombre;
    }
    
    public String getNombre() {
        return this.Nombre;
    }
    
    //Getter y Setter de codigoPostal
    public void setCodigoPostal(String codigoPostal) {
        this.CodigoPostal = codigoPostal;
    }
    
    public String getCodigoPostal() {
        return this.CodigoPostal;
    }
    
    
    
    //Getter y Setter de idMunicipio
    /*public void setIdMunicipio(int idMunicipio) {
        this.IdMunicipio = idMunicipio;
    }
    
    public int getIdMunicipio() {
        return this.IdMunicipio;
    }*/

    
    
    
}
