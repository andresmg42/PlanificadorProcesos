package Algoritmos;

import cliente_docker.versionesContenedores.Contenedor1;
import cliente_docker.versionesContenedores.Contenedor3;
import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.BuildImageResultCallback;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.model.BuildResponseItem;
import com.github.dockerjava.api.model.HostConfig;
import com.github.dockerjava.api.model.Image;

import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.DockerClientBuilder;
import java.io.File;
import java.io.IOException;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RoundRobinCorregido {

    private Queue<Contenedor1> contenedorQueue;
    long tiempo0;
    int numContenedores;
    List<Contenedor1> contenedores;
    long totalTornaroundTime;
    long totalResponseTime;
    long tornaroundTimeP;
    long responseTimeP;
    String resultadoE;
    DockerClient client;
    DefaultDockerClientConfig clientConfig;
    int quantum;
    int tiempoActual;

    public RoundRobinCorregido(List<Contenedor1> contenedores, int quantum) {
        this.contenedorQueue = new LinkedList<>();

        this.numContenedores = contenedores.size();
        this.contenedores = contenedores;
        this.tiempoActual = 0;
        for (Contenedor1 cont : contenedores) {
            contenedorQueue.offer(cont);

        }
        this.quantum = quantum;

        clientConfig = DefaultDockerClientConfig.createDefaultConfigBuilder().withDockerHost("unix:///var/run/docker.sock").build();
        this.client = DockerClientBuilder.getInstance(clientConfig).build();

    }


            
            
       
    
    public String crearImagen(Contenedor1 cont) {
        
        try {
            File dockerfile = new File("src/main/java/dockerfiles/Dockerfile");
            File baseDir = dockerfile.getParentFile();

            // Build image
            String imageId = client.buildImageCmd(baseDir)
                    .withDockerfile(dockerfile)
                    .withBuildArg("VECTOR",cont.getComando())
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
            container.setTiempoInicio(tiempoActual);
            container.setResponseTime(tiempoActual - container.getTiempoLlegada());
        }
       
        Contenedor1 cont=new Contenedor1(container.getContenedor_id(),container.getNombreI(),container.getComando(),container.getTiempoLlegada(),container.getTiempoRestante());
        
        ejecutarContenedor(cont);
        container.setContainerId(cont.getContainerId());
        System.out.println("Tiempo " + tiempoActual+ ": Iniciando contenedor " + container.getNombreI());
    }

    private void stopContainer(Contenedor1 container) {
        client.stopContainerCmd(container.getContainerId()).exec();
        System.out.println("Tiempo " + tiempoActual + ": Deteniendo contenedor " + container.getNombreI());
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

    /*public void cerrarConexion(Contenedor1 cont) {

        try {
            cont.getClient().close();
        } catch (IOException ex) {
            Logger.getLogger(RoundRobinCorregido.class.getName()).log(Level.SEVERE, null, ex);
        }
    }*/

    public void ejecutarContenedores() throws InterruptedException {
        totalTornaroundTime = 0;

        totalResponseTime = 0;
        Contenedor1 currentContainer=null;
        long count = 0;
        resultadoE = "";
        while (!contenedorQueue.isEmpty()) {
            Contenedor1 contenedor = contenedorQueue.poll();
            resultadoE += "Ejecutando contenedor: " + contenedor.getNombreI() + "\n\n";
            
            //si hay un contenedor en ejecucion comprueba si debe ser preemptado
            if(currentContainer!=null && !contenedorQueue.isEmpty() && currentContainer.getTiempoRestante()>quantum){
            stopContainer(currentContainer);
            contenedorQueue.offer(currentContainer);
            currentContainer=null;
            }
            //si no hay contenedor en eejecucion, toma uno de la cola de listos
            if(currentContainer==null && !contenedorQueue.isEmpty()){
            currentContainer=contenedorQueue.poll();
            startContainer(currentContainer);
            }
            
            //Simular la ejecucion por un quantum de tiempo
            if(currentContainer!=null){
            if(currentContainer.getTiempoRestante()<quantum){
            currentContainer.setTiempoRestante(0);
            tiempoActual+=currentContainer.getTiempoEstimadoIngresado();
            }else{
            currentContainer.setTiempoRestante(currentContainer.getTiempoRestante()-quantum);
            tiempoActual+=quantum;
            }
            
            
            }
            
           
            if (currentContainer.getTiempoRestante() ==0) {
                
                stopContainer(currentContainer);
                //client.removeContainerCmd(currentContainer.getContainerId()).exec();
                contenedor.setTiempoFinal(tiempoActual);
           
                //currentContainer.setTiempoEstimadoReal((int) (contenedor.getTiempoFinal() - contenedor.getTiempoInicio()));
                currentContainer.setTornaroundTime((int) (contenedor.getTiempoFinal() - contenedor.getTiempoLlegada()));
                
                
                
        
                totalTornaroundTime += contenedor.getTornaroundTime();
                totalResponseTime += contenedor.getResponseTime();
               
                //cerrarConexion(contenedor);

                count++;

            } 

        }

        try {
            client.close();
        } catch (IOException ex) {
            Logger.getLogger(RoundRobinCorregido.class.getName()).log(Level.SEVERE, null, ex);
        }

        for (Contenedor1 cont : contenedores) {
            
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

       
        
        Contenedor1 cont1 = new Contenedor1(1, "sleep2", "sleep 2", 0, 2);
        Contenedor1 cont2 = new Contenedor1(2, "sleep3", "sleep 3", 1, 3);
        Contenedor1 cont3 = new Contenedor1(3, "sleep4", "sleep 4", 2, 4);
        
        /*Contenedor3 cont1 = new Contenedor3(1, "sleep1", "sleep 1", 0, 1);
        Contenedor3 cont2 = new Contenedor3(2, "sleep4", "sleep 4", 1, 4);
        Contenedor3 cont3 = new Contenedor3(3, "sleep6", "sleep 6", 2, 6);
        Contenedor3 cont4 = new Contenedor3(3, "sleep2", "sleep 2", 3, 2);*/
        
        
        
        
        

        List<Contenedor1> conts = new ArrayList<>();
        conts.add(cont1);
        conts.add(cont2);
        conts.add(cont3);
        //conts.add(cont4);

        RoundRobinCorregido scheduler = new RoundRobinCorregido(conts, 2);

        try {
            scheduler.ejecutarContenedores();
        } catch (InterruptedException ex) {
            Logger.getLogger(RoundRobinCorregido.class.getName()).log(Level.SEVERE, null, ex);
        }

        System.out.println(scheduler.getResultadoE());
      
    }
}
