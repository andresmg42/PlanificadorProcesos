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
    private String algoritmo;
    private double tornaroundTimeP;
    private double responseTimeP;
    private Date fecha;
    private Time Time;
    
    public Ejecucion(){}

    public Ejecucion( int listado_id, String algoritmo,long tornaroundTimeP,long responseTimeP,Date fecha, Time Time) {
      
        this.listado_id = listado_id;
        this.algoritmo = algoritmo;
        this.tornaroundTimeP=tornaroundTimeP;
        this.responseTimeP=responseTimeP;
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

    public double getTornaroundTimeP() {
        return tornaroundTimeP;
    }

    public void setTornaroundTimeP(double tornaroundTimeP) {
        this.tornaroundTimeP = tornaroundTimeP;
    }

    public double getResponseTimeP() {
        return responseTimeP;
    }

    public void setResponseTimeP(double responseTimeP) {
        this.responseTimeP = responseTimeP;
    }
    
    
    
    
}
