/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import logica.Comando;

/**
 *
 * @author andresuv
 */
public class DaoComando {
    Interfaz interfaz;
    Connection conexion;
    
    public DaoComando(){
    interfaz=new Interfaz();
    //conexion=interfaz.getConnetion();
    
    }
    
    public int DaoInsertarComando(Comando c){
     String insertar_sql="INSERT INTO comando VALUES(?,?) ";
        try {
         conexion=interfaz.openConnection();
         PreparedStatement ptm=conexion.prepareStatement(insertar_sql);
         ptm.setString(1,c.getNombre_imagen());
         ptm.setString(2,c.getNombre_comando());
         int result=ptm.executeUpdate();
         return result;
         
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }catch(Exception e){
            System.out.println(e.getMessage());
        }finally{
            try {
                conexion.close();
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
        return -1;
    
    }
    
    public Comando DaoBuscarComando(String nombreI){
    Comando com=new Comando();
    String sql_buscar="SELECT * FROM comando WHERE nombre_imagen=?";
        try {
            conexion=interfaz.openConnection();
           PreparedStatement ptm= conexion.prepareStatement(sql_buscar);
           ptm.setString(1, nombreI);
           ResultSet result=ptm.executeQuery();
           while(result.next()){
        
           com.setNombre_imagen(result.getString(1));
           com.setNombre_imagen(result.getString(2));
           
               
           }
           return com;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }finally{
            try {
                conexion.close();
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
        return null;
    
    }
    
    public static void main(String[] args) {
        DaoComando dao=new DaoComando();
        Comando com=new Comando();
        /*com.setComando_id("33242fds");
        com.setNombre_imagen("cat");
        com.setNombre_comando("cat");*/
        System.out.println(dao.DaoBuscarComando("sleep2").getNombre_imagen()); 
        System.out.println(dao.DaoInsertarComando(new Comando("sleep10","sleep 10")));
        
    }
           
    
   
    
}
