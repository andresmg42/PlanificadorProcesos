package cliente_docker.versionesContenedores;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.BuildImageResultCallback;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.command.ExecCreateCmdResponse;
import com.github.dockerjava.api.command.InspectContainerResponse;
import com.github.dockerjava.api.command.WaitContainerResultCallback;
import com.github.dockerjava.api.model.BuildResponseItem;
import com.github.dockerjava.api.model.Image;
import com.github.dockerjava.api.model.PruneType;
import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.DockerClientBuilder;
import com.github.dockerjava.core.command.ExecStartResultCallback;
import com.github.dockerjava.core.command.LogContainerResultCallback;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author andresuv
 */
public class Contenedor3 {

    String nombreI;
    String comando;
    int contenedor_id;
    int tiempoLlegada;
    int tiempoInicio;
    int tiempoEstimadoReal;
    int tiempoEstimadoIngresado;
    int tiempoFinal;
    int tornaroundTime;
    int responseTime;
    int tiempoRestante;
    boolean bandera;
    DefaultDockerClientConfig clientConfig;
    DockerClient client;
    CreateContainerResponse container;

    public Contenedor3(int contenedor_id, String nombreI, String comando, int tiempoLlegada, int tiempoEstimadoIngresado) {

        this.nombreI = nombreI;
        this.comando = comando;
        this.tiempoLlegada = tiempoLlegada;
        this.tiempoEstimadoIngresado = tiempoEstimadoIngresado;
        this.contenedor_id = contenedor_id;
        clientConfig = DefaultDockerClientConfig.createDefaultConfigBuilder().withDockerHost("unix:///var/run/docker.sock").build();
        this.client = DockerClientBuilder.getInstance(clientConfig).build();
        this.bandera = true;
        ejecutarCrearContenedor();

    }

    public CreateContainerResponse crearContenedor() {

        Image image = verificarImagen();
        container = client.createContainerCmd(image.getId()).exec();
        this.tiempoRestante = this.tiempoEstimadoIngresado;
        return container;

    }

    public String crearImagen() {

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
        } /*finally {
            try {
                client.pruneCmd(PruneType.IMAGES).withLabelFilter("<none>").exec();
                client.close();
            } catch (IOException ex) {
                Logger.getLogger(Contenedor3.class.getName()).log(Level.SEVERE, null, ex);
            }
        }*/
        return null;

    }

    public Image verificarImagen() {

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

    public void ejecutarCrearContenedor() {

        if (verificarImagen().getId() == null) {

            crearImagen();

        }

        this.container = crearContenedor();

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

    public void setTiempoInicio(int tiempoInicio) {
        this.tiempoInicio = tiempoInicio;
    }

    public long getTiempoEstimadoReal() {
        return tiempoEstimadoReal;
    }

    public void setTiempoEstimadoReal(int tiempoEstimado) {
        this.tiempoEstimadoReal = tiempoEstimado;
    }

    public long getTiempoFinal() {
        return tiempoFinal;
    }

    public void setTiempoFinal(int tiempoFinal) {
        this.tiempoFinal = tiempoFinal;
    }

    public long getTiempoLlegada() {
        return tiempoLlegada;
    }

    public void setTiempoLlegada(int tiempoInicio2) {
        this.tiempoLlegada = tiempoInicio2;
    }

    public long getTiempoEstimadoIngresado() {
        return tiempoEstimadoIngresado;
    }

    public void setTiempoEstimadoIngresado(int tiempoEstimadoPromedio) {
        this.tiempoEstimadoIngresado = tiempoEstimadoPromedio;
    }

    public long getTornaroundTime() {
        return tornaroundTime;
    }

    public void setTornaroundTime(int tornaroundTime) {
        this.tornaroundTime = tornaroundTime;
    }

    public long getResponseTime() {
        return responseTime;
    }

    public void setResponseTime(int responseTime) {
        this.responseTime = responseTime;
    }

    public int getContenedor_id() {
        return contenedor_id;
    }

    public void setContenedor_id(int contenedor_id) {
        this.contenedor_id = contenedor_id;
    }

    public DockerClient getClient() {
        return client;
    }

    public void setClient(DockerClient client) {
        this.client = client;
    }

    public CreateContainerResponse getContainer() {
        return container;
    }

    public void setContainer(CreateContainerResponse container) {
        this.container = container;
    }

    public int getTiempoRestante() {
        return tiempoRestante;
    }

    public void setTiempoRestante(int tiempoRestante) {
        this.tiempoRestante = tiempoRestante;
    }

    public boolean isBandera() {
        return bandera;
    }

    public void setBandera(boolean bandera) {
        this.bandera = bandera;
    }

    private static void sendSignal(DockerClient client, String containerId, String signal) {
        ExecCreateCmdResponse execCreateCmdResponse = client.execCreateCmd(containerId)
                .withCmd("kill", "-" + signal, "1")
                .exec();

        try {
            client.execStartCmd(execCreateCmdResponse.getId())
                    .exec(new ExecStartResultCallback())
                    .awaitCompletion();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    
   private static void printContainerState(DockerClient client, Contenedor3 cont, String message) {
    InspectContainerResponse inspect = client.inspectContainerCmd(cont.getContainer().getId()).exec();
    System.out.println(message + ":");
    System.out.println("  Running: " + inspect.getState().getRunning());
    System.out.println("  Status: " + inspect.getState().getStatus());
    System.out.println("  PID: " + inspect.getState().getPid());
    System.out.println("  Exit Code: " + inspect.getState().getExitCode());
    System.out.println("  Error: " + inspect.getState().getError());
}
      
       private static void printContainerLogs(DockerClient client, Contenedor3 cont) {
        try {
            String logs = client.logContainerCmd(cont.getContainer().getId())
                    .withStdOut(true)
                    .withStdErr(true)
                    .exec(new LogContainerResultCallback())
                    .awaitCompletion()
                    .toString();
            System.out.println("Container logs:");
            System.out.println(logs);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

//quitar sheduler y 
    public static void main(String[] args)  {
        //Contenedor3 cont = new Contenedor3(15, "sleep5", "sleep 5", 0, 5);

        DefaultDockerClientConfig clientConfig = DefaultDockerClientConfig.createDefaultConfigBuilder().withDockerHost("unix:///var/run/docker.sock").build();
        DockerClient client = DockerClientBuilder.getInstance(clientConfig).build();

        Contenedor3 cont = new Contenedor3(15, "sleepcontrol",
        "sh -c 'trap \"echo Paused at $(date); sleep 1\" SIGTSTP; " +
        "trap \"echo Resumed at $(date)\" SIGCONT; " +
        "echo Starting at $(date); " +
        "for i in {1..60}; do echo \"Running: $i at $(date)\"; sleep 1; done; " +
        "echo Done at $(date)'", 0, 60);

    try {
        client.startContainerCmd(cont.getContainer().getId()).exec();
        System.out.println("Container started");

        printContainerState(client, cont, "Initial state");

        Thread.sleep(2000);
        sendSignal(client, cont.getContainer().getId(), "SIGTSTP");
        System.out.println("Sleep paused");

        printContainerState(client, cont, "After pause");

        Thread.sleep(2000);
        System.out.println("Waited for 2 seconds while paused");

        sendSignal(client, cont.getContainer().getId(), "SIGCONT");
        System.out.println("Sleep resumed");

        printContainerState(client, cont, "After resume");

        Thread.sleep(2000);
        sendSignal(client, cont.getContainer().getId(), "SIGTSTP");
        System.out.println("Sleep paused again");

        printContainerState(client, cont, "After second pause");

        // ... resto del código ...

    } catch (Exception ex) {
        System.err.println("Error: " + ex.getMessage());
        ex.printStackTrace();
    } finally {
        try {
            System.out.println("Container logs:");
            System.out.println(client.logContainerCmd(cont.getContainer().getId())
                    .withStdOut(true)
                    .withStdErr(true)
                    .withTail(100)
                    .exec(new LogContainerResultCallback())
                    .awaitCompletion().toString());
            
            client.removeContainerCmd(cont.getContainer().getId()).withForce(true).exec();
            System.out.println("Container removed");
        } catch (Exception e) {
            System.err.println("Error in cleanup: " + e.getMessage());
        }
    }

    }

}
