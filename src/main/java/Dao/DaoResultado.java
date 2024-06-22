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
import logica.Resultado;

/**
 *
 * @author andresuv
 */
public class DaoResultado {

    Interfaz interfaz;
    Connection conexion;

    public DaoResultado() {
        interfaz = new Interfaz();
    }

    public int InsertarResultado(Resultado r) {
        String sql_insertar = "INSERT INTO ejecucion(ejecucion_id,t_final,response_time,tornaroun_time) VALUES(?,?,?,?)";
        try {
            conexion = interfaz.getConnetion();
            PreparedStatement ptm = conexion.prepareStatement(sql_insertar);
            ptm.setInt(1, r.getEjecucion_id());
            ptm.setDouble(2, r.getT_final());
            ptm.setDouble(3, r.getResponse_time());
            ptm.setDouble(4, r.getTornaround_time());
            int result = ptm.executeUpdate();
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
        return -1;

    }
    
    //FALTA CONSULTAR RESULTADO

   

}
