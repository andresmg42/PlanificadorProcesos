/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controlador;
import Dao.DaoContenedor;
import cliente_docker.versionesContenedores.Contenedor1;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


/**
 *
 * @author Jose Daniel
 */
public class ControladorContenedor {
    
    public ControladorContenedor(){}
    
    //OBJETOS DE OTRAS CLASES
    DaoContenedor daoContenedor = new DaoContenedor();
    
    //METODOS 
    public int insertarContenedor(Contenedor1 c){
        return daoContenedor.DaoInsertarContenedor(c);
    }
    
   
    
    
}
