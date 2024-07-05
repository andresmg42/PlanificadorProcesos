
import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.model.HostConfig;
import com.github.dockerjava.core.DockerClientBuilder;

import java.util.PriorityQueue;
import java.util.ArrayList;
import java.util.List;

class DockerContainer implements Comparable<DockerContainer> {
    String id;
    long arrivalTime;
    long burstTime;
    long remainingTime;
    String containerId;

    public DockerContainer(String id, long arrivalTime, long burstTime) {
        this.id = id;
        this.arrivalTime = arrivalTime;
        this.burstTime = burstTime;
        this.remainingTime = burstTime;
    }

    @Override
    public int compareTo(DockerContainer other) {
        return Long.compare(this.remainingTime, other.remainingTime);
    }
}

public class SRTNScheduler {
    private List<DockerContainer> containers;
    private PriorityQueue<DockerContainer> readyQueue;
    private long currentTime;
    private DockerClient dockerClient;

    public SRTNScheduler() {
        containers = new ArrayList<>();
        readyQueue = new PriorityQueue<>();
        currentTime = 0;
        dockerClient = DockerClientBuilder.getInstance().build();
    }

    public void addContainer(String containerId, long arrivalTime, long burstTime) {
        containers.add(new DockerContainer(containerId, arrivalTime, burstTime));
    }

    private void startContainer(DockerContainer container) {
        CreateContainerResponse containerResponse = dockerClient.createContainerCmd("ubuntu")
            .withCmd("sleep", String.valueOf(container.remainingTime))
            .withHostConfig(HostConfig.newHostConfig().withAutoRemove(true))
            .exec();
        
        container.containerId = containerResponse.getId();
        dockerClient.startContainerCmd(container.containerId).exec();
        System.out.println("Tiempo " + currentTime + ": Iniciando contenedor " + container.id);
    }

    private void stopContainer(DockerContainer container) {
        dockerClient.stopContainerCmd(container.containerId).exec();
        System.out.println("Tiempo " + currentTime + ": Deteniendo contenedor " + container.id);
    }

    public void runScheduler() {
        containers.sort((a, b) -> Long.compare(a.arrivalTime, b.arrivalTime));
        
        int completedContainers = 0;
        DockerContainer currentContainer = null;

        while (completedContainers < containers.size() || !readyQueue.isEmpty() || currentContainer != null) {
            // Añadir contenedores recién llegados a la cola de listos
            while (!containers.isEmpty() && containers.get(0).arrivalTime <= currentTime) {
                readyQueue.offer(containers.remove(0));
            }

            // Si hay un contenedor en ejecución, comprueba si debe ser preemptado
            if (currentContainer != null && !readyQueue.isEmpty() && 
                readyQueue.peek().remainingTime < currentContainer.remainingTime) {
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
                currentContainer.remainingTime--;
                currentTime++;

                // Si el contenedor ha terminado
                if (currentContainer.remainingTime == 0) {
                    System.out.println("Tiempo " + currentTime + ": Contenedor " + currentContainer.id + " completado");
                    completedContainers++;
                    currentContainer = null;
                }
            } else {
                // Si no hay contenedores listos, avanza el tiempo
                currentTime++;
            }
        }
    }
    
    public static void main(String[] args) {
       SRTNScheduler srt = new SRTNScheduler();
       
       srt.addContainer("1", 0, 3);
       srt.addContainer("2", 2, 6);
       srt.addContainer("3", 4, 4);
       srt.addContainer("4", 6, 5);
       srt.addContainer("6", 8, 2);
       
       srt.runScheduler();
    }
}
