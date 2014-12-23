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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@Entity
@Table(catalog = "road2roldanillo", schema = "public")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Categoria.findAll", query = "SELECT c FROM Categoria c"),
    @NamedQuery(name = "Categoria.findById", query = "SELECT c FROM Categoria c WHERE c.id = :id"),
    @NamedQuery(name = "Categoria.findByNombre", query = "SELECT c FROM Categoria c WHERE c.nombre = :nombre"),
    @NamedQuery(name = "Categoria.findByMDPI", query = "SELECT c FROM Categoria c WHERE c.mdpi = :mdpi"),
    @NamedQuery(name = "Categoria.findByHDPI", query = "SELECT c FROM Categoria c WHERE c.hdpi = :hdpi"),
    @NamedQuery(name = "Categoria.findByXHDPI", query = "SELECT c FROM Categoria c WHERE c.xhdpi = :xhdpi"),
    @NamedQuery(name = "Categoria.findByXXHDPI", query = "SELECT c FROM Categoria c WHERE c.xxhdpi = :xxhdpi")})
public class Categoria implements Serializable {

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
    @Size(min = 1, max = 100)
    @Column(nullable = false, length = 100)
    private String mdpi;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(nullable = false, length = 100)
    private String hdpi;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(nullable = false, length = 100)
    private String xhdpi;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(nullable = false, length = 100)
    private String xxhdpi;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "categoria", fetch = FetchType.LAZY)
    private List<Lugar> lugarList;

    public Categoria() {
    }

    public Categoria(Integer id) {
        this.id = id;
    }

    public Categoria(Integer id, String nombre, String mdpi, String hdpi, String xhdpi, String xxhdpi) {
        this.id = id;
        this.nombre = nombre;
        this.mdpi = mdpi;
        this.hdpi = hdpi;
        this.xhdpi = xhdpi;
        this.xxhdpi = xxhdpi;
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

    public String getMDPI() {
        return mdpi;
    }

    public void setMDPI(String mdpi) {
        this.mdpi = mdpi;
    }

    public String getHDPI() {
        return hdpi;
    }

    public void setHDPI(String hdpi) {
        this.hdpi = hdpi;
    }

    public String getXHDPI() {
        return xhdpi;
    }

    public void setXHDPI(String xhdpi) {
        this.xhdpi = xhdpi;
    }

    public String getXXHDPI() {
        return xxhdpi;
    }

    public void setXXHDPI(String xxhdpi) {
        this.xxhdpi = xxhdpi;
    }

    @XmlTransient
    public List<Lugar> getLugarList() {
        return lugarList;
    }

    public void setLugarList(List<Lugar> lugarList) {
        this.lugarList = lugarList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Categoria)) {
            return false;
        }
        Categoria other = (Categoria) object;
        return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
    }

    @Override
    public String toString() {
        return nombre;
    }

}
