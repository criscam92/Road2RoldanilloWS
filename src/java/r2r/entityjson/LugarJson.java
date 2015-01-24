package r2r.entityjson;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import r2r.persistencia.entidades.Foto;

public class LugarJson {
    private Integer id;
    private String nombre;
    private double latitud;
    private double longitud;
    private String descripcion;
    private double puntaje;
    private String direccion;
    private String telefono;
    private String sitio;
    private Date fecha;
    private int borrado;
    private Integer categoria;
    private List<FotoJson> fotos;

    public LugarJson() {
    }
    
    

    public LugarJson(Integer id, String nombre, double latitud, double longitud, String descripcion, double puntaje, String direccion, String telefono, String sitio, Date fecha, int borrado, Integer categoria, List<FotoJson> fotos) {
        this.id = id;
        this.nombre = nombre;
        this.latitud = latitud;
        this.longitud = longitud;
        this.descripcion = descripcion;
        this.puntaje = puntaje;
        this.direccion = direccion;
        this.telefono = telefono;
        this.sitio = sitio;
        this.fecha = fecha;
        this.borrado = borrado;
        this.categoria = categoria;
        this.fotos = fotos;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public double getLatitud() {
        return latitud;
    }

    public void setLatitud(double latitud) {
        this.latitud = latitud;
    }

    public double getLongitud() {
        return longitud;
    }

    public void setLongitud(double longitud) {
        this.longitud = longitud;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public double getPuntaje() {
        return puntaje;
    }

    public void setPuntaje(double puntaje) {
        this.puntaje = puntaje;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getSitio() {
        return sitio;
    }

    public void setSitio(String sitio) {
        this.sitio = sitio;
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

    public int getCategoria() {
        return categoria;
    }

    public void setCategoria(Integer categoria) {
        this.categoria = categoria;
    }

    public List<FotoJson> getFotos() {
        return fotos;
    }

    public void setFotos(List<Foto> fotosTMP) {
        List<FotoJson> fotosList = new ArrayList<>();
        for (Foto foto : fotosTMP) {
            fotosList.add(
                    new FotoJson(foto.getId(), foto.getFoto(), foto.getBorrado())
            );
        }
        this.fotos = fotosList;
    }
    
    
}
