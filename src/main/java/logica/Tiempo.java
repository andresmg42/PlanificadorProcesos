/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package logica;

/**
 *
 * @author andresuv
 */
public class Tiempo {
    private int tiempo_id;
    private double t_inicial;
    private double t_ejecucion;
    
    public Tiempo(){}

    public Tiempo( double t_inicial, double t_ejecucion) {
       
        this.t_inicial = t_inicial;
        this.t_ejecucion = t_ejecucion;
    }
    
    

    public int getTiempo_id() {
        return tiempo_id;
    }

    public void setTiempo_id(int tiempo_id) {
        this.tiempo_id = tiempo_id;
    }

    public double getT_inicial() {
        return t_inicial;
    }

    public void setT_inicial(double t_inicial) {
        this.t_inicial = t_inicial;
    }

    public double getT_ejecucion() {
        return t_ejecucion;
    }

    public void setT_ejecucion(double t_ejecucion) {
        this.t_ejecucion = t_ejecucion;
    }
    
    
}
