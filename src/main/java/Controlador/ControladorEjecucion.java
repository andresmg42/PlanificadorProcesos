/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controlador;

import Algoritmos.FIFO;
import Algoritmos.HRRN;
import Algoritmos.RoundRobin;
import Algoritmos.SPN;
import Algoritmos.SRT;
import Dao.DaoContenedor;
import Dao.DaoEjecucion;
import cliente_docker.versionesContenedores.Contenedor1;
import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.BuildImageResultCallback;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.command.ListContainersCmd;
import com.github.dockerjava.api.model.Bind;
import com.github.dockerjava.api.model.BuildResponseItem;
import com.github.dockerjava.api.model.Container;
import com.github.dockerjava.api.model.HostConfig;
import com.github.dockerjava.api.model.Image;
import com.github.dockerjava.api.model.Volume;
import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.DockerClientBuilder;
import java.io.File;
import static java.nio.file.Files.list;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import logica.Ejecucion;

/**
 *
 * @author andresuv
 */
public class ControladorEjecucion {

    DaoEjecucion daoE;
    DaoContenedor daoC;
    RoundRobin RR;
    FIFO fifo;
    DockerClient client;
    DefaultDockerClientConfig clientConfig;

    public ControladorEjecucion() {
        daoE = new DaoEjecucion();
        daoC = new DaoContenedor();
        clientConfig = DefaultDockerClientConfig.createDefaultConfigBuilder().withDockerHost("unix:///var/run/docker.sock").build();
        this.client = DockerClientBuilder.getInstance(clientConfig).build();
        //ejecutarDB();

    }

    public void inicializarContenedorBaseDeDatos() {

    }

    public String crearImagen() {
        try {
            File baseDir = new File("src/main/java/dockerfileDB/Dockerfile");
            String imageId = client.buildImageCmd(baseDir)
                    .withTags(Set.of("my-postgres-image"))
                    .exec(new BuildImageResultCallback())
                    .awaitImageId();

            return imageId;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Image verificarImagen() {
        List<Image> images = client.listImagesCmd().exec();
        if (images.isEmpty()) {
            return null;
        } else {
            for (Image im : images) {
                if (im.getRepoTags() != null) {
                    for (String tag : im.getRepoTags()) {
                        if (tag.contains("my-postgres-image")) {
                            return im;
                        }
                    }
                }
            }
            return null;
        }
    }

    public String crearContenedor(String imagen_id) {

        CreateContainerResponse container = client.createContainerCmd(imagen_id)
                .withEnv("POSTGRES_DB=mydatabase", "POSTGRES_USER=myuser", "POSTGRES_PASSWORD=mypassword")
                .withName("contenedorProyecto")
                .withHostConfig(
                        HostConfig.newHostConfig()
                                .withBinds(new Bind("postgres-data", new Volume("/var/lib/postgresql/data")))
                                .withAutoRemove(true)
                )
                .exec();

        client.startContainerCmd(container.getId()).exec();
        return container.getId();
    }
    
  /*public String verificarContenedor() {
    List<String> nombres = new ArrayList<>();
    nombres.add("contenedorProyecto");

    // Ejecuta el comando de listar contenedores con el filtro de nombre
    List<Container> list = client.listContainersCmd().withNameFilter(nombres).exec();

    // Verifica si la lista no está vacía
    if (!list.isEmpty()) {
        return list.get(0).getId();
    } else {
        // Retorna un mensaje apropiado o null si no se encontró el contenedor
        return null; // O podrías retornar un mensaje como "Contenedor no encontrado"
    }
}*/
    

    public void ejecutarDB() {
        Image imagen = verificarImagen();
        if (imagen.getId() == null) {
            String imagen_id = crearImagen();
            crearContenedor(imagen_id);
        }
        crearContenedor("my-postgres-image");

    }

    public int insertarEjecucion(int listado_id, String algoritmo, double tornaroundTimeP, double responseTimeP, Date fecha, Time hora) {
        Ejecucion e = new Ejecucion();
        e.setListado_id(listado_id);
        e.setAlgoritmo(algoritmo);
        e.setFecha(fecha);
        e.setTime(hora);
        e.setTornaroundTimeP(tornaroundTimeP);
        e.setResponseTimeP(responseTimeP);
        return daoE.DaoInsertarEjecucion(e);

    }

    public ResultSet consultarResultados(int listado_id) {
        return daoE.consultarResultados(listado_id);

    }

    public ResultSet listarEjecucion() {
        return daoE.DaolistarEjecucion();
    }

    public Object[] fechaHoraActual() {
        // Capturar la fecha y hora actual
        java.util.Date utilDate = new java.util.Date();  // Captura la fecha y hora actual

        // Convertir a java.sql.Date
        java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());  // Convierte a java.sql.Date

        // Convertir a java.sql.Time
        java.sql.Time sqlTime = new java.sql.Time(utilDate.getTime());  // Convierte a java.sql.Time

        // Imprimir resultados
        return new Object[]{sqlDate, sqlTime};
    }

    public List<Contenedor1> ordenarContenedores(List<Contenedor1> contenedores) {
        Collections.sort(contenedores, new Comparator<Contenedor1>() {
            @Override
            public int compare(Contenedor1 c1, Contenedor1 c2) {
                return Long.compare(c1.getTiempoLlegada(), c2.getTiempoLlegada());
            }
        });
        return contenedores;
    }

    public int funcionAuxiliarEjec(String nombreAlgoritmo, List<Contenedor1> conts, int listado_id, double TurnaroundTimeP, double ResponseTimeP) {
        for (Contenedor1 c : conts) {
            int actualizarC = daoC.DaoActualizarContenedor(c);
            System.out.println("resultado de ActualizarContenedor: " + actualizarC);

        }

        Object[] fechaHora = fechaHoraActual();
        int crearEjecucion = insertarEjecucion(listado_id, nombreAlgoritmo, TurnaroundTimeP, ResponseTimeP, (Date) fechaHora[0], (Time) fechaHora[1]);
        return crearEjecucion;

    }

    public String EjecutarAlgoritmo(String nombreAlgoritmo, int listado_id) throws InterruptedException {
        List<Contenedor1> conts = ordenarContenedores(daoC.DaobtenerConetenedoresListado(listado_id));

        switch (nombreAlgoritmo) {
            case "FIFO":
                fifo = new FIFO(conts);
                fifo.ejecutarContenedores();

                int res = funcionAuxiliarEjec(nombreAlgoritmo, conts, listado_id, fifo.getTornaroundTimeP(), fifo.getResponseTimeP());
                System.out.println("resultado crearEjecucion: " + res);

                return fifo.getResultadoE();

            case "RoundRobin":
                RR = new RoundRobin(conts, 2);
                RR.ejecutarContenedores();
                int res1 = funcionAuxiliarEjec(nombreAlgoritmo, conts, listado_id, RR.getTornaroundTimeP(), RR.getResponseTimeP());
                System.out.println("resultado crearEjecucion: " + res1);
                return RR.getResultadoE();

            case "SRT":

                SRT S = new SRT(conts);
                S.runScheduler();
                int res2 = funcionAuxiliarEjec(nombreAlgoritmo, conts, listado_id, S.getTornaroundTimeP(), S.getResponseTimeP());
                System.out.println("resultado crearEjecucion: " + res2);
                return S.getResultado();

            case "SPN":

                SPN SP = new SPN(conts);
                SP.runScheduler();
                int res3 = funcionAuxiliarEjec(nombreAlgoritmo, conts, listado_id, SP.getTornaroundTimeP(), SP.getResponseTimeP());
                System.out.println("resultado crearEjecucion: " + res3);
                return SP.getResultado();

            case "HRRN":

                HRRN hrrn = new HRRN(conts);
                hrrn.runScheduler();
                int res4 = funcionAuxiliarEjec(nombreAlgoritmo, conts, listado_id, hrrn.getTornaroundTimeP(), hrrn.getResponseTimeP());
                System.out.println("resultado crearEjecucion: " + res4);
                return hrrn.getResultado();

            // se pueden agregar más casos según sea necesario
            default:
                // código a ejecutar si ninguno de los casos anteriores coincide
                return null;

        }
    }

    /*public String EjecutarAlgoritmo(String nombreAlgoritmo, int listado_id) throws InterruptedException {
        List<Contenedor1> conts = ordenarContenedores(daoC.DaobtenerConetenedoresListado(listado_id));
        //List<Contenedor3> conts3 = ordenarContenedores3(daoC.DaobtenerConetenedoresListado3(listado_id));
        
      
        
        switch (nombreAlgoritmo) {
            case "FIFO":
                fifo = new FIFO(conts);
                fifo.ejecutarContenedores();
                
                for (Contenedor1 c : conts) {
                    int actualizarC = daoC.DaoActualizarContenedor(c);
                    System.out.println("resultado de ActualizarContenedor: " + actualizarC);
                    
                }
                
                Object[] fechaHora = fechaHoraActual();
                int crearEjecucion = insertarEjecucion(listado_id, "FIFO", fifo.getTornaroundTimeP(), fifo.getResponseTimeP(), (Date) fechaHora[0], (Time) fechaHora[1]);
                System.out.println("resultado crearEjecucion: " + crearEjecucion);
                
                return fifo.getResultadoE();
            
            case "RoundRobin":                
                RR = new RoundRobin(conts, 2);
                RR.ejecutarContenedores();
                for (Contenedor1 c : conts) {
                    int actualizarC = daoC.DaoActualizarContenedor(c);
                    System.out.println("resultado de ActualizarContenedor: " + actualizarC);
                }
                
                Object[] fechaH = fechaHoraActual();
                int crearEjec = insertarEjecucion(listado_id, "RoundRobin", RR.getTornaroundTimeP(), RR.getResponseTimeP(), (Date) fechaH[0], (Time) fechaH[1]);
                System.out.println("resultado crearEjecucion: " + crearEjec);
                return RR.getResultadoE();
            
            case "SRT": 
                
                SRT S = new SRT(conts);
                S.runScheduler();
                for (Contenedor1 c : S.getContenedoresCompletos()) {
                    int actualizarC = daoC.DaoActualizarContenedor(c);
                    System.out.println("resultado de ActualizarContenedor: " + actualizarC);
                }
                
                Object[] fecha = fechaHoraActual();
                int crearE = insertarEjecucion(listado_id, "SRT", S.getTornaroundTimeP(), S.getResponseTimeP(), (Date) fecha[0], (Time) fecha[1]);
                System.out.println("resultado crearEjecucion: " + crearE);
                return S.getResultado();
                
             case "SPN": 
                
                SPN SP = new SPN(conts);
                SP.runScheduler();
                for (Contenedor1 c : SP.getContenedoresCompletos()) {
                    int actualizarC = daoC.DaoActualizarContenedor(c);
                    System.out.println("resultado de ActualizarContenedor: " + actualizarC);
                }
                
                Object[] fechaS = fechaHoraActual();
                int crearEjecu = insertarEjecucion(listado_id, "SRT", SP.getTornaroundTimeP(), SP.getResponseTimeP(), (Date) fechaS[0], (Time) fechaS[1]);
                System.out.println("resultado crearEjecucion: " + crearEjecu);
                return SP.getResultado();

            // se pueden agregar más casos según sea necesario
            default:
                // código a ejecutar si ninguno de los casos anteriores coincide
                return null;
            
        }
        
    }*/
    public static void main(String[] args) {
        ControladorEjecucion cont = new ControladorEjecucion();
        /*try {
            System.out.println(cont.EjecutarAlgoritmo("RoundRobin", 2));
        } catch (InterruptedException ex) {
            Logger.getLogger(ControladorEjecucion.class.getName()).log(Level.SEVERE, null, ex);
        }*/

 /* ResultSet res=cont.consultarResultados(2);
        
        try {
            while(res.next()){
                System.out.println(res.getInt(1));
                System.out.println(res.getString(2));
                System.out.println(res.getDouble(3));
                
            }
        } catch (SQLException ex) {
            Logger.getLogger(ControladorEjecucion.class.getName()).log(Level.SEVERE, null, ex);
        }
         */
        //cont.crearImagen();
        /*Image imagen=cont.verificarImagen();
        System.out.println(imagen.getId());
         */
       cont.ejecutarDB();
        
     

    }

}
