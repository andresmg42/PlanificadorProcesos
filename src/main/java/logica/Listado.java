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
public class Listado {
    private int listado_id;
    private String nombre;
    private Date fecha;
    private Time hora;
    
    public Listado(){}

    public Listado( int listado_id,String nombre, Date fecha, Time hora) {
        this.nombre = nombre;
        this.fecha = fecha;
        this.hora = hora;
        this.listado_id=listado_id;
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

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public Time getHora() {
        return hora;
    }

    public void setHora(Time hora) {
        this.hora = hora;
    }
    
    
    
}
