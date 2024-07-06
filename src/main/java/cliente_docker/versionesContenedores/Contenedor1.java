/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cliente_docker.versionesContenedores;

/**
 *
 * @author andresuv
 */


public class Contenedor1 implements Comparable<Contenedor1>{

    
    String nombreI;
    String comando;
    int contenedor_id;
    long tiempoLlegada;
    long tiempoInicio;
    long tiempoEstimadoReal;
    long tiempoEstimadoIngresado;
    long tiempoFinal;
    long tornaroundTime;
    long responseTime;
    long tiempo0;
    long tiempoRestante;
    String containerId;
    String comparador;
    double NTAT;

    public Contenedor1(int contenedor_id, String nombreI, String comando, long tiempoLlegada, long tiempoEstimadoIngresado) {

        this.nombreI = nombreI;
        this.comando = comando;
        this.tiempoLlegada = tiempoLlegada;
        this.tiempoEstimadoIngresado = tiempoEstimadoIngresado;
        this.tiempoRestante = tiempoEstimadoIngresado;
        this.contenedor_id = contenedor_id;
        this.NTAT= 0.0;
        

        

    }
    
   @Override
    public int compareTo(Contenedor1 t) {
        if(comparador.equals("SRT")){
        return Long.compare(this.tiempoRestante, t.tiempoRestante);
        }else if(comparador.equals("SPN")){
        return Long.compare(this.tiempoEstimadoIngresado, t.tiempoEstimadoIngresado);
        }else if(comparador.equals("HRRN")){
        return Double.compare(this.NTAT,t.NTAT);
        }
        return -1;
        
    }


    public String getNombreI() {
        return nombreI;
    }

    public void setNombreI(String nombreI) {
        this.nombreI = nombreI;
    }

    public String getComando() {
        return comando;
    }

    public void setComando(String comando) {
        this.comando = comando;
    }

    public long getTiempoInicio() {
        return tiempoInicio;
    }

    public void setTiempoInicio(long tiempoInicio) {
        this.tiempoInicio = tiempoInicio;
    }

    public long getTiempoEstimadoReal() {
        return tiempoEstimadoReal;
    }

    public void setTiempoEstimadoReal(long tiempoEstimado) {
        this.tiempoEstimadoReal = tiempoEstimado;
    }

    public long getTiempoFinal() {
        return tiempoFinal;
    }

    public void setTiempoFinal(long tiempoFinal) {
        this.tiempoFinal = tiempoFinal;
    }

    public long getTiempoLlegada() {
        return tiempoLlegada;
    }

    public void setTiempoLlegada(long tiempoInicio2) {
        this.tiempoLlegada = tiempoInicio2;
    }

    public long getTiempoEstimadoIngresado() {
        return tiempoEstimadoIngresado;
    }

    public void setTiempoEstimadoIngresado(long tiempoEstimadoPromedio) {
        this.tiempoEstimadoIngresado = tiempoEstimadoPromedio;
    }

    public long getTornaroundTime() {
        return tornaroundTime;
    }

    public void setTornaroundTime(long tornaroundTime) {
        this.tornaroundTime = tornaroundTime;
    }

    public long getResponseTime() {
        return responseTime;
    }

    public void setResponseTime(long responseTime) {
        this.responseTime = responseTime;
    }

    public long getTiempo0() {
        return tiempo0;
    }

    public void setTiempo0(long tiempo0) {
        this.tiempo0 = tiempo0;
    }

    public int getContenedor_id() {
        return contenedor_id;
    }

    public void setContenedor_id(int contenedor_id) {
        this.contenedor_id = contenedor_id;
    }

    public long getTiempoRestante() {
        return tiempoRestante;
    }

    public void setTiempoRestante(long tiempoRestante) {
        this.tiempoRestante = tiempoRestante;
    }

    public String getContainerId() {
        return containerId;
    }

    public void setContainerId(String containerId) {
        this.containerId = containerId;
    }

    public String getComparador() {
        return comparador;
    }

    public void setComparador(String comparador) {
        this.comparador = comparador;
    }

    public double getNTAT() {
        return NTAT;
    }

    public void setNTAT(double NTAT) {
        this.NTAT = NTAT;
    }
    
    
    
    

//quitar sheduler y 
    public static void main(String[] args) {
        Contenedor1 cont = new Contenedor1(15, "sleep4", "sleep 4", 0,4);
      
        System.out.println(cont.getTiempoEstimadoReal());
    }

    

}
