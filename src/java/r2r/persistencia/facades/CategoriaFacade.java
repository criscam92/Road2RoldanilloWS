package r2r.persistencia.facades;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
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

    public boolean getCategoriaByNombre(Categoria categoria) {

        boolean result = false;
        try {
            Query query = getEntityManager().createQuery("SELECT c FROM Categoria c WHERE UPPER(c.nombre) = :nombre AND c.borrado = :borrado");
            query.setParameter("nombre", categoria.getNombre().trim().toUpperCase());
            query.setParameter("borrado", 0);

            Categoria cat = (Categoria) query.getSingleResult();

            if (cat != null) {
                if (categoria.getId() != null) {
                    result = !cat.getId().equals(categoria.getId());
                } else {
                    result = true;
                }
            }

        } catch (NoResultException e) {
            System.out.println("================ El nombre de la categoria no existe ====================");
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

    public Long getCountLugaresByCategoria(Categoria c) {
        try {
            Query query = em.createQuery("SELECT COUNT(l) FROM Lugar l WHERE l.categoria.id= :cat");
            query.setParameter("cat", c.getId());
            return (Long) query.getSingleResult();
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return 0l;
    }

}
