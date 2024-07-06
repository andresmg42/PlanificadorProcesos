package Algoritmos;

import cliente_docker.versionesContenedores.Contenedor1;
import cliente_docker.versionesContenedores.DockerContainer;
import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.BuildImageResultCallback;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.command.WaitContainerResultCallback;
import com.github.dockerjava.api.model.BuildResponseItem;
import com.github.dockerjava.api.model.Frame;
import com.github.dockerjava.api.model.HostConfig;
import com.github.dockerjava.api.model.Image;
import com.github.dockerjava.api.model.PruneType;
import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.DockerClientBuilder;
import com.github.dockerjava.core.command.LogContainerResultCallback;
import java.io.File;
import java.io.IOException;

import java.util.PriorityQueue;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SPN {
    
    private List<Contenedor1> containers;
    private List<Contenedor1> contenedoresCompletos;
    private PriorityQueue<Contenedor1> readyQueue;
    private long currentTime;
    private DockerClient dockerClient;
    private String resultado;
    private long tornaroundTimeP;
    private long responseTimeP;
    private long totalResponseTime;
    private long totalTurnaroundTime;
    DefaultDockerClientConfig clientConfig;
    
    public SPN(List<Contenedor1> conts) {
        containers = new ArrayList<>();
        contenedoresCompletos = new ArrayList<>();
        readyQueue = new PriorityQueue<>();
        currentTime = 0;
        this.containers = conts;
        dockerClient = DockerClientBuilder.getInstance().build();
        clientConfig = DefaultDockerClientConfig.createDefaultConfigBuilder().withDockerHost("unix:///var/run/docker.sock").build();
        resultado = "\n\n---------------------estos son los resultados---------------------\n\n";
        configurarComparador();
    }
    
    public void addContainer(int contenedorId, String nombreI, String comando, long tiempoLlegada, long tiempoEstimadoIngresado) {
        containers.add(new Contenedor1(contenedorId, nombreI, comando, tiempoLlegada, tiempoEstimadoIngresado));
    }
    
    public void configurarComparador() {
        for (Contenedor1 cont : containers) {
            cont.setComparador("SPN");
        }
    }
    
    public String crearImagen(Contenedor1 cont) {
        DockerClient DockerClient = DockerClientBuilder.getInstance(clientConfig).build();
        try {
            File dockerfile = new File("src/main/java/dockerfiles/Dockerfile");
            File baseDir = dockerfile.getParentFile();

            // Build image
            String imageId = dockerClient.buildImageCmd(baseDir)
                    .withDockerfile(dockerfile)
                    .withBuildArg("VECTOR", cont.getComando())
                    .withTag(cont.getNombreI())
                    .exec(new BuildImageResultCallback() {
                        @Override
                        public void onNext(BuildResponseItem item) {
                            System.out.println("" + item.getStream());
                            super.onNext(item);
                        }
                    }).awaitImageId();
            
            System.out.println("Image created with ID: " + imageId);
            
            return imageId;
            
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
        
    }
    
    public Image verificarImagen(Contenedor1 cont) {
        DockerClient client = DockerClientBuilder.getInstance(clientConfig).build();
        List<Image> images = client.listImagesCmd().exec();
        if (images.isEmpty()) {
            return null;
        } else {
            Image image = new Image();
            
            for (Image im : images) {
                if (im.getRepoTags() != null) {
                    for (String tag : im.getRepoTags()) {
                        if (tag.contains(cont.getNombreI())) {
                            image = im;
                        }
                    }
                }
            }
            return image;
            
        }
        
    }
    
    public String ejecutarContenedor(Contenedor1 cont) {
        
        if (verificarImagen(cont).getId() == null) {
            
            crearImagen(cont);
            
        }
        
        String resultado = crearContenedor(cont);
        
        return resultado;
        
    }
    
    public String crearContenedor(Contenedor1 cont) {
        final StringBuilder retorno = new StringBuilder();
        DockerClient client = DockerClientBuilder.getInstance(clientConfig).build();
        Image image = verificarImagen(cont);
        CreateContainerResponse container = client.createContainerCmd(image.getId()).withHostConfig(HostConfig.newHostConfig().withAutoRemove(true))
                .exec();
        cont.setContainerId(container.getId());
        
        client.startContainerCmd(container.getId()).exec();
        
        return retorno.toString();
    }
    
    private void startContainer(Contenedor1 container) {
        if (container.getTiempoEstimadoIngresado() == container.getTiempoRestante()) {
            container.setTiempoInicio(currentTime);
            container.setResponseTime(currentTime - container.getTiempoLlegada());
        }
        
        Contenedor1 cont = new Contenedor1(container.getContenedor_id(), container.getNombreI(), container.getComando(), container.getTiempoLlegada(), container.getTiempoRestante());
        
        ejecutarContenedor(cont);
        container.setContainerId(cont.getContainerId());
        System.out.println("Tiempo " + currentTime + ": Iniciando contenedor " + container.getNombreI());
    }
    
    private void stopContainer(Contenedor1 container) {
        dockerClient.stopContainerCmd(container.getContainerId()).exec();
        System.out.println("Tiempo " + currentTime + ": Deteniendo contenedor " + container.getNombreI());
    }
    
    public String runScheduler() {
        //containers.sort((a, b) -> Long.compare(a.getTiempoLlegada(), b.getTiempoLlegada()));

        int completedContainers = 0;
        Contenedor1 currentContainer = null;
        
        while (completedContainers < containers.size() || !readyQueue.isEmpty() || currentContainer != null) {
            // Añadir contenedores recién llegados a la cola de listos
            while (!containers.isEmpty() && containers.get(0).getTiempoLlegada() <= currentTime) {
                readyQueue.offer(containers.remove(0));
            }

            
            // Si no hay contenedor en ejecución, toma uno de la cola de listos
            if (currentContainer == null && !readyQueue.isEmpty()) {
                currentContainer = readyQueue.poll();
                startContainer(currentContainer);
            }

            // Simular la ejecución por una unidad de tiempo
            if (currentContainer != null) {
                currentContainer.setTiempoRestante(currentContainer.getTiempoRestante() - 1);
                currentTime++;

                // Si el contenedor ha terminado
                if (currentContainer.getTiempoRestante() == 0) {
                    currentContainer.setTiempoFinal(currentTime);
                    currentContainer.setTornaroundTime(this.currentTime - currentContainer.getTiempoLlegada());
                    System.out.println("Tiempo " + this.currentTime + ": Contenedor " + currentContainer.getNombreI() + " completado");
                    totalTurnaroundTime += currentContainer.getTornaroundTime();
                    totalResponseTime += currentContainer.getResponseTime();
                    contenedoresCompletos.add(currentContainer);
                    completedContainers++;
                    currentContainer = null;
                }
            } else {
                // Si no hay contenedores listos, avanza el tiempo
                currentTime++;
            }
        }
        
        for (Contenedor1 cont : this.contenedoresCompletos) {
            resultado += "nombre contenedor: " + cont.getNombreI() + "\n\n";
            resultado += "tiempo llegada: " + cont.getTiempoLlegada() + "\n\n";
            resultado += "tiempo inicio: " + cont.getTiempoInicio() + "\n\n";
            resultado += "tiempo final: " + cont.getTiempoFinal() + "\n\n";
            resultado += "turnaroundtime: " + cont.getTornaroundTime() + "\n\n";
            resultado += "responseTime: " + cont.getResponseTime() + "\n\n";
            
        }
        
        this.tornaroundTimeP = this.totalTurnaroundTime / this.contenedoresCompletos.size();
        this.responseTimeP = this.totalResponseTime / this.contenedoresCompletos.size();
        resultado += "turnaroundTime promedio: " + this.tornaroundTimeP+"\n\n";
        resultado += "respnseTIme promedio: " + this.responseTimeP+"\n\n";
        
        return resultado;
        
    }
    
    public List<Contenedor1> getContenedoresCompletos() {
        return contenedoresCompletos;
    }
    
    public String getResultado() {
        return resultado;
    }
    
    public long getTornaroundTimeP() {
        return tornaroundTimeP;
    }
    
    public long getResponseTimeP() {
        return responseTimeP;
    }
    
    public static void main(String[] args) {
        
        List<Contenedor1> conts = new ArrayList<>();
        
        Contenedor1 cont1 = new Contenedor1(1, "sleep3", "sleep 3", 0, 3);
        Contenedor1 cont2 = new Contenedor1(1, "sleep6", "sleep 6", 2, 6);
        Contenedor1 cont3 = new Contenedor1(1, "sleep4", "sleep 4", 4, 4);
        Contenedor1 cont4 = new Contenedor1(1, "sleep5", "sleep 5", 6, 5);
        Contenedor1 cont5 = new Contenedor1(1, "sleep2", "sleep 2", 8, 2);
        
        conts.add(cont1);
        conts.add(cont2);
        conts.add(cont3);
        conts.add(cont4);
        conts.add(cont5);
        
        SPN snp = new SPN(conts);

        /*srt.addContainer(1, "sleep3","sleep 3", 0, 3);
        srt.addContainer(2, "sleep6","sleep 6", 2, 6);
        srt.addContainer(3, "sleep4","sleep 4", 4, 4);
        srt.addContainer(4, "sleep5","sleep 5", 6, 5);
        srt.addContainer(5, "sleep2","sleep 2", 8, 2);*/
        System.out.println(snp.runScheduler());
        
    }
}
