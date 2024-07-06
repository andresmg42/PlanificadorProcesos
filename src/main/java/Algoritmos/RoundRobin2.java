package Algoritmos;

import cliente_docker.versionesContenedores.Contenedor3;
import com.github.dockerjava.api.DockerClient;

import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.DockerClientBuilder;
import java.io.IOException;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RoundRobin2 {

    private Queue<Contenedor3> contenedorQueue;
    long tiempo0;
    int numContenedores;
    List<Contenedor3> contenedores;
    long totalTornaroundTime;
    long totalResponseTime;
    long tornaroundTimeP;
    long responseTimeP;
    String resultadoE;
    DockerClient client;
    DefaultDockerClientConfig clientConfig;
    int quantum;
    int tiempoActual;

    public RoundRobin2(List<Contenedor3> contenedores, int quantum) {
        this.contenedorQueue = new LinkedList<>();

        this.numContenedores = contenedores.size();
        this.contenedores = contenedores;
        this.tiempoActual = 0;
        for (Contenedor3 cont : contenedores) {
            contenedorQueue.offer(cont);

        }
        this.quantum = quantum;

        clientConfig = DefaultDockerClientConfig.createDefaultConfigBuilder().withDockerHost("unix:///var/run/docker.sock").build();
        this.client = DockerClientBuilder.getInstance(clientConfig).build();

    }

    public void ejecutarContenedor(Contenedor3 cont) throws InterruptedException {

        if (cont.isBandera()) {
            if (cont.getTiempoLlegada() == 0) {
                tiempo0 = System.nanoTime();

            }
            
            cont.setTiempoInicio(tiempoActual);
            client.startContainerCmd(cont.getContainer().getId()).exec();
            cont.setBandera(false);
            if(cont.getTiempoRestante()<=quantum){
            Thread.sleep(cont.getTiempoRestante()*1000);
        
            tiempoActual += cont.getTiempoEstimadoIngresado();
            cont.setTiempoRestante((int)(cont.getTiempoRestante() - cont.getTiempoEstimadoIngresado()));
            
            }else{
            Thread.sleep(2000);
            cont.setBandera(false);
            tiempoActual += quantum;
            cont.setTiempoRestante((int)(cont.getTiempoRestante() - quantum));
            
            
            
            }
            
            
        } else {
            if(cont.getTiempoRestante()<=quantum){
            client.unpauseContainerCmd(cont.getContainer().getId()).exec();
            Thread.sleep(cont.getTiempoRestante()*1000);
            tiempoActual+=cont.getTiempoRestante();
            cont.setTiempoRestante(0);
            
            
            }else{
            client.unpauseContainerCmd(cont.getContainer().getId()).exec();
            Thread.sleep(2000);
            cont.setTiempoRestante(cont.getTiempoRestante() - quantum);
            tiempoActual+=quantum;
           
            
            }
            
        }

    }
    
    

    
    
    public void pausarContenedor(Contenedor3 cont) {

        client.pauseContainerCmd(cont.getContainer().getId()).exec();
   
    }

    public List<Contenedor3> getContenedores() {
        return contenedores;
    }

    public void setContenedores(List<Contenedor3> contenedores) {
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

    public Queue<Contenedor3> getContenedorQueue() {
        return contenedorQueue;
    }

    public void setContenedorQueue(Queue<Contenedor3> contenedorQueue) {
        this.contenedorQueue = contenedorQueue;
    }

    public void cerrarConexion(Contenedor3 cont) {

        try {
            cont.getClient().close();
        } catch (IOException ex) {
            Logger.getLogger(RoundRobin2.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void ejecutarContenedores() throws InterruptedException {
        totalTornaroundTime = 0;

        totalResponseTime = 0;

        long count = 0;
        resultadoE = "";
        while (!contenedorQueue.isEmpty()) {
            Contenedor3 contenedor = contenedorQueue.poll();
            resultadoE += "Ejecutando contenedor: " + contenedor.getNombreI() + "\n\n";

            try {
                ejecutarContenedor(contenedor);
            } catch (InterruptedException ex) {
                Logger.getLogger(RoundRobin2.class.getName()).log(Level.SEVERE, null, ex);
            }

           
            if (contenedor.getTiempoRestante() <=0 || !client.inspectContainerCmd(contenedor.getContainer().getId()).exec().getState().getRunning()) {
                
            
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
                
                contenedorQueue.offer(contenedor);
                
                
               
            } 

        }

        try {
            client.close();
        } catch (IOException ex) {
            Logger.getLogger(RoundRobin2.class.getName()).log(Level.SEVERE, null, ex);
        }

        for (Contenedor3 cont : contenedores) {
            
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

       
        
        Contenedor3 cont1 = new Contenedor3(1, "sleep2", "sleep 2", 0, 2);
        Contenedor3 cont2 = new Contenedor3(2, "sleep3", "sleep 3", 1, 3);
        Contenedor3 cont3 = new Contenedor3(3, "sleep4", "sleep 4", 2, 4);
        
        /*Contenedor3 cont1 = new Contenedor3(1, "sleep1", "sleep 1", 0, 1);
        Contenedor3 cont2 = new Contenedor3(2, "sleep4", "sleep 4", 1, 4);
        Contenedor3 cont3 = new Contenedor3(3, "sleep6", "sleep 6", 2, 6);
        Contenedor3 cont4 = new Contenedor3(3, "sleep2", "sleep 2", 3, 2);*/
        
        
        
        
        

        List<Contenedor3> conts = new ArrayList<>();
        conts.add(cont1);
        conts.add(cont2);
        conts.add(cont3);
        //conts.add(cont4);

        RoundRobin2 scheduler = new RoundRobin2(conts, 2);

        try {
            scheduler.ejecutarContenedores();
        } catch (InterruptedException ex) {
            Logger.getLogger(RoundRobin2.class.getName()).log(Level.SEVERE, null, ex);
        }

        System.out.println(scheduler.getResultadoE());
      
    }
}
