package com.example.bodegapp.Objetos;

public class Producto {

    String uid;
    String referencia;
    String lugar;
    int piso;
    String cama;
    String parte;

    public Producto() {
    }

    public Producto(String uid, String referencia, String lugar, int piso, String cama, String parte) {
        this.uid = uid;
        this.referencia = referencia;
        this.lugar = lugar;
        this.piso = piso;
        this.cama = cama;
        this.parte = parte;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getReferencia() {
        return referencia;
    }

    public void setReferencia(String referencia) {
        this.referencia = referencia;
    }

    public String getLugar() {
        return lugar;
    }

    public void setLugar(String lugar) {
        this.lugar = lugar;
    }

    public int getPiso() {
        return piso;
    }

    public void setPiso(int piso) {
        this.piso = piso;
    }

    public String getCama() {
        return cama;
    }

    public void setCama(String cama) {
        this.cama = cama;
    }

    public String getParte() {
        return parte;
    }

    public void setParte(String parte) {
        this.parte = parte;
    }
}
