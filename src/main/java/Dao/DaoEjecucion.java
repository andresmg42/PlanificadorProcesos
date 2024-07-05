/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import logica.Ejecucion;

/**
 *
 * @author andresuv
 */
public class DaoEjecucion {
    Interfaz interfaz;
    Connection conexion;
    
    public DaoEjecucion(){
    interfaz=new Interfaz();
    }
    
    public int DaoInsertarEjecucion(Ejecucion e){
    String sql_insertar="INSERT INTO ejecucion(listado_id,algoritmo,t_tornaround_timep,t_response_timep,fecha,hora) VALUES (?,?,?,?,?,?)";
        try {
            conexion=interfaz.getConnetion();
            PreparedStatement ptm=conexion.prepareStatement(sql_insertar);
            ptm.setInt(1,e.getListado_id());
            ptm.setString(2,e.getAlgoritmo());
            ptm.setLong(3,e.getTornaroundTimeP());
            ptm.setLong(4,e.getResponseTimeP());
            ptm.setDate(5,e.getFecha());
            ptm.setTime(6,e.getTime());
            int result=ptm.executeUpdate();
            return result;
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }finally{
            try {
                conexion.close();
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        }
        return -1;
    }
    
     public ResultSet DaolistarEjecucion() {
        String sql_listar = "SELECT * FROM ejecucion";
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
     
    
     
    
}
