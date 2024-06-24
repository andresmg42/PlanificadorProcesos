/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controlador;

import Dao.DaoListado;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.Time;
import logica.Listado;

/**
 *
 * @author Jose Daniel
 */
public class ControladorListado {

    public ControladorListado() {
    }

    //OBJETOS DE OTRAS CLASES
    DaoListado daoListado = new DaoListado();

    public ResultSet listarListados() {
        return daoListado.DaolistarListado();
    }

    public int insertarListado(int id_listado,String nombre,Date fecha,Time hora) {
        Listado l=new Listado(id_listado,nombre,fecha,hora);
         return daoListado.DaoInsertarListado(l);
        
    }

    public static void main(String[] args) {
        
        ControladorListado con = new ControladorListado();

        int res=con.insertarListado(1,"listadoPP", Date.valueOf("2024-06-19"), Time.valueOf("20:54:00"));
        System.out.println(res);

    }
}
