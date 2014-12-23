
package r2r.persistencia.entidades;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author CRISTIAN
 */
@Entity
@Table(catalog = "road2roldanillo", schema = "public")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Lugar.findAll", query = "SELECT l FROM Lugar l"),
    @NamedQuery(name = "Lugar.findById", query = "SELECT l FROM Lugar l WHERE l.id = :id"),
    @NamedQuery(name = "Lugar.findByNombre", query = "SELECT l FROM Lugar l WHERE l.nombre = :nombre"),
    @NamedQuery(name = "Lugar.findByLatitud", query = "SELECT l FROM Lugar l WHERE l.latitud = :latitud"),
    @NamedQuery(name = "Lugar.findByLongitud", query = "SELECT l FROM Lugar l WHERE l.longitud = :longitud"),
    @NamedQuery(name = "Lugar.findByDescripcion", query = "SELECT l FROM Lugar l WHERE l.descripcion = :descripcion"),
    @NamedQuery(name = "Lugar.findByPuntaje", query = "SELECT l FROM Lugar l WHERE l.puntaje = :puntaje"),
    @NamedQuery(name = "Lugar.findByDireccion", query = "SELECT l FROM Lugar l WHERE l.direccion = :direccion"),
    @NamedQuery(name = "Lugar.findByTelefono", query = "SELECT l FROM Lugar l WHERE l.telefono = :telefono"),
    @NamedQuery(name = "Lugar.findBySitio", query = "SELECT l FROM Lugar l WHERE l.sitio = :sitio")})
public class Lugar implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(nullable = false)
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(nullable = false, length = 100)
    private String nombre;
    @Basic(optional = false)
    @NotNull
    @Column(nullable = false)
    private double latitud;
    @Basic(optional = false)
    @NotNull
    @Column(nullable = false)
    private double longitud;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2147483647)
    @Column(nullable = false, length = 2147483647)
    private String descripcion;
    @Basic(optional = false)
    @NotNull
    @Column(nullable = false)
    private float puntaje;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(nullable = false, length = 100)
    private String direccion;
    @Size(max = 30)
    @Column(length = 30)
    private String telefono;
    @Size(max = 50)
    @Column(length = 50)
    private String sitio;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "lugar", fetch = FetchType.LAZY)
    private List<Foto> fotoList;
    @JoinColumn(name = "categoria", referencedColumnName = "id", nullable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Categoria categoria;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "lugar", fetch = FetchType.LAZY)
    private List<Comentario> comentarioList;

    public Lugar() {
    }

    public Lugar(Integer id) {
        this.id = id;
    }

    public Lugar(Integer id, String nombre, double latitud, double longitud, String descripcion, float puntaje, String direccion) {
        this.id = id;
        this.nombre = nombre;
        this.latitud = latitud;
        this.longitud = longitud;
        this.descripcion = descripcion;
        this.puntaje = puntaje;
        this.direccion = direccion;
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

    public float getPuntaje() {
        return puntaje;
    }

    public void setPuntaje(float puntaje) {
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

    @XmlTransient
    public List<Foto> getFotoList() {
        return fotoList;
    }

    public void setFotoList(List<Foto> fotoList) {
        this.fotoList = fotoList;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    @XmlTransient
    public List<Comentario> getComentarioList() {
        return comentarioList;
    }

    public void setComentarioList(List<Comentario> comentarioList) {
        this.comentarioList = comentarioList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Lugar)) {
            return false;
        }
        Lugar other = (Lugar) object;
        return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
    }

    @Override
    public String toString() {
        return nombre;
    }
    
}
