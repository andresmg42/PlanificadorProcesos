
package cliente_docker.versionesContenedores;

import com.github.dockerjava.api.command.CreateContainerResponse;

/**
 *
 * @author andresuv
 */
public class DockerContainer implements Comparable<DockerContainer>{
    
   
    int tiempoLlegada;
    int tiempoEstimadoIngresado;
    int tiempoRestante;
    int containerId;
    String nombreI;
    CreateContainerResponse container;
    int turnaroudTime;
    int ResponseTime;
    int tiempoInicio;
    int tiempoFinal;

    public DockerContainer(int contenedorId,String nombreI, int tiempoLlegada, int tiempoEstimadoIngresado) {
        this.containerId=contenedorId;
        this.tiempoLlegada = tiempoLlegada;
        this.tiempoEstimadoIngresado = tiempoEstimadoIngresado;
        this.tiempoRestante = tiempoEstimadoIngresado;
        this.nombreI=nombreI;
    }

    @Override
    public int compareTo(DockerContainer other) {
        return Long.compare(this.tiempoRestante, other.tiempoRestante);
    }

    public int getTiempoLlegada() {
        return tiempoLlegada;
    }

    public void setTiempoLlegada(int tiempoLlegada) {
        this.tiempoLlegada = tiempoLlegada;
    }

    public int getTiempoEstimadoIngresado() {
        return tiempoEstimadoIngresado;
    }

    public void setTiempoEstimadoIngresado(int tiempoEstimadoIngresado) {
        this.tiempoEstimadoIngresado = tiempoEstimadoIngresado;
    }

    public int getTiempoRestante() {
        return tiempoRestante;
    }

    public void setTiempoRestante(int tiempoRestante) {
        this.tiempoRestante = tiempoRestante;
    }

    public int getContainerId() {
        return containerId;
    }

    public void setContainerId(int containerId) {
        this.containerId = containerId;
    }

    public String getNombreI() {
        return nombreI;
    }

    public void setNombreI(String nombreI) {
        this.nombreI = nombreI;
    }

    public CreateContainerResponse getContainer() {
        return container;
    }

    public void setContainer(CreateContainerResponse container) {
        this.container = container;
    }

    public int getTurnaroudTime() {
        return turnaroudTime;
    }

    public void setTurnaroudTime(int turnaroudTime) {
        this.turnaroudTime = turnaroudTime;
    }

    public int getResponseTime() {
        return ResponseTime;
    }

    public void setResponseTime(int ResponseTime) {
        this.ResponseTime = ResponseTime;
    }

    public int getTiempoInicio() {
        return tiempoInicio;
    }

    public void setTiempoInicio(int tiempoInicio) {
        this.tiempoInicio = tiempoInicio;
    }

    public int getTiempoFinal() {
        return tiempoFinal;
    }

    public void setTiempoFinal(int tiempoFinal) {
        this.tiempoFinal = tiempoFinal;
    }
    
    
    
    
    
    
}


