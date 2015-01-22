package r2r.persistencia.facades;

import r2r.entityjson.LugarJson;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import r2r.persistencia.entidades.Lugar;

@Stateless
public class LugarFacade extends AbstractFacade<Lugar> {

    @PersistenceContext(unitName = "Road2RoldanilloWSPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public LugarFacade() {
        super(Lugar.class);
    }

    public Double getPuntaje(Integer idLugar) {
        getEntityManager();
        Double resultado = 0.0;
        try {
            Query query = em.createQuery("SELECT AVG(c.puntaje) FROM Comentario c WHERE c.lugar.id = :id");
            query.setParameter("id", idLugar);
            query.setMaxResults(1);
            resultado = (Double) query.getSingleResult();

            System.out.println("\n============= RESULTADO =============");
            System.out.println(resultado.toString());
            System.out.println("============= RESULTADO =============\n");

        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultado;
    }

    public List<Lugar> getListLugarByFecha(long timestamp) {
        List<Lugar> lugares = new ArrayList<>();
        try {
            Date fecha = new Date(timestamp);
            Query query = getEntityManager().createNamedQuery("Lugar.findByFecha");
            query.setParameter("fecha", fecha);
            lugares = query.getResultList();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return lugares;
    }

    public List<Lugar> getListLugarByBorrado(int borrado) {
        List<Lugar> lugares = new ArrayList<>();
        try {
            Query query = getEntityManager().createNamedQuery("Lugar.findByBorrado");
            query.setParameter("borrado", borrado);
            lugares = query.getResultList();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return lugares;
    }

    public List<LugarJson> getListaLugaresTMP(Long timeStamp) {
        List<Lugar> lugares = getListLugarByFecha(timeStamp);
        List<LugarJson> lugaresJsons = new ArrayList<>();
        for (Lugar lugar : lugares) {
            LugarJson lugarJson = new LugarJson();
            lugarJson.setBorrado(lugar.getBorrado());
            lugarJson.setCategoria(lugar.getCategoria().getId());
            lugarJson.setDireccion(lugar.getDireccion());
            lugarJson.setFecha(lugar.getFecha());
            lugarJson.setId(lugar.getId());
            lugarJson.setLatitud(lugar.getLatitud());
            lugarJson.setLongitud(lugar.getLongitud());
            lugarJson.setNombre(lugar.getNombre());
            lugarJson.setPuntaje(lugar.getPuntaje());
            lugarJson.setSitio(lugar.getSitio());
            lugarJson.setTelefono(lugar.getTelefono());
            lugaresJsons.add(lugarJson);
        }
        return lugaresJsons;
    }

}
