/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package logica;

/**
 *
 * @author andresuv
 */
public class Comando {
    private String nombre_imagen;
    private String nombre_comando;
    
    public Comando(){}

    public Comando(String nombre_imagen,String nombre_comando){
    this.nombre_comando=nombre_comando;
    this.nombre_imagen=nombre_imagen;
    }
    
    public String getNombre_imagen() {
        return nombre_imagen;
    }

    public void setNombre_imagen(String nombre_imagen) {
        this.nombre_imagen = nombre_imagen;
    }

    public String getNombre_comando() {
        return nombre_comando;
    }

    public void setNombre_comando(String nombre_comando) {
        this.nombre_comando = nombre_comando;
    }
    
    
    
}
