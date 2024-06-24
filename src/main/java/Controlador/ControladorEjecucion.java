/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controlador;

import Algoritmos.FifoScheduler1;
import Dao.DaoContenedor;
import Dao.DaoEjecucion;
import cliente_docker.versionesContenedores.Contenedor1;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.Time;
import java.util.List;
import logica.Ejecucion;

/**
 *
 * @author andresuv
 */
public class ControladorEjecucion {
    DaoEjecucion daoE;
    DaoContenedor daoC;
    FifoScheduler1 fifo;
   
    
    ControladorEjecucion(){
    daoE=new DaoEjecucion();
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
    
   
    
    public String EjecutarAlgoritmo(String nombreAlgoritmo,int listado_id){
    List<Contenedor1> conts=daoC.DaobtenerConetenedoresListado(listado_id);
    switch (nombreAlgoritmo) {
    case "fifo":
        fifo=new FifoScheduler1(conts);
        fifo.ejecutarContenedores();
       //List<Contenedor1> contenedoresProcesados=fifo.getContenedores();
        for(Contenedor1 c:conts){
        int actualizarC=daoC.DaoActualizarContenedor(c);
            System.out.println("resultado de ActualizarContenedor: "+actualizarC);
            
        }
        
        Object[] fechaHora=fechaHoraActual();
        int crearEjecucion=insertarEjecucion(listado_id,"FIFO",fifo.getTornaroundTimeP(),fifo.getResponseTimeP(),(Date)fechaHora[1],(Time)fechaHora[2]);
        
        
        
        
        
        return fifo.getResultadoE();
        
   
    // se pueden agregar más casos según sea necesario
    default:
        // código a ejecutar si ninguno de los casos anteriores coincide
        return null;

    }
    
    
    
  
    }
    
    
    
}
    

