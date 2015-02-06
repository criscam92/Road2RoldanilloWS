package r2r.persistencia.facades;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import javax.annotation.Resource;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;
import r2r.persistencia.entidades.Categoria;
import r2r.util.JsfUtil;

@Stateless
public class CategoriaFacade extends AbstractFacade<Categoria> {

    @PersistenceContext(unitName = "Road2RoldanilloWSPU")
    private EntityManager em;
    @Resource
    private SessionContext sessionContext;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public CategoriaFacade() {
        super(Categoria.class);
    }

    public List<Categoria> getListCategoriasByFecha(long timestamp) {
        List<Categoria> categorias = new ArrayList<>();
        try {
            Date fecha = new Date(timestamp);
            Query query = getEntityManager().createQuery("SELECT c FROM Categoria c WHERE c.fecha >= :fecha");
            query.setParameter("fecha", fecha);
            categorias = query.getResultList();

        } catch (Exception e) {
            System.out.println("\n\n=================== ERROR CONSULTANDO LAS CATEGORIAS POR FECHA ====================");
//            e.printStackTrace();
            System.out.println("=================== ERROR CONSULTANDO LAS CATEGORIAS POR FECHA ====================\n\n");
        }
        return categorias;
    }

    public List<Categoria> getListCategoriasByBorrado(int borrado) {
        List<Categoria> categorias = new ArrayList<>();
        try {
            Query query = getEntityManager().createNamedQuery("Categoria.findByBorrado");
            query.setParameter("borrado", borrado);
            categorias = query.getResultList();

        } catch (Exception e) {
            System.out.println("\n\n=================== ERROR CONSULTANDO LAS CATEGORIAS POR BORRADO ===================");
//            e.printStackTrace();
            System.out.println("==================== ERROR CONSULTANDO LAS CATEGORIAS POR BORRADO ===================\n\n");
        }
        return categorias;
    }

    public boolean getCategoriaByNombre(String nombre) {
        boolean result = false;
        try {
            Query query = getEntityManager().createQuery("SELECT c FROM Categoria c WHERE c.nombre = :nombre AND c.borrado = :borrado");
            query.setParameter("nombre", nombre.trim());
            query.setParameter("borrado", 0);
            query.setMaxResults(1);

            if (query.getSingleResult() != null) {
                result = true;
            }

        } catch (Exception e) {
            System.out.println("\n\n================== ERROR CONSULTANDO LA CATEGORIA POR NOMBRE =================");
//            e.printStackTrace();
            System.out.println("================== ERROR CONSULTANDO LA CATEGORIA POR NOMBRE =================\n\n");
        }
        return result;
    }

    public boolean getLugarByCategoria(Integer id) {
        boolean result = false;
        try {
            Query query = getEntityManager().createQuery("SELECT l FROM Lugar l WHERE l.categoria.id = :categoria AND l.borrado = :borrado");
            query.setParameter("categoria", id);
            query.setParameter("borrado", 0);
            
            if (query.getResultList() != null && !query.getResultList().isEmpty()) {
                result = true;
            }

        } catch (Exception e) {
            System.out.println("\n\n=============== ERROR CONSULTANDO LOS LUGARES POR CATEGORIA ==============");
//            e.printStackTrace();
            System.out.println("=============== ERROR CONSULTANDO LOS LUGARES POR CATEGORIA ==============\n\n");
        }
        return result;
    }

    public String getNombreIconoByCategoria(Integer id) {
        String nomIcono = "";
        try {
            Query query = getEntityManager().createQuery("SELECT c.icono FROM Categoria c WHERE c.id = id");
            query.setParameter("id", id);
            nomIcono = (String) query.getSingleResult();
        } catch (Exception e) {
            System.out.println("\n\n=================== ERROR OBTENIENDO EL NOMBRE DEL ICONO POR CATEGORIA =====================");
//            e.printStackTrace();
            System.out.println("=================== ERROR OBTENIENDO EL NOMBRE DEL ICONO POR CATEGORIA =====================\n\n");
        }
        return nomIcono;
    }

    public boolean add(Categoria categoria) {
        System.out.println("ENTRE AL METODO ADD");
        boolean result = false;
        try {
            getEntityManager().persist(categoria);
            result = true;
        } catch (Exception e) {
            System.out.println("============ ERROR GUARDANDO LA CATEGORIA ============");
            e.printStackTrace();
            System.out.println("============ ERROR GUARDANDO LA CATEGORIA ============");
        }
        return result;
    }

}
