package r2r.persistencia.facades;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import r2r.persistencia.entidades.Lugar;
import r2r.persistencia.entidades.Usuario;
import r2r.util.JsfUtil;

@Stateless
public class UsuarioFacade extends AbstractFacade<Usuario> {

    @PersistenceContext(unitName = "Road2RoldanilloWSPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public UsuarioFacade() {
        super(Usuario.class);
    }

    public Usuario login(String userName, String password) {
        Usuario usuario = null;
        try {
            Query query = getEntityManager().createNamedQuery("Usuario.findByUsuarioAndContrasena");
            query.setParameter("usuario", userName);
            query.setParameter("contrasena", password);
            query.setMaxResults(1);
            usuario = (Usuario) query.getSingleResult();
        } catch (Exception e) {
            System.out.println("======================= ERROR ======================");
            e.printStackTrace();
            System.out.println("======================= ERROR ======================");
            JsfUtil.addErrorMessage("Error consultando el usuario en la base de datos");
            usuario = null;
        }
        return usuario;
    }

}
