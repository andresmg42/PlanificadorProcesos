/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Dao;

import cliente_docker.versionesContenedores.Contenedor1;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author andresuv
 */
public class DaoContenedor {

    Interfaz interfaz;
    Connection conexion;

    public DaoContenedor() {
        interfaz = new Interfaz();
    }

    public int DaoInsertarContenedor(Contenedor1 c) {
        String insertar_sql = "INSERT INTO contenedor (nombre_imagen,t_llegada,t_estimado_ingresado) VALUES(?,?,?) ";
        try {
            conexion = interfaz.openConnection();
            PreparedStatement ptm = conexion.prepareStatement(insertar_sql);
            ptm.setString(1, c.getNombreI());
            ptm.setDouble(2, c.getTiempoLlegada());
            ptm.setDouble(3, c.getTiempoEstimadoIngresado());

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

    public int DaoActualizarContenedor(Contenedor1 c) {
        String actualizar_sql = "UPDATE contenedor SET t_inicial=?, t_real_estimado=?,t_final=?,t_turnaround_time=?,t_respose_time=? WHERE contenedor_id=?";

        try {
            conexion = interfaz.openConnection();
            PreparedStatement ptm = conexion.prepareStatement(actualizar_sql);
            ptm.setDouble(1, c.getTiempoInicio());
            ptm.setDouble(2, c.getTiempoEstimadoReal());
            ptm.setDouble(3, c.getTiempoFinal());
            ptm.setDouble(4, c.getTornaroundTime());
            ptm.setDouble(5, c.getResponseTime());
            ptm.setInt(6, c.getContenedor_id());

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

    public ResultSet DaoListarContenedores(int listado_id) {
        String sql_listar = "SELECT cont.contenedor_id,com.nombre_imagen,com.nombre_comando,cont.t_llegada,cont.t_estimado_ingresado\n"
                + "FROM contenedor cont,c_l,comando com\n"
                + "WHERE cont.contenedor_id=c_l.contenedor_id\n"
                + "AND cont.nombre_imagen=com.nombre_imagen\n"
                + "AND c_l.listado_id=?";
        try {
            conexion = interfaz.openConnection();
            PreparedStatement ptm = conexion.prepareStatement(sql_listar);
            ptm.setInt(1, listado_id);
            ResultSet result = ptm.executeQuery();
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
    
     public List<Contenedor1> DaobtenerConetenedoresListado(int listado_id) {
    List<Contenedor1> listaC=new ArrayList<>();
    ResultSet con=DaoListarContenedores(listado_id);
    
        try {
         while(con.next()){
        Contenedor1 c=new Contenedor1(con.getInt(1),con.getString(2),con.getString(3),con.getLong(4),con.getLong(5));
        listaC.add(c);
    }
    return listaC;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    return null;
    
    }

}
