/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import logica.Listado;

/**
 *
 * @author andresuv
 */
public class DaoListado {
    Interfaz interfaz;
    Connection conexion;
    
    
    public DaoListado(){
     interfaz=new Interfaz();
    }
    
    public int DaoInsertarListado(Listado l){
    String sql_insertar="INSERT INTO listado(nombre,fecha,hora) VALUES(?,?,?)";
        try {
            conexion=interfaz.getConnetion();
            PreparedStatement ptm=conexion.prepareStatement(sql_insertar);
            ptm.setString(1,l.getNombre());
            ptm.setDate(2,l.getFecha());
            ptm.setTime(3,l.getHora());
            int result=ptm.executeUpdate();
            return result;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        finally{
            try {
                conexion.close();
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
        return -1;
    }
    
    public int DaoEliminarListado(int listado_id){
    String sql_eliminar="DELETE FROM listado WHERE listado_id=?";
        try {
            conexion=interfaz.getConnetion();
            PreparedStatement ptm=conexion.prepareStatement(sql_eliminar);
            ptm.setInt(1,listado_id);
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
    
     public ResultSet DaolistarListado() {
        String sql_listar = "SELECT * FROM listado";
        try {
            conexion = interfaz.getConnetion();
            Statement sentecia = conexion.createStatement();
            ResultSet result = sentecia.executeQuery(sql_listar);
            return result;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                conexion.close();
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
        return null;
    }
    
     
    

    
    public static void main(String[] args) {
        //Listado l=new Listado("listadoP",Date.valueOf("2024-06-19"),Time.valueOf("20:54:00"));
        DaoListado dao =new DaoListado();
        //System.out.println(dao.DaoInsertarListado(l));
        System.out.println(dao.DaoEliminarListado(1));
        
        
       
    }
    
}
