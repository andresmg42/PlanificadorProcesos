/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controlador;

import Algoritmos.FifoScheduler1;
import Algoritmos.RoundRobin;
import Algoritmos.SRT;
import Dao.DaoContenedor;
import Dao.DaoEjecucion;
import cliente_docker.versionesContenedores.Contenedor1;
import cliente_docker.versionesContenedores.Contenedor3;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.Time;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import logica.Ejecucion;

/**
 *
 * @author andresuv
 */
public class ControladorEjecucion {
    DaoEjecucion daoE;
    DaoContenedor daoC;
    FifoScheduler1 fifo;
    RoundRobin RR;
   
    
    ControladorEjecucion(){
    daoE=new DaoEjecucion();
    daoC=new DaoContenedor();
    }
    
    public int insertarEjecucion(int listado_id,String algoritmo, long tornaroundTimeP,long responseTimeP,Date fecha,Time hora){
    Ejecucion e=new Ejecucion();
    e.setListado_id(listado_id);
    e.setAlgoritmo(algoritmo);
    e.setFecha(fecha);
    e.setTime(hora);
    e.setTornaroundTimeP(tornaroundTimeP);
    e.setResponseTimeP(responseTimeP);
    return daoE.DaoInsertarEjecucion(e);
   
    }
    
    public ResultSet listarEjecucion(){
     return daoE.DaolistarEjecucion();
    }
    
     public Object[] fechaHoraActual() {
        // Capturar la fecha y hora actual
        java.util.Date utilDate = new java.util.Date();  // Captura la fecha y hora actual

        // Convertir a java.sql.Date
        java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());  // Convierte a java.sql.Date

        // Convertir a java.sql.Time
        java.sql.Time sqlTime = new java.sql.Time(utilDate.getTime());  // Convierte a java.sql.Time

        // Imprimir resultados
        return new Object[]{sqlDate,sqlTime};
    }
     
    public List<Contenedor1> ordenarContenedores(List<Contenedor1> contenedores) {
        Collections.sort(contenedores, new Comparator<Contenedor1>() {
            @Override
            public int compare(Contenedor1 c1, Contenedor1 c2) {
                return Long.compare(c1.getTiempoLlegada(), c2.getTiempoLlegada());
            }
        });
        return contenedores;
    }
    
      public List<Contenedor3> ordenarContenedores3(List<Contenedor3> contenedores) {
        Collections.sort(contenedores, new Comparator<Contenedor3>() {
            @Override
            public int compare(Contenedor3 c1, Contenedor3 c2) {
                return Long.compare(c1.getTiempoLlegada(), c2.getTiempoLlegada());
            }
        });
        return contenedores;
    }
    
   
    
    public String EjecutarAlgoritmo(String nombreAlgoritmo,int listado_id) throws InterruptedException{
    List<Contenedor1> conts=ordenarContenedores(daoC.DaobtenerConetenedoresListado(listado_id));
    List<Contenedor3> conts3=ordenarContenedores3(daoC.DaobtenerConetenedoresListado3(listado_id));

    switch (nombreAlgoritmo) {
    case "FIFO":
        fifo=new FifoScheduler1(conts);
        fifo.ejecutarContenedores();
      
        for(Contenedor1 c:conts){
        int actualizarC=daoC.DaoActualizarContenedor(c);
            System.out.println("resultado de ActualizarContenedor: "+actualizarC);
            
        }
        
        Object[] fechaHora=fechaHoraActual();
        int crearEjecucion=insertarEjecucion(listado_id,"FIFO",fifo.getTornaroundTimeP(),fifo.getResponseTimeP(),(Date)fechaHora[0],(Time)fechaHora[1]);
        System.out.println("resultado crearEjecucion: "+crearEjecucion);
        
        
        
        
        
        return fifo.getResultadoE();
        
    case "RoundRobin": 
        RR=new RoundRobin(conts3,2);
        RR.ejecutarContenedores();
        for(Contenedor3 c:conts3){
        int actualizarC=daoC.DaoActualizarContenedor3(c);
            System.out.println("resultado de ActualizarContenedor: "+actualizarC);
        }
        
        Object[] fechaH=fechaHoraActual();
        int crearEjec=insertarEjecucion(listado_id,"RoundRobin",RR.getTornaroundTimeP(),RR.getResponseTimeP(),(Date)fechaH[0],(Time)fechaH[1]);
        System.out.println("resultado crearEjecucion: "+crearEjec);
        return RR.getResultadoE();
        
    case "SRTNS": 
        SRT S=new SRT(conts);
        S.runScheduler();
        for(Contenedor1 c:S.getContenedoresCompletos()){
        int actualizarC=daoC.DaoActualizarContenedor(c);
            System.out.println("resultado de ActualizarContenedor: "+actualizarC);
        }
        
        Object[] fecha=fechaHoraActual();
        int crearE=insertarEjecucion(listado_id,"SRT",S.getTornaroundTimeP(),S.getResponseTimeP(),(Date)fecha[0],(Time)fecha[1]);
        System.out.println("resultado crearEjecucion: "+crearE);
        return S.getResultado();
        
    // se pueden agregar más casos según sea necesario
    default:
        // código a ejecutar si ninguno de los casos anteriores coincide
        return null;

    
    
    }
    
  
    }
    
    public static void main(String[] args) {
        ControladorEjecucion cont=new ControladorEjecucion();
        try {
            System.out.println(cont.EjecutarAlgoritmo("RoundRobin", 2));
        } catch (InterruptedException ex) {
            Logger.getLogger(ControladorEjecucion.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
    }
    
    
    
}
    

