/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controlador;
import Dao.DaoListado;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import logica.Listado;
/**
 *
 * @author Jose Daniel
 */
public class ControladorListado {
    

    public ControladorListado(){}
    
    //OBJETOS DE OTRAS CLASES
    DaoListado daoListado = new DaoListado();
    
    public ResultSet listarListados(){
        return daoListado.DaolistarListado();
    }
    
    public int insertarListado(Listado listado){
        int prueba = daoListado.DaoInsertarListado(listado);         
        return prueba;
    }
    
    public static void main(String[] args) {
        Listado l=new Listado("listadoPP",Date.valueOf("2024-06-19"),Time.valueOf("20:54:00"));
        ControladorListado con = new ControladorListado();
        
            con.insertarListado(l);

    }
}
