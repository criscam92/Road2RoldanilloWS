package r2r.persistencia.facades;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import r2r.persistencia.entidades.Usuario;
import r2r.seguridad.Encrypt;

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
        System.out.println("CONTRASEÑA" + Encrypt.getStringMessageDigest(password));
        try {
            Query query = getEntityManager().createNamedQuery("Usuario.findByUsuarioAndContrasena");
            query.setParameter("usuario", userName);
            query.setParameter("contrasena", Encrypt.getStringMessageDigest(password));
            query.setMaxResults(1);
            usuario = (Usuario) query.getSingleResult();
        } catch (Exception e) {
            System.out.println("\n======================= ERROR CONSULTANDO EL USUARIO Y CONTRASENA ======================");
            e.printStackTrace();
            System.out.println("======================= ERROR CONSULTANDO EL USUARIO Y CONTRASENA ======================\n");
        }
        return usuario;
    }

    public boolean getUsuarioByNombre(Usuario usuario) {
        boolean result = false;
        try {
            Query query = getEntityManager().createQuery("SELECT u FROM Usuario u WHERE u.usuario = :usuario");
            query.setParameter("usuario", usuario.getUsuario());
            Usuario user = (Usuario) query.getSingleResult();

            if (user != null) {
                if (usuario.getId() != null) {
                    result = user.getId().equals(usuario.getId());
                } else {
                    result = true;
                }
            }

        } catch (Exception e) {
            System.out.println("================ ERROR CONSULTANDO EL USUARIO POR NOMBRE ====================");
            e.printStackTrace();
            System.out.println("================ ERROR CONSULTANDO EL USUARIO POR NOMBRE ====================");
        }
        return result;
    }

}
