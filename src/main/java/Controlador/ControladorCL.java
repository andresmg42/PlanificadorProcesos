/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controlador;
import Dao.DaoCL;

/**
 *
 * @author Jose Daniel
 */
public class ControladorCL {
    
    public ControladorCL(){}
    
    //OBJETOS DE OTRAS CLASES
    DaoCL daoCL = new DaoCL();
    
    //METODOS
    public int insertarCL(int comando, int listado_id){
        return daoCL.DaoinsertarCL(comando, listado_id);
    }
    
    public static void main(String[] args) {
        ControladorCL cl=new ControladorCL();
        
        
        cl.insertarCL(5,2 );
        
        cl.insertarCL(4,2);
        
        cl.insertarCL(6,2 );
        
        cl.insertarCL(7,2 );
    }
}
