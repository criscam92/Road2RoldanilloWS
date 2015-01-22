package r2r.entityjson;

import java.util.Date;

public class ComentarioJson {
    private Integer id;
    private String detalle;
    private Date fecha;
    private int puntaje;
    private Integer lugar;
    private String usuarioId;
    private String usuarioNombre;

    public ComentarioJson() {
    }

    public ComentarioJson(Integer id, String detalle, Date fecha, int puntaje, Integer lugar, String usuarioId, String usuarioNombre) {
        this.id = id;
        this.detalle = detalle;
        this.fecha = fecha;
        this.puntaje = puntaje;
        this.lugar = lugar;
        this.usuarioId = usuarioId;
        this.usuarioNombre = usuarioNombre;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDetalle() {
        return detalle;
    }

    public void setDetalle(String detalle) {
        this.detalle = detalle;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public int getPuntaje() {
        return puntaje;
    }

    public void setPuntaje(int puntaje) {
        this.puntaje = puntaje;
    }

    public Integer getLugar() {
        return lugar;
    }

    public void setLugar(Integer lugar) {
        this.lugar = lugar;
    }

    public String getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(String usuarioId) {
        this.usuarioId = usuarioId;
    }

    public String getUsuarioNombre() {
        return usuarioNombre;
    }

    public void setUsuarioNombre(String usuarioNombre) {
        this.usuarioNombre = usuarioNombre;
    }
    
    
}
