package model;

import java.util.Date;

public class HumedadHistorica {
    private int id;
    private double humedad;
    private Date fechaRegistro;

    // Constructor
    public HumedadHistorica(int id, double humedad, Date fechaRegistro) {
        this.id = id;
        this.humedad = humedad;
        this.fechaRegistro = fechaRegistro;
    }

    // Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public double getHumedad() { return humedad; }
    public void setHumedad(double humedad) { this.humedad = humedad; }
    public Date getFechaRegistro() { return fechaRegistro; }
    public void setFechaRegistro(Date fechaRegistro) { this.fechaRegistro = fechaRegistro; }
}