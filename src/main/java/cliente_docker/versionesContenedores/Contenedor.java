/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cliente_docker.versionesContenedores;

/**
 *
 * @author andresuv
 */
import cliente_docker.versionesFifo.FifoScheduler;
import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.BuildImageResultCallback;

import com.github.dockerjava.api.command.CreateContainerResponse;

import com.github.dockerjava.api.model.BuildResponseItem;

import com.github.dockerjava.api.command.WaitContainerResultCallback;

import com.github.dockerjava.api.model.Frame;
import com.github.dockerjava.core.command.LogContainerResultCallback;
import com.github.dockerjava.api.model.Image;
import com.github.dockerjava.api.model.PruneType;
import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.DockerClientBuilder;
import java.io.File;
import java.io.IOException;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import cliente_docker.versionesFifo.FifoScheduler.*;


public class Contenedor {

    DefaultDockerClientConfig clientConfig;
    String nombreI;
    String comando;
    long tiempoLlegada;
    long tiempoInicio;
    long tiempoEstimadoReal;
    long tiempoEstimadoIngresado;
    long tiempoFinal;
    String algoritmo;
    FifoScheduler fifo;
    

    public Contenedor(String nombreI, String comando,long tiempoLlegada,long tiempoEstimadoIngresado,FifoScheduler fifo) {
        this.nombreI = nombreI;
        this.comando = comando;
        this.tiempoLlegada=tiempoLlegada;
        this.tiempoEstimadoIngresado=tiempoEstimadoIngresado;
        this.fifo=fifo;
       
        
        clientConfig = DefaultDockerClientConfig.createDefaultConfigBuilder().withDockerHost("unix:///var/run/docker.sock").build();

    }

    public String crearImagen() {
        DockerClient client = DockerClientBuilder.getInstance(clientConfig).build();
        try {
            File dockerfile = new File("src/main/java/dockerfiles/Dockerfile");
            File baseDir = dockerfile.getParentFile();

            // Build image
            String imageId = client.buildImageCmd(baseDir)
                    .withDockerfile(dockerfile)
                    .withBuildArg("VECTOR", comando)
                    .withTag(nombreI)
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
        } finally {
            try {
                client.pruneCmd(PruneType.IMAGES).withLabelFilter("<none>").exec();
                client.close();
            } catch (IOException ex) {
                Logger.getLogger(Contenedor.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return "-1";

    }

    public String crearContenedor() {
        final StringBuilder retorno = new StringBuilder();
        DockerClient client = DockerClientBuilder.getInstance(clientConfig).build();
        Image image = verificarImagen();

        CreateContainerResponse container = client.createContainerCmd(image.getId()).exec();
        this.tiempoInicio=System.currentTimeMillis();
        if(this.tiempoLlegada==0){
        fifo.setTiempo0(System.currentTimeMillis());
        }
        client.startContainerCmd(container.getId()).exec();
        LogContainerResultCallback callback = new LogContainerResultCallback() {
            @Override
            public void onNext(Frame item) {

                retorno.append(new String(item.getPayload())).append("\n");

            }
        };
        
        try {
            client.logContainerCmd(container.getId())
                    .withStdOut(true)
                    .withStdErr(true)
                    .exec(callback)
                    .awaitCompletion();
            client.waitContainerCmd(container.getId()).exec(new WaitContainerResultCallback()).awaitCompletion();
        this.tiempoFinal=System.currentTimeMillis();
        this.tiempoEstimadoReal=this.tiempoFinal-this.tiempoInicio;
            client.removeContainerCmd(container.getId()).exec();
        } catch (InterruptedException ex) {
            Logger.getLogger(Contenedor.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                client.close();
            } catch (IOException ex) {
                Logger.getLogger(Contenedor.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return retorno.toString();
    }

    public String ejecutarContenedor() {
        
        
        if (verificarImagen().getId() == null) {

            crearImagen();

        }
        
        String resultado=crearContenedor();
        
        return resultado;
        
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

    public String getAlgoritmo() {
        return algoritmo;
    }

    public void setAlgoritmo(String algoritmo) {
        this.algoritmo = algoritmo;
    }
    
    
    
    

    public Image verificarImagen() {
        DockerClient client = DockerClientBuilder.getInstance(clientConfig).build();
        List<Image> images = client.listImagesCmd().exec();
        if (images.isEmpty()) {
            return null;
        } else {
            Image image = new Image();

            for (Image im : images) {
                if (im.getRepoTags() != null) {
                    for (String tag : im.getRepoTags()) {
                        if (tag.contains(nombreI)) {
                            image = im;
                        }
                    }
                }
            }
            return image;

        }
        

    }
//quitar sheduler y 
    public static void main(String[] args) {
        //Contenedor cont = new Contenedor("sleep1", "sleep 1",0,1400);

        //System.out.println(cont.ejecutarContenedor());

        //System.out.println(cont.verificarImagen());

        /*if(cont.verificarImagen().getId()==null){
            System.out.println(cont.crearImagen());
        }
       
        System.out.println(cont.crearContenedor());*/
        
       // System.out.println(cont.ejecutarContenedor());
        //System.out.println(cont.getTiempoEstimadoReal());
        
    }

}
