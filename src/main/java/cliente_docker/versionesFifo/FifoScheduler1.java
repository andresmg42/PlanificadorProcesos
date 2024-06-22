package cliente_docker.versionesFifo;



import cliente_docker.versionesContenedores.Contenedor1;
import cliente_docker.versionesContenedores.Contenedor;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
import java.util.LinkedList;
import java.util.Queue;

public class FifoScheduler1 {

    private Queue<Contenedor1> contenedorQueue;
    long tiempo0;
    int numContenedores;

    public FifoScheduler1(int numContenedores) {
        this.contenedorQueue = new LinkedList<>();
        this.numContenedores=numContenedores;
        this.tiempo0=0;
    }

    public void agregarContenedor(Contenedor1 contenedor) {
        contenedorQueue.offer(contenedor);
    }
    
  

   

    public void ejecutarContenedores() {
        long  totalTornaroundTime=0;
        long totalResponseTime=0;
        long count=0;
        while (!contenedorQueue.isEmpty()) {
         Contenedor1 contenedor = contenedorQueue.poll();
            
            System.out.println("Ejecutando contenedor: " + contenedor.getNombreI());
            String resultado = contenedor.ejecutarContenedor();
            if(contenedorQueue.size()==numContenedores-1){
                tiempo0=contenedor.getTiempo0();
            }
            
            if(contenedorQueue.peek()!=null){
            contenedorQueue.peek().setTiempo0(tiempo0);
            }
            
            System.out.println("Resultado: " + resultado);
            System.out.println("Tiempo llegada: " + contenedor.getTiempoLlegada());
            System.out.println("Tiempo inicial: " + contenedor.getTiempoInicio());
            System.out.println("Tiempo final: " + contenedor.getTiempoFinal());
            System.out.println("Tiempo real estimado: " +contenedor.getTiempoEstimadoReal());
            System.out.println("Tiempo estimado Ingresado:" + contenedor.getTiempoEstimadoIngresado());
            totalTornaroundTime+=contenedor.getTornaroundTime();
            System.out.println("TornaroundTime: "+contenedor.getTornaroundTime());
            System.out.println("ResponseTime: "+contenedor.getResponseTime());
            totalResponseTime+=contenedor.getResponseTime();
            count++;
        }
        
        System.out.println("TornaroundTimePromedio: "+totalTornaroundTime/count);
        System.out.println("ResponseTimePromedio: "+totalResponseTime/count);
        System.out.println("count: "+count);
    }

    public static void main(String[] args) {
        // Crear instancia del planificador FIFO
        FifoScheduler1 scheduler = new FifoScheduler1(3);

        // Crear y agregar contenedores
        Contenedor1 cont1 = new Contenedor1("sleep2", "sleep 2",0,2400);
        Contenedor1 cont2 = new Contenedor1("sleep3", "sleep3", 1500, 3000);
        Contenedor1 cont3 = new Contenedor1("sleep4", "sleep 4", 2000, 4000);

        scheduler.agregarContenedor(cont1);
        scheduler.agregarContenedor(cont2);
        scheduler.agregarContenedor(cont3);

        // Ejecutar contenedores
        scheduler.ejecutarContenedores();
    }
}
