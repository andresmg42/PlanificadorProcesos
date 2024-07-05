package cliente_docker.versionesContenedores;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.BuildImageResultCallback;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.command.ExecCreateCmdResponse;
import com.github.dockerjava.api.command.WaitContainerResultCallback;
import com.github.dockerjava.api.model.BuildResponseItem;
import com.github.dockerjava.api.model.HostConfig;
import com.github.dockerjava.api.model.Image;
import com.github.dockerjava.api.model.PruneType;
import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.DockerClientBuilder;
import com.github.dockerjava.core.command.ExecStartResultCallback;
import java.io.File;
import java.io.IOException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.stream.Collectors;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 *
 * @author andresuv
 */
public class Contenedor5 implements Comparable<Contenedor4> {

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

    public Contenedor5(int contenedor_id, String nombreI, String comando, int tiempoLlegada, int tiempoEstimadoIngresado) {

        this.nombreI = nombreI;
        this.comando = comando;
        this.tiempoLlegada = tiempoLlegada;
        this.tiempoEstimadoIngresado = tiempoEstimadoIngresado;
        this.contenedor_id = contenedor_id;
        clientConfig = DefaultDockerClientConfig.createDefaultConfigBuilder().withDockerHost("unix:///var/run/docker.sock").build();
        this.client = DockerClientBuilder.getInstance(clientConfig).build();
        this.bandera = true;
        modificarSH(tiempoEstimadoIngresado);
        this.container = crearContenedor();
        this.tiempoRestante = this.tiempoEstimadoIngresado;
        //ejecutarCrearContenedor();

    }

    public CreateContainerResponse crearContenedor() {

        Image image = verificarImagen();
        container = client.createContainerCmd(image.getId()).exec();

        return container;

    }

    public void modificarSH(int sleep) {
        String filePath = "src/main/java/dockerfileR/timed_sleep.sh"; // Ajusta esto a la ruta correcta de tu archivo

        try {
            // Leer todas las líneas del archivo
            List<String> lines = Files.readAllLines(Paths.get(filePath));

            // Modificar la línea que contiene total_time
            List<String> modifiedLines = lines.stream()
                    .map(line -> {
                        if (line.trim().startsWith("total_time=")) {
                            return "total_time=" + sleep;
                        }
                        return line;
                    })
                    .collect(Collectors.toList());

            // Escribir las líneas modificadas de vuelta al archivo
            Files.write(Paths.get(filePath), modifiedLines, StandardOpenOption.TRUNCATE_EXISTING);

            System.out.println("Archivo modificado exitosamente. Nuevo tiempo de espera: " + sleep + " segundos.");
        } catch (IOException e) {
            System.err.println("Error al modificar el archivo: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public String crearImagen() {

        try {
            File dockerfile = new File("src/main/java/dockerfileR/Dockerfile");
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
                Logger.getLogger(Contenedor4.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
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

    public int getTiempoInicio() {
        return tiempoInicio;
    }

    public void setTiempoInicio(int tiempoInicio) {
        this.tiempoInicio = tiempoInicio;
    }

    public int getTiempoEstimadoReal() {
        return tiempoEstimadoReal;
    }

    public void setTiempoEstimadoReal(int tiempoEstimado) {
        this.tiempoEstimadoReal = tiempoEstimado;
    }

    public int getTiempoFinal() {
        return tiempoFinal;
    }

    public void setTiempoFinal(int tiempoFinal) {
        this.tiempoFinal = tiempoFinal;
    }

    public int getTiempoLlegada() {
        return tiempoLlegada;
    }

    public void setTiempoLlegada(int tiempoInicio2) {
        this.tiempoLlegada = tiempoInicio2;
    }

    public int getTiempoEstimadoIngresado() {
        return tiempoEstimadoIngresado;
    }

    public void setTiempoEstimadoIngresado(int tiempoEstimadoPromedio) {
        this.tiempoEstimadoIngresado = tiempoEstimadoPromedio;
    }

    public int getTornaroundTime() {
        return tornaroundTime;
    }

    public void setTornaroundTime(int tornaroundTime) {
        this.tornaroundTime = tornaroundTime;
    }

    public int getResponseTime() {
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
    
    @Override
    public int compareTo(Contenedor4 other) {
        return Long.compare(this.getTiempoRestante(), other.getTiempoRestante());
    }

//quitar sheduler y 
    public static void main(String[] args) {
        Contenedor4 cont = new Contenedor4(1, "timed_sleep", "sleep 6", 0, 6);
        DockerClient client = cont.getClient();
        System.out.println(cont.getNombreI());
        try {
            client.startContainerCmd(cont.getContainer().getId()).exec();
            Thread.sleep(2000);
            client.pauseContainerCmd(cont.getContainer().getId()).exec();
            Thread.sleep(2000);
            client.unpauseContainerCmd(cont.getContainer().getId()).exec();
            Thread.sleep(2000);
            client.pauseContainerCmd(cont.getContainer().getId()).exec();
            Thread.sleep(2000);
            client.unpauseContainerCmd(cont.getContainer().getId()).exec();
            long inicio = System.currentTimeMillis();
            client.waitContainerCmd(cont.getContainer().getId()).exec(new WaitContainerResultCallback()).awaitCompletion();
            long fin = System.currentTimeMillis();
            System.out.println(fin - inicio);

        } catch (InterruptedException ex) {
            Logger.getLogger(Contenedor4.class.getName()).log(Level.SEVERE, null, ex);
        }

        /*try {
            
            
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }*/
    }

   

}
