/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package logica;

/**
 *
 * @author andresuv
 */
public class Resultado {
    private int resultado_id;
    private int ejecucion_id;
    private double t_final;
    private double response_time;
    private double tornaround_time;
    private double response_time_p;
    private double tornarnaroun_time_p;
    
    public Resultado(){}

    public Resultado(int resultado_id, int ejecucion_id, double t_final, double response_time, double tornaround_time) {
        this.resultado_id = resultado_id;
        this.ejecucion_id = ejecucion_id;
        this.t_final = t_final;
        this.response_time = response_time;
        this.tornaround_time = tornaround_time;
    }
    
    

    public int getResultado_id() {
        return resultado_id;
    }

    public void setResultado_id(int resultado_id) {
        this.resultado_id = resultado_id;
    }

    public int getEjecucion_id() {
        return ejecucion_id;
    }

    public void setEjecucion_id(int ejecucion_id) {
        this.ejecucion_id = ejecucion_id;
    }

    public double getT_final() {
        return t_final;
    }

    public void setT_final(double t_final) {
        this.t_final = t_final;
    }

    public double getResponse_time() {
        return response_time;
    }

    public void setResponse_time(double response_time) {
        this.response_time = response_time;
    }

    public double getTornaround_time() {
        return tornaround_time;
    }

    public void setTornaround_time(double tornaround_time) {
        this.tornaround_time = tornaround_time;
    }
    
     public double getResponse_time_p() {
        return response_time_p;
    }

    public void setResponse_time_p(double response_time_p) {
        this.response_time_p = response_time_p;
    }

    public double getTornarnaroun_time_p() {
        return tornarnaroun_time_p;
    }

    public void setTornarnaroun_time_p(double tornarnaroun_time_p) {
        this.tornarnaroun_time_p = tornarnaroun_time_p;
    }
    
    
    
}
