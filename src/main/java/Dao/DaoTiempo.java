/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import logica.Tiempo;

/**
 *
 * @author andresuv
 */
public class DaoTiempo {

    Interfaz interfaz;
    Connection conexion;

    public DaoTiempo() {
        interfaz = new Interfaz();
    }

    public int DaoInsertarTiempo(Tiempo t) {
        String sql_isertar = "INSERT INTO tiempo(t_inicial,t_ejecucion) VALUES (?,?)";
        try {
            conexion = interfaz.getConnetion();
            PreparedStatement ptm = conexion.prepareStatement(sql_isertar);
            ptm.setDouble(1, t.getT_inicial());
            ptm.setDouble(2, t.getT_ejecucion());
            int result = ptm.executeUpdate();
            return result;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
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
    

}
