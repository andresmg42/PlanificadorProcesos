package Algoritmos;

import cliente_docker.versionesContenedores.Contenedor3;
import cliente_docker.versionesContenedores.Contenedor4;
import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.WaitContainerResultCallback;

import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.DockerClientBuilder;
import java.io.IOException;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RoundRobin1 {

    private Queue<Contenedor4> contenedorQueue;
    long tiempo0;
    int numContenedores;
    List<Contenedor4> contenedores;
    long totalTornaroundTime;
    long totalResponseTime;
    long tornaroundTimeP;
    long responseTimeP;
    String resultadoE;
    DockerClient client;
    DefaultDockerClientConfig clientConfig;
    int quantum;
    int tiempoActual;

    public RoundRobin1(List<Contenedor4> contenedores, int quantum) {
        this.contenedorQueue = new LinkedList<>();

        this.numContenedores = contenedores.size();
        this.contenedores = contenedores;
        this.tiempoActual = 0;
        for (Contenedor4 cont : contenedores) {
            contenedorQueue.offer(cont);

        }
        this.quantum = quantum;

        clientConfig = DefaultDockerClientConfig.createDefaultConfigBuilder().withDockerHost("unix:///var/run/docker.sock").build();
        this.client = DockerClientBuilder.getInstance(clientConfig).build();

    }

    public void ejecutarContenedor(Contenedor4 cont) throws InterruptedException {

        if (cont.isBandera()) {
            if (cont.getTiempoLlegada() == 0) {
                tiempo0 = System.nanoTime();

            }
            
            cont.setTiempoInicio(tiempoActual);
            cont.setBandera(false);
            client.startContainerCmd(cont.getContainer().getId()).exec();
           
            if(cont.getTiempoRestante()<=quantum){
            Thread.sleep(cont.getTiempoRestante()*1000);
        
            tiempoActual += cont.getTiempoEstimadoIngresado();
            cont.setTiempoRestante((int)(cont.getTiempoRestante() - cont.getTiempoEstimadoIngresado()));
                System.out.println("contenedor "+cont.getComando()+",tiempo restante: "+cont.getTiempoRestante()+" y estoy en bandera");
            
            }else{
            Thread.sleep(2000);
            tiempoActual += quantum;
            cont.setTiempoRestante((int)(cont.getTiempoRestante() - quantum));
                System.out.println("contenedor "+cont.getComando()+",Tiempo restante: "+cont.getTiempoRestante()+" y estoy en bandera");
            
            
            
            }
            
            
        } else {
            if(cont.getTiempoRestante()<=quantum){
            client.unpauseContainerCmd(cont.getContainer().getId()).exec();
            Thread.sleep(cont.getTiempoRestante()*1000);
            
            tiempoActual+=cont.getTiempoRestante();
            cont.setTiempoRestante(0);
            System.out.println("contenedor "+cont.getComando()+", Tiempo Restate: "+cont.getTiempoRestante()+ "y estoy despausado");
            
            
            
            }else{
            client.unpauseContainerCmd(cont.getContainer().getId()).exec();
            Thread.sleep(2000);
            
            cont.setTiempoRestante(cont.getTiempoRestante() - quantum);
            System.out.println("contenedor "+cont.getComando()+",Tiempo Restante: "+cont.getTiempoRestante()+" y estoy despausado" +" y esoty corriendo :"+ client.inspectContainerCmd(cont.getContainer().getId()).exec().getState().getRunning());
            tiempoActual+=quantum;
           
            
            }
            
        }

    }
    
    

    
    
    public void pausarContenedor(Contenedor3 cont) {

        client.pauseContainerCmd(cont.getContainer().getId()).exec();
   
    }

    public List<Contenedor4> getContenedores() {
        return contenedores;
    }

    public void setContenedores(List<Contenedor4> contenedores) {
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

    public Queue<Contenedor4> getContenedorQueue() {
        return contenedorQueue;
    }

    public void setContenedorQueue(Queue<Contenedor4> contenedorQueue) {
        this.contenedorQueue = contenedorQueue;
    }

    public void cerrarConexion(Contenedor4 cont) {

        try {
            cont.getClient().close();
        } catch (IOException ex) {
            Logger.getLogger(RoundRobin1.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void ejecutarContenedores() throws InterruptedException {
        totalTornaroundTime = 0;

        totalResponseTime = 0;

        long count = 0;
        resultadoE = "";
        while (!contenedorQueue.isEmpty()) {
            Contenedor4 contenedor = contenedorQueue.poll();
            resultadoE += "Ejecutando contenedor: " + contenedor.getComando() + "\n\n";

            try {
                ejecutarContenedor(contenedor);
            } catch (InterruptedException ex) {
                Logger.getLogger(RoundRobin1.class.getName()).log(Level.SEVERE, null, ex);
            }

           
            if (contenedor.getTiempoRestante() <=0) {
                System.out.println("soy contenedor: "+contenedor.getComando()+", tiempo restante: "+contenedor.getTiempoRestante()+" y estoy en el final");
                
                
                if(client.inspectContainerCmd(contenedor.getContainer().getId()).exec().getState().getRunning()){
                client.stopContainerCmd(contenedor.getContainer().getId()).exec();
                }
                
                client.removeContainerCmd(contenedor.getContainer().getId()).exec();
                contenedor.setTiempoFinal(tiempoActual);
           
                contenedor.setTiempoEstimadoReal((int) (contenedor.getTiempoFinal() - contenedor.getTiempoInicio()));
                contenedor.setTornaroundTime((int) (contenedor.getTiempoFinal() - contenedor.getTiempoLlegada()));
                contenedor.setResponseTime((int) (contenedor.getTiempoInicio() - contenedor.getTiempoLlegada()));
                
                
                
                contenedor.setTornaroundTime((int) (contenedor.getTiempoFinal() - contenedor.getTiempoLlegada()));
                contenedor.setResponseTime((int) (contenedor.getTiempoInicio() - contenedor.getTiempoLlegada()));
                totalTornaroundTime += contenedor.getTornaroundTime();
                totalResponseTime += contenedor.getResponseTime();
               
                cerrarConexion(contenedor);

                count++;

            } else if(client.inspectContainerCmd(contenedor.getContainer().getId()).exec().getState().getRunning()) {
                
               
                
                client.pauseContainerCmd(contenedor.getContainer().getId()).exec();
                 System.out.println("soy contenedor: "+contenedor.getComando() + " y esto en pausa");
                
                contenedorQueue.offer(contenedor);
                
                
               
            } 

        }

        try {
            client.close();
        } catch (IOException ex) {
            Logger.getLogger(RoundRobin1.class.getName()).log(Level.SEVERE, null, ex);
        }

        for (Contenedor4 cont : contenedores) {
            
            resultadoE+="Resultados: "+cont.getNombreI()+"\n";
            

            resultadoE += "Tiempo llegada: " + cont.getTiempoLlegada() + "\n";

            resultadoE += "Tiempo inicial: " + cont.getTiempoInicio() + "\n";

            resultadoE += "Tiempo final: " + cont.getTiempoFinal() + "\n";

            resultadoE += "Tiempo real estimado: " + cont.getTiempoEstimadoReal() + "\n";

            resultadoE += "Tiempo estimado Ingresado:" + cont.getTiempoEstimadoIngresado() + "\n";

            resultadoE += "TornaroundTime: " + cont.getTornaroundTime() + "\n";

            resultadoE += "ResponseTime: " + cont.getResponseTime() + "\n\n";

            totalResponseTime += cont.getResponseTime();
            totalTornaroundTime += cont.getTornaroundTime();

        }

        tornaroundTimeP = totalTornaroundTime / count;
        responseTimeP = totalResponseTime / count;
        resultadoE += "TornaroundTimePromedio: " + tornaroundTimeP + "\n";
        resultadoE += "ResponseTimePromedio: " + responseTimeP + "\n\n";

    }

    public static void main(String[] args) {

       
        
        Contenedor4 cont1 = new Contenedor4(1, "timed_sleep", "sleep 2", 0, 2);
        Contenedor4 cont2 = new Contenedor4(2, "timed_sleep", "sleep 3", 1, 3);
        Contenedor4 cont3 = new Contenedor4(3, "timed_sleep", "sleep 4", 2, 4);
        
        /*Contenedor4 cont1 = new Contenedor4(1, "timed_sleep", "sleep 1", 0, 1);
        Contenedor4 cont2 = new Contenedor4(2, "timed_sleep", "sleep 4", 1, 4);
        Contenedor4 cont3 = new Contenedor4(3, "timed_sleep", "sleep 6", 2, 6);
        Contenedor4 cont4 = new Contenedor4(3, "timed_sleep", "sleep 2", 3, 2);*/
        
        
        
        
        

        List<Contenedor4> conts = new ArrayList<>();
        conts.add(cont1);
        conts.add(cont2);
        conts.add(cont3);
        //conts.add(cont4);

        RoundRobin1 scheduler = new RoundRobin1(conts, 2);

        try {
            scheduler.ejecutarContenedores();
        } catch (InterruptedException ex) {
            Logger.getLogger(RoundRobin1.class.getName()).log(Level.SEVERE, null, ex);
        }

        System.out.println(scheduler.getResultadoE());
      
    }
}
