package r2r.persistencia.facades;

import r2r.entityjson.FotoJson;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import r2r.persistencia.entidades.Foto;
import r2r.persistencia.entidades.Lugar;
import r2r.util.JsfUtil;

@Stateless
public class FotoFacade extends AbstractFacade<Foto> {

    @PersistenceContext(unitName = "Road2RoldanilloWSPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public FotoFacade() {
        super(Foto.class);
    }

    public boolean add(Foto foto, List<String> listFotos) {
        boolean result = true;
        try {
            for (String f : listFotos) {
                Foto fo = new Foto();
                fo.setBorrado(foto.getBorrado());
                fo.setFecha(foto.getFecha());
                fo.setFoto(f);
                fo.setLugar(foto.getLugar());
                getEntityManager().persist(fo);
            }
        } catch (Exception e) {
            System.out.println("======================= ERROR ======================");
            e.printStackTrace();
            System.out.println("======================= ERROR ======================");
            JsfUtil.addErrorMessage("Error agregando las fotos");
            result = false;
        }
        return result;
    }

    public List<Foto> getFotosGroupByLugar() {
        List<Foto> fotos = new ArrayList<>();
        try {
            Query query = getEntityManager().createNamedQuery("Foto.findGroupByLugar");
            query.setParameter("borrado", 0);
            fotos = query.getResultList();
        } catch (Exception e) {
            System.out.println("======================= ERROR ======================");
            e.printStackTrace();
            System.out.println("======================= ERROR ======================");
            JsfUtil.addErrorMessage("Error consultando las fotos");
            fotos = new ArrayList<>();
        }
        return fotos;
    }

    public Lugar getLugarByNombre(String lugar) {
        Lugar l = new Lugar();
        try {
            Query query = getEntityManager().createNamedQuery("Lugar.findByNombre");
            query.setParameter("nombre", lugar.trim());
            query.setMaxResults(1);
            l = (Lugar) query.getSingleResult();
        } catch (Exception e) {
            System.out.println("======================= ERROR ======================");
            e.printStackTrace();
            System.out.println("======================= ERROR ======================");
            JsfUtil.addErrorMessage("Error consultando las fotos");
            l = new Lugar();
        }
        return l;
    }

    public List<Foto> getFotosByFecha(Long timeStamp) {
        List<Foto> fotos = new ArrayList<>();
        try {
            Date fecha = new Date(timeStamp);
            Query query = getEntityManager().createNamedQuery("Foto.findByFecha");
            query.setParameter("fecha", fecha);
            fotos = query.getResultList();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return fotos;
    }

    public List<Foto> getFotosByLugar(Lugar lugar) {
        List<Foto> nomfotos = new ArrayList<>();
        try {
            Query query = getEntityManager().createNamedQuery("Foto.findByLugar");
            query.setParameter("lugar", lugar.getId());
            query.setParameter("borrado", 0);
            nomfotos = query.getResultList();
        } catch (Exception e) {
            System.out.println("======================= ERROR ======================");
            e.printStackTrace();
            System.out.println("======================= ERROR ======================");
            JsfUtil.addErrorMessage("Error consultando las fotos");
            nomfotos = new ArrayList<>();
        }
        return nomfotos;
    }

    public List<FotoJson> getListaFotosTMP(Long timeStamp) {
        List<Foto> fotos = getFotosByFecha(timeStamp);
        List<FotoJson> fotosJsons = new ArrayList<>();

        for (Foto foto : fotos) {
            FotoJson fotoJson = new FotoJson();
            fotoJson.setBorrado(foto.getBorrado());
//            fotoJson.setFecha(foto.getFecha());
            fotoJson.setFoto(foto.getFoto());
            fotoJson.setId(foto.getId());

            fotosJsons.add(fotoJson);
        }

        return fotosJsons;
    }

    public boolean removeFoto(Integer foto) {
        try {
            Calendar fec = Calendar.getInstance();
            Query q = getEntityManager().createQuery("UPDATE Foto f SET f.borrado = :borrado, f.fecha = :fecha WHERE f.foto.id = :id");
            q.setParameter("borrado", 1);
            q.setParameter("fecha", fec.getTime());
            q.setParameter("id", foto);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

}
