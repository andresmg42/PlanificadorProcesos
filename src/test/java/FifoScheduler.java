
import cliente_docker.versionesContenedores.Contenedor;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
import java.util.LinkedList;
import java.util.Queue;

public class FifoScheduler {

    private Queue<Contenedor> contenedorQueue;
    private  long Tiempo0;

    public FifoScheduler() {
        this.contenedorQueue = new LinkedList<>();
        
    }

    public void agregarContenedor(Contenedor contenedor) {
        contenedorQueue.offer(contenedor);
    }

  
    
    

    public void ejecutarContenedores() {
        long  totalTornaroundTime=0;
        long totalResponseTime=0;
        long count=0;
            
        while (!contenedorQueue.isEmpty()) {
            Contenedor contenedor = contenedorQueue.poll();
            
            System.out.println("Ejecutando contenedor: " + contenedor.getNombreI());
            String resultado = contenedor.ejecutarContenedor();
            System.out.println("Resultado: " + resultado);
            System.out.println("Tiempo llegada: " + contenedor.getTiempoLlegada());

            System.out.println("Tiempo inicial: " + (contenedor.getTiempoInicio()-Tiempo0));
            System.out.println("Tiempo final: " + (contenedor.getTiempoFinal()-Tiempo0));
            System.out.println("Tiempo real estimado: " +(contenedor.getTiempoEstimadoReal()));
            System.out.println("Tiempo estimado Promedio:" + contenedor.getTiempoEstimadoIngresado());
            totalTornaroundTime+=(contenedor.getTiempoFinal()-Tiempo0)-contenedor.getTiempoLlegada();
            System.out.println("TornaroundTime: "+((contenedor.getTiempoFinal()-Tiempo0)-contenedor.getTiempoLlegada()));
            System.out.println("ResponseTime: "+((contenedor.getTiempoInicio()-Tiempo0)-contenedor.getTiempoLlegada()));
            totalResponseTime+=(contenedor.getTiempoInicio()-Tiempo0)-contenedor.getTiempoLlegada();
            count++;
        }
        
        System.out.println("TornaroundTimePromedio: "+totalTornaroundTime/count);
        System.out.println("ResponseTimePromedio: "+totalResponseTime/count);
   
    }

    //public static void main(String[] args) {
        // Crear instancia del planificador FIFO
        FifoScheduler scheduler = new FifoScheduler();

        // Crear y agregar contenedores
       /* Contenedor cont1 = new Contenedor("sleep2", "sleep 2", 0, 2000);
        Contenedor cont2 = new Contenedor("sleep3", "sleep3", 1500, 3000);
        Contenedor cont3 = new Contenedor("sleep4", "sleep 4", 2000, 4000);

        scheduler.agregarContenedor(cont1);
        scheduler.agregarContenedor(cont2);
        scheduler.agregarContenedor(cont3);

        // Ejecutar contenedores
        scheduler.ejecutarContenedores();*/
   // }
}
