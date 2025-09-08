package com.digis01.MsanchezProgramacionNCapas.ML;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.springframework.format.annotation.DateTimeFormat;

public class Usuario {

    //@NotNull()
    private int IdUsuario;
    private String Nombre;

   
    private String ApellidoPaterno;

    
    private String ApellidoMaterno;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date FechaNacimiento;
    private int NumeroHermanos;
    private double PromedioEscolar;

    //Nuevos parametros
   
    private String UserName;

//    @Pattern(regexp = "^[a-zA-Z-0-9]+[^-|\\s]+@[a-zA-Z]+\\.[a-z]+", message = "Formato de correo invalido")
    
    private String Email;
    private String Password;
    private String Sexo;

    
    private String Telefono;

    
    private String Celular;

   
    private String Curp;

    //Baja logica
    private int Status;

    public int IdRol;

    public Rol Rol; //Propiedad de navegacion (no ocupa setter ni getter, porque es public)

    public List<Direccion> Direcciones;

    private String Imagen;

    //Constructores
    public Usuario() {
    }

    public Usuario(int idUsuario, String nombre, String apellidoPaterno, String apellidoMaterno, Date fechaNacimiento, int numeroHermanos, double promedioEscolar, String userName, String email, String password, String sexo, String telefono, String celular, String curp, int idRol, int status) {
        this.IdUsuario = idUsuario;
        this.Nombre = nombre;
        this.ApellidoPaterno = apellidoPaterno;
        this.ApellidoMaterno = apellidoMaterno;
        this.FechaNacimiento = fechaNacimiento;
        this.NumeroHermanos = numeroHermanos;
        this.PromedioEscolar = promedioEscolar;

        //Nuevos parametros
        this.UserName = userName;
        this.Email = email;
        this.Password = password;
        this.Sexo = sexo;
        this.Telefono = telefono;
        this.Celular = celular;
        this.Curp = curp;
        this.IdRol = IdRol;

        this.Status = status;
    }

    //Constructor para el buscador
    public Usuario(String Nombre, String ApellidoPaterno, String ApellidoMaterno, int IdRol) {
        this.Nombre = Nombre;
        this.ApellidoPaterno = ApellidoPaterno;
        this.ApellidoMaterno = ApellidoMaterno;
        this.IdRol = IdRol;
    }

    // Getter y Setter de idUsuario
    public void setIdUsuario(int idUsuario) {
        this.IdUsuario = idUsuario;
    }

    public int getIdUsuario() {
        return this.IdUsuario;
    }

    //Getter y Setter de nombre
    public void setNombre(String nombre) {
        this.Nombre = nombre;
    }

    public String getNombre() {
        return this.Nombre;
    }

    //Getter y Setter de apellidoPaterno
    public void setApellidoPaterno(String apellidoPaterno) {
        this.ApellidoPaterno = apellidoPaterno;
    }

    public String getApellidoPaterno() {
        return this.ApellidoPaterno;
    }

    //Getter y Setter de apellidoMaterno
    public void setApellidoMaterno(String apellidoMaterno) {
        this.ApellidoMaterno = apellidoMaterno;
    }

    public String getApellidoMaterno() {
        return this.ApellidoMaterno;
    }

    //Getter y Setter de apellidoMaterno
    public void setFechaNacimiento(Date fechaNacimiento) {
        this.FechaNacimiento = fechaNacimiento;
    }

    public Date getFechaNacimiento() {
        return this.FechaNacimiento;
    }

    //Getter y Setter de numeroHermanos
    public void setNumeroHermanos(int numeroHermanos) {
        this.NumeroHermanos = numeroHermanos;
    }

    public int getNumeroHermanos() {
        return this.NumeroHermanos;
    }

    //Getter y Setter de promedioEscolar
    public void setPromedioEscolar(double promedioEscolar) {
        this.PromedioEscolar = promedioEscolar;
    }

    public double getPromedioEscolar() {
        return this.PromedioEscolar;
    }

    //Getter y Setter de userName
    public void setUserName(String userName) {
        this.UserName = userName;
    }

    public String getUserName() {
        return this.UserName;
    }

    //Getter y Setter de email
    public void setEmail(String email) {
        this.Email = email;
    }

    public String getEmail() {
        return this.Email;
    }

    //Getter y Setter de password
    public void setPassword(String password) {
        this.Password = password;
    }

    public String getPassword() {
        return this.Password;
    }

    //Getter y Setter de sexo
    public void setSexo(String sexo) {
        this.Sexo = sexo;
    }

    public String getSexo() {
        return this.Sexo;
    }

    //Getter y Setter de telefono
    public void setTelefono(String telefono) {
        this.Telefono = telefono;
    }

    public String getTelefono() {
        return this.Telefono;
    }

    //Getter y Setter de celular
    public void setCelular(String celular) {
        this.Celular = celular;
    }

    public String getCelular() {
        return this.Celular;
    }

    //Getter y Setter de curp
    public void setCurp(String curp) {
        this.Curp = curp;
    }

    public String getCurp() {
        return this.Curp;
    }

    //Getter y Stter de idRol
    public void setIdRol(int idRol) {
        this.IdRol = idRol;
    }

    public int getIdRol() {
        return this.IdRol;
    }

    //Getter y Setter de Rol
    public void setRol(Rol Rol) {
        this.Rol = Rol;
    }

    public Rol getRol() {
        return Rol;
    }

    //Getter y Setter de la lista de Direcciones
    public List<Direccion> getDirecciones() {
        return Direcciones;
    }

    public void setDirecciones(List<Direccion> Direcciones) {
        this.Direcciones = Direcciones;
    }

    //Getter y Setter de imagen
    public void setImagen(String imagen) {
        this.Imagen = imagen;
    }

    public String getImagen() {
        return this.Imagen;
    }

    //Getter y Setter de imagen
    public void setStatus(int status) {
        this.Status = status;
    }

    public int getStatus() {
        return this.Status;
    }
    /*@Override
    public String toString() {
        return "Usuario{" +
                "IdUsuario='" + IdUsuario + '\'' +
                ", Nombre='" + Nombre + '\'' +
                ", ApellidoPaterno='" + ApellidoPaterno + '\'' +
                ", ApellidoMaterno='" + ApellidoMaterno + '\'' +
                ", FechaNacimiento='" + FechaNacimiento + '\'' +
                ", NumeroHermanos='" + NumeroHermanos + '\'' +
                ", PromedioEscolar='" + PromedioEscolar + '\'' +
                '}';
    }*/

}
