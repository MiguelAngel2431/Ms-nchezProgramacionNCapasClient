
package com.digis01.MsanchezProgramacionNCapas.ML;

import java.util.List;

public class Result <T> {
    
    public boolean correct;
    public String errorMessage;
    public Exception ex;
    public T object;
    public List<T> objects;
    
    // ✅ Este campo es necesario
    public List<ErrorCM> listaErrores;

    // (opcional: getters y setters)
    public List<ErrorCM> getListaErrores() {
        return listaErrores;
    }

    public void setListaErrores(List<ErrorCM> listaErrores) {
        this.listaErrores = listaErrores;
    }
    
}
