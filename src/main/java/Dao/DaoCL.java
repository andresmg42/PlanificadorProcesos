/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 *
 * @author Jose Daniel
 */
public class DaoCL {
    Interfaz interfaz;
    Connection conexion;
    
    public DaoCL(){
        interfaz = new Interfaz();        
    }
    
    public int DaoinsertarCL(int contenedor_id, int listado_id){
        String insertar = "INSERT INTO c_l (contenedor_id,listado_id) VALUES(?,?)";
        
        try{
            conexion = interfaz.openConnection();
            PreparedStatement ptm = conexion.prepareStatement(insertar);
                
                ptm.setInt(1, contenedor_id);
                ptm.setInt(2, listado_id);
                
                return ptm.executeUpdate();
        }catch (SQLException e) {
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
    
    public static void main(String[] args) {
       
        
    }
}
