package model;

import java.util.Date;

public class CalendarioRiego {
    private int id;
    private Date fechaRiego;
    private String descripcion;

    // Constructor
    public CalendarioRiego(int id, Date fechaRiego, String descripcion) {
        this.id = id;
        this.fechaRiego = fechaRiego;
        this.descripcion = descripcion;
    }

    // Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public Date getFechaRiego() { return fechaRiego; }
    public void setFechaRiego(Date fechaRiego) { this.fechaRiego = fechaRiego; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
}

