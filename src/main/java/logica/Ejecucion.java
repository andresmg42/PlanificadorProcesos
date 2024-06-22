/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package logica;

import java.sql.Date;
import java.sql.Time;

/**
 *
 * @author andresuv
 */
public class Ejecucion {
    private int ejecucion_id;
    private int listado_id;
    private String nombre;
    private String algoritmo;
    private Date fecha;
    private Time Time;
    
    public Ejecucion(){}

    public Ejecucion(int ejecucion_id, int listado_id, String nombre, String algoritmo, Date fecha, Time Time) {
        this.ejecucion_id = ejecucion_id;
        this.listado_id = listado_id;
        this.nombre = nombre;
        this.algoritmo = algoritmo;
        this.fecha = fecha;
        this.Time = Time;
    }
    
    

    public int getEjecucion_id() {
        return ejecucion_id;
    }

    public void setEjecucion_id(int ejecucion_id) {
        this.ejecucion_id = ejecucion_id;
    }

    public int getListado_id() {
        return listado_id;
    }

    public void setListado_id(int listado_id) {
        this.listado_id = listado_id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getAlgoritmo() {
        return algoritmo;
    }

    public void setAlgoritmo(String algoritmo) {
        this.algoritmo = algoritmo;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public Time getTime() {
        return Time;
    }

    public void setTime(Time Time) {
        this.Time = Time;
    }
    
    
    
    
}
