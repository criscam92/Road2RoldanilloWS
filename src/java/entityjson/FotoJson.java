package entityjson;

import java.util.Date;

public class FotoJson {
    private Integer id;
    private String foto;
    private Integer lugar;
    private Date fecha;
    private int borrado;

    public FotoJson() {
    }

    public FotoJson(Integer id, String foto, Integer lugar, Date fecha, int borrado) {
        this.id = id;
        this.foto = foto;
        this.lugar = lugar;
        this.fecha = fecha;
        this.borrado = borrado;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public Integer getLugar() {
        return lugar;
    }

    public void setLugar(Integer lugar) {
        this.lugar = lugar;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public int getBorrado() {
        return borrado;
    }

    public void setBorrado(int borrado) {
        this.borrado = borrado;
    }
    
    
}
