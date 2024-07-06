/*package Algoritmos;

import cliente_docker.versionesContenedores.Contenedor1;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;


public class FifoScheduler1 {

    private Queue<Contenedor1> contenedorQueue;
    long tiempo0;
    int numContenedores;
    List<Contenedor1> contenedores;
    long totalTornaroundTime;
    long totalResponseTime;
    long tornaroundTimeP;
    long responseTimeP;
    String resultadoE;

    public FifoScheduler1(List<Contenedor1> contenedores) {
        this.contenedorQueue = new LinkedList<>();

        this.numContenedores = contenedores.size();
        this.contenedores = contenedores;
        this.tiempo0 = 0;
        for (Contenedor1 cont : contenedores) {
            contenedorQueue.offer(cont);

        }

      
    }

    public List<Contenedor1> getContenedores() {
        return contenedores;
    }

    public void setContenedores(List<Contenedor1> contenedores) {
        this.contenedores = contenedores;
    }

    public long getTornaroundTimeP() {
        return tornaroundTimeP;
    }

    public void setTornaroundTimeP(long tornaroundTimeP) {
        this.tornaroundTimeP = tornaroundTimeP;
    }

    public long getResponseTimeP() {
        return responseTimeP;
    }

    public void setResponseTimeP(long responseTimeP) {
        this.responseTimeP = responseTimeP;
    }

    public String getResultadoE() {
        return resultadoE;
    }

    public Queue<Contenedor1> getContenedorQueue() {
        return contenedorQueue;
    }

    public void setContenedorQueue(Queue<Contenedor1> contenedorQueue) {
        this.contenedorQueue = contenedorQueue;
    }
    
    public void ejecutarContenedores() {
        totalTornaroundTime = 0;
        totalResponseTime = 0;
        long count = 0;
        resultadoE="";
        while (!contenedorQueue.isEmpty()) {
            Contenedor1 contenedor = contenedorQueue.poll();

            
            resultadoE+="Ejecutando contenedor: "+ contenedor.getNombreI()+"\n";
            String resultado = contenedor.ejecutarContenedor();
            if (contenedorQueue.size() == numContenedores - 1) {
                tiempo0 = contenedor.getTiempo0();
            }

            if (contenedorQueue.peek() != null) {
                contenedorQueue.peek().setTiempo0(tiempo0);
            }

            resultadoE+="Resultado:\n"+resultado+"\n";
            
            resultadoE+="Tiempo llegada: " + contenedor.getTiempoLlegada()+"\n";
            
            resultadoE+="Tiempo inicial: " + contenedor.getTiempoInicio()+"\n";
            
            resultadoE+="Tiempo final: " + contenedor.getTiempoFinal()+"\n";
          
            resultadoE+="Tiempo real estimado: " +contenedor.getTiempoEstimadoReal()+"\n";
            
            resultadoE+="Tiempo estimado Ingresado:" + contenedor.getTiempoEstimadoIngresado()+"\n";
            
            resultadoE+="TornaroundTime: "+contenedor.getTornaroundTime()+"\n";
            
            resultadoE+="ResponseTime: "+contenedor.getResponseTime()+"\n\n";
            
            totalResponseTime += contenedor.getResponseTime();
            totalTornaroundTime += contenedor.getTornaroundTime();
            count++;
        }

        tornaroundTimeP = totalTornaroundTime / count;
        responseTimeP = totalResponseTime / count;
        resultadoE+="TornaroundTimePromedio: "+totalTornaroundTime/count+"\n";
        resultadoE+="ResponseTimePromedio: "+totalResponseTime/count+"\n\n";
      
    }

    public static void main(String[] args) {
        
        
    }
}*/
