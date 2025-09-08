
package com.digis01.MsanchezProgramacionNCapas.ML;


public class Direccion {
    private int IdDireccion;

    private String Calle;

    private String NumeroInterior;
    private String NumeroExterior;
    public int IdUsuario;

    public Colonia Colonia; //Propiedad de navegacion
    
    //Constructores
    public Direccion() {}
    
    public Direccion(int idDireccion, String calle, String numeroInterior, String numeroExterior, int idUsuario) {
        this.IdDireccion = idDireccion;
        this.Calle = calle;
        this.NumeroInterior = numeroInterior;
        this.NumeroExterior = numeroExterior;
        //this.IdColonia = idColonia;
        this.IdUsuario = idUsuario;
    }

    public Direccion(int IdDireccion) {
        this.IdDireccion = IdDireccion;
    }

    public int getIdUsuario() {
        return IdUsuario;
    }

    public void setIdUsuario(int IdUsuario) {
        this.IdUsuario = IdUsuario;
    }
    
    
    
    //Getter y setter de idColonia
    public void setIdDireccion(int idDireccion) {
        this.IdDireccion = idDireccion;
    }
    
    public int getIdDireccion() {
        return this.IdDireccion;
    }
    
    //Getter y setter de calle
    public void setCalle(String calle) {
        this.Calle = calle;
    }
    
    public String getCalle() {
        return this.Calle;
    }
    
    //Getter y setter de numeroInterior
    public void setNumeroInterior(String numeroInterior) {
        this.NumeroInterior = numeroInterior;
    }
    
    public String getNumeroInterior() {
        return this.NumeroInterior;
    }
    
    //Getter y setter de numeroExterior
    public void setNumeroExterior(String numeroExterior) {
        this.NumeroExterior = numeroExterior;
    }
    
    public String getNumeroExterior() {
        return this.NumeroExterior;
    }
    
    //Getter y setter de Colonia
    public void setColonia(Colonia Colonia) {
        this.Colonia = Colonia;
    }
    
    public Colonia getColonia() {
        return Colonia;
    }
    
    
    
    //Getter y setter de idColonia
    /*public void setIdColonia(int idColonia) {
        this.IdColonia = idColonia;
    }
    
    public int getIdColonia() {
        return this.IdColonia;
    }
    
    //Getter y Setter de idUsuario
    public void setIdUsuario(int idUsuario) {
        this.IdUsuario = idUsuario;
    }
    
    public int getIdUsuario() {
        return this.IdUsuario;
    }*/
    

    
}
