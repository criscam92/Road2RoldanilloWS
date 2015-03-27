package r2r.entityjson;

 
import java.util.Date;
 
/**
 * Created by gurzaf on 1/7/15.
 */
public class ComentarioJson{
 
    private int id;
    private String detalle;
    private Date fecha;
    private int puntaje;
    private int lugar;
    private String usuario;
    private String usuarioNombre;
    private int borrado;
    private int subido;
 
    public ComentarioJson(){}
    
    public ComentarioJson(int id, String detalle, Date fecha, int puntaje, int lugar, String usuario, String usuarioNombre, int borrado, int subido) {
        this.id = id;
        this.detalle = detalle;
        this.fecha = fecha;
        this.puntaje = puntaje;
        this.lugar = lugar;
        this.usuario = usuario;
        this.usuarioNombre = usuarioNombre;
        this.borrado = borrado;
        this.subido = subido;
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
 
    public int getLugar() {
        return lugar;
    }
 
    public void setLugar(int lugar) {
        this.lugar = lugar;
    }
 
    public String getUsuario() {
        return usuario;
    }
 
    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }
 
    public String getUsuarioNombre() {
        return usuarioNombre;
    }
 
    public void setUsuarioNombre(String usuarioNombre) {
        this.usuarioNombre = usuarioNombre;
    }
 
    public int getBorrado() {
        return borrado;
    }
 
    public void setBorrado(int borrado) {
        this.borrado = borrado;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSubido() {
        return subido;
    }

    public void setSubido(int subido) {
        this.subido = subido;
    }
 
}