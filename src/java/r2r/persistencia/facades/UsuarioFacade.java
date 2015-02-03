package r2r.persistencia.facades;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import r2r.persistencia.entidades.Usuario;
import r2r.seguridad.Encrypt;
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
            query.setParameter("contrasena", Encrypt.getStringMessageDigest(password));
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
