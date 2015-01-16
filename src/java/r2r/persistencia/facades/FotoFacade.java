package r2r.persistencia.facades;

import java.util.ArrayList;
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
        System.out.println("Nombre Lugar: " + lugar);
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
        System.out.println("Resultado Lugar: " + l.getNombre());
        return l;
    }

    public List<String> getFotosByLugar(Lugar lugar) {
        System.out.println("LugarID: " + lugar.getId());
        List<String> nomfotos = new ArrayList<>();
        try {
            Query query = getEntityManager().createNamedQuery("Foto.findByLugar");
            query.setParameter("lugar", lugar.getId());
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

}
