/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controlador;
import Dao.DaoComando;
import Dao.DaoContenedor;
import cliente_docker.versionesContenedores.Contenedor1;
import logica.Comando;



/**
 *
 * @author Jose Daniel
 */
public class ControladorContenedor {
    
   DaoContenedor daoC=new DaoContenedor();
   DaoComando daoCom=new DaoComando();
    
    public ControladorContenedor(){}
    
    //OBJETOS DE OTRAS CLASES
    DaoContenedor daoContenedor = new DaoContenedor();
    
    //METODOS 
    public int insertarContenedor(int contenedor_id,String nombreI,String comando,long t_llegada,long t_estimado_ingresado){
        Contenedor1 c=new Contenedor1(contenedor_id,nombreI,comando, t_llegada,t_estimado_ingresado);
        
        if(daoCom.DaoBuscarComando(c.getNombreI()).getNombre_imagen()==null){
        Comando com=new Comando(c.getNombreI(),c.getComando());
        daoCom.DaoInsertarComando(com);
        }
        return daoContenedor.DaoInsertarContenedor(c);
    }
    
    public static void main(String[] args) {
        ControladorContenedor cont=new ControladorContenedor();
        
        System.out.println(cont.insertarContenedor(5,"sleep6", "sleep 6", 2, 6));
        System.out.println(cont.insertarContenedor(4,"sleep4", "sleep 4", 1, 4));
        System.out.println(cont.insertarContenedor(6,"sleep1", "sleep 1",0,1));
        System.out.println(cont.insertarContenedor(7,"sleep2", "sleep 2",3,2));
        
        
    }
    
    
}
