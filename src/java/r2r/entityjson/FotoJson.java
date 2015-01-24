package r2r.entityjson;

public class FotoJson {
    private Integer id;
    private String foto;
//    private Date fecha;
    private int borrado;

    public FotoJson() {
    }

    public FotoJson(Integer id, String foto, int borrado) {
        this.id = id;
        this.foto = foto;
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

    public int getBorrado() {
        return borrado;
    }

    public void setBorrado(int borrado) {
        this.borrado = borrado;
    }
    
    
}
