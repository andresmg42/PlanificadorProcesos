package Algoritmos;



import cliente_docker.versionesContenedores.DockerContainer;
import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.model.HostConfig;
import com.github.dockerjava.core.DockerClientBuilder;

import java.util.PriorityQueue;
import java.util.ArrayList;
import java.util.List;


public class SRTNS {
    private List<DockerContainer> containers;
    private List<DockerContainer> contenedoresCompletos;
    private PriorityQueue<DockerContainer> readyQueue;
    private int currentTime;
    private DockerClient dockerClient;
    private String resultado;
    private int tornaroundTimeP;
    private int responseTimeP;
    private int totalResponseTime;
    private int totalTurnaroundTime;

    public SRTNS() {
        containers = new ArrayList<>();
        contenedoresCompletos=new ArrayList<>();
        readyQueue = new PriorityQueue<>();
        currentTime = 0;
        dockerClient = DockerClientBuilder.getInstance().build();
        resultado="\n\n---------------------estos son los resultados---------------------\n\n";
    }

    public void addContainer(int contenedorId,String nombreI, int tiempoLlegada, int tiempoEstimadoIngresado) {
        containers.add(new DockerContainer(contenedorId,nombreI,tiempoLlegada,tiempoEstimadoIngresado));
    }   

    private void startContainer(DockerContainer container) {
        if(container.getTiempoEstimadoIngresado()==container.getTiempoRestante()){
        container.setTiempoInicio(currentTime);
        container.setResponseTime(currentTime-container.getTiempoLlegada());
        }
        CreateContainerResponse containerResponse = dockerClient.createContainerCmd("ubuntu")
            .withCmd("sleep", String.valueOf(container.getTiempoRestante()))
            .withHostConfig(HostConfig.newHostConfig().withAutoRemove(true))
            .exec();
        
        container.setContainer(containerResponse);
        dockerClient.startContainerCmd(container.getContainer().getId()).exec();
        System.out.println("Tiempo " + currentTime + ": Iniciando contenedor " + container.getNombreI());
    }

    private void stopContainer(DockerContainer container) {
        dockerClient.stopContainerCmd(container.getContainer().getId()).exec();
        System.out.println("Tiempo " + currentTime + ": Deteniendo contenedor " + container.getNombreI());
    }
    
    

    public String runScheduler() {
        containers.sort((a, b) -> Long.compare(a.getTiempoLlegada(), b.getTiempoLlegada()));
        
        int completedContainers = 0;
        DockerContainer currentContainer = null;

        while (completedContainers < containers.size() || !readyQueue.isEmpty() || currentContainer != null) {
            // Añadir contenedores recién llegados a la cola de listos
            while (!containers.isEmpty() && containers.get(0).getTiempoLlegada() <= currentTime) {
                readyQueue.offer(containers.remove(0));
            }

            // Si hay un contenedor en ejecución, comprueba si debe ser preemptado
            if (currentContainer != null && !readyQueue.isEmpty() && 
                readyQueue.peek().getTiempoRestante() < currentContainer.getTiempoRestante()) {
                stopContainer(currentContainer);
                readyQueue.offer(currentContainer);
                currentContainer = null;
            }

            // Si no hay contenedor en ejecución, toma uno de la cola de listos
            if (currentContainer == null && !readyQueue.isEmpty()) {
                currentContainer = readyQueue.poll();
                startContainer(currentContainer);
            }

            // Simular la ejecución por una unidad de tiempo
            if (currentContainer != null) {
                currentContainer.setTiempoRestante(currentContainer.getTiempoRestante()-1);
                currentTime++;

                // Si el contenedor ha terminado
                if (currentContainer.getTiempoRestante() == 0) {
                    currentContainer.setTiempoFinal(currentTime);
                    currentContainer.setTurnaroudTime(currentTime-currentContainer.getTiempoLlegada());
                    System.out.println("Tiempo " + currentTime + ": Contenedor " + currentContainer.getNombreI() + " completado");
                    totalTurnaroundTime+=currentContainer.getTurnaroudTime();
                    totalResponseTime+=currentContainer.getResponseTime();
                    contenedoresCompletos.add(currentContainer);
                    completedContainers++;
                    currentContainer = null;
                }
            } else {
                // Si no hay contenedores listos, avanza el tiempo
                currentTime++;
            }
        }
        
        for(DockerContainer cont:this.contenedoresCompletos){
            resultado+="nombre contenedor: "+cont.getNombreI()+"\n\n";
            resultado+="tiempo llegada: "+cont.getTiempoLlegada()+"\n\n";
            resultado+="tiempo inicio: "+cont.getTiempoInicio()+"\n\n";
            resultado+="tiempo final: "+cont.getTiempoFinal()+"\n\n";
            resultado+="turnaroundtime: "+cont.getTurnaroudTime()+"\n\n";
            resultado+="responseTime: "+cont.getResponseTime()+"\n\n";
            
        }
        
        this.tornaroundTimeP=this.totalTurnaroundTime/this.contenedoresCompletos.size();
        this.responseTimeP=this.totalResponseTime/this.contenedoresCompletos.size();
        resultado+="turnaroundTime promedio: "+this.tornaroundTimeP;
        resultado+="respnseTIme promedio: "+this.responseTimeP;
        
        return resultado;
        
        
        
    }

    public List<DockerContainer> getContenedoresCompletos() {
        return contenedoresCompletos;
    }

    public String getResultado() {
        return resultado;
    }

    public int getTornaroundTimeP() {
        return tornaroundTimeP;
    }

    public int getResponseTimeP() {
        return responseTimeP;
    }
    
    
    
    public static void main(String[] args) {
       SRTNS srt = new SRTNS();
       
       srt.addContainer(1,"sleep3", 0, 3);
       srt.addContainer(2,"sleep6", 2, 6);
       srt.addContainer(3,"sleep4", 4, 4);
       srt.addContainer(4,"sleep5", 6, 5);
       srt.addContainer(5,"sleep2", 8, 2);
       
        System.out.println(srt.runScheduler());
       
    }
}
