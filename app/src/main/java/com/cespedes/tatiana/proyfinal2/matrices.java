package com.cespedes.tatiana.proyfinal2;

/**
 * Created by Tatiana on 1/06/2016.
 */
public class matrices {
    double coordenada_x;
    double coordenada_y;
    int conexion, conexionant, estado, nCable, nHuecos;

    public double getCoordenada_x() {

        return coordenada_x;
    }

    public double getCoordenada_y() {

        return coordenada_y;
    }

    public int getConexion() {
        return conexion;
    }

    public void setConexion(int conexion) {
        this.conexion = conexion;
    }

    public int getConexionant() {
        return conexionant;
    }

    public void setConexionant(int conexionant) {
        this.conexionant = conexionant;
    }

    public void setCoordenada_x(double coordenada_x) {
        this.coordenada_x = coordenada_x;
    }

    public void setCoordenada_y(double coordenada_y) {
        this.coordenada_y = coordenada_y;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }

    public int getnCable() {
        return nCable;
    }

    public void setnCable(int nCable) {
        this.nCable = nCable;
    }

    public int getnHuecos() {
        return nHuecos;
    }

    public void setnHuecos(int nHuecos) {
        this.nHuecos = nHuecos;
    }

    public matrices() {
        this.coordenada_x = 0;
        this.coordenada_y = 0;
        this.conexion=10000;
        this.conexionant=0;
        this.estado=0;
        this.nCable=10000;
        this.nHuecos=0;
    }
}
