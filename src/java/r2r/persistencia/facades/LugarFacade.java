package r2r.persistencia.facades;

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

}
