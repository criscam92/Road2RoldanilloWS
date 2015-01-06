package r2r.persistencia.facades;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import r2r.persistencia.entidades.Categoria;

@Stateless
public class CategoriaFacade extends AbstractFacade<Categoria> {

    @PersistenceContext(unitName = "Road2RoldanilloWSPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public CategoriaFacade() {
        super(Categoria.class);
    }

    public List<Categoria> getListCategotiasByFecha(long timestamp) {
        getEntityManager();
        List<Categoria> categorias = new ArrayList<>();
        try {
            Date fecha = new Date(timestamp);
            Query query = em.createQuery("SELECT c FROM Categoria c WHERE c.fecha >= :fecha");
            query.setParameter("fecha", fecha);
            categorias = query.getResultList();
            
            System.out.println("================= CATEGORIAS ===============");
            System.out.println("" + categorias.size());
            System.out.println("================= CATEGORIAS ===============");

        } catch (Exception e) {
            e.printStackTrace();
        }
        return categorias;
    }

}
