package r2r.persistencia.facades;

import r2r.entityjson.ComentarioJson;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import r2r.persistencia.entidades.Comentario;
import r2r.persistencia.entidades.Foto;

@Stateless
public class ComentarioFacade extends AbstractFacade<Comentario> {
    @PersistenceContext(unitName = "Road2RoldanilloWSPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
    
    public List<Comentario> getComentariosByFecha(Long timeStamp){
        List<Comentario> comentarios = new ArrayList<>();
        try {
            Date fecha = new Date(timeStamp);
            Query query = getEntityManager().createNamedQuery("Comentario.findByFecha");
            query.setParameter("fecha", fecha);
            comentarios = query.getResultList();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return comentarios;
    }

    public ComentarioFacade() {
        super(Comentario.class);
    }
    
    public List<ComentarioJson> getComentariosJsonsTMP(Long timeStamp){
        List<Comentario> comentarios = getComentariosByFecha(timeStamp);
        List<ComentarioJson> comentariosJsons = new ArrayList<>();
        
        for (Comentario comentario : comentarios) {
            ComentarioJson comentarioJson = new ComentarioJson();
            comentarioJson.setDetalle(comentario.getDetalle());
            comentarioJson.setFecha(comentario.getFecha());
            comentarioJson.setId(comentario.getId());
            comentarioJson.setLugar(comentario.getLugar().getId());
            comentarioJson.setPuntaje(comentario.getPuntaje());
            comentarioJson.setUsuarioId(comentario.getUsuarioId());
            comentarioJson.setUsuarioNombre(comentario.getUsuarioNombre());
            
            comentariosJsons.add(comentarioJson);
        }
        return comentariosJsons;
    }
    
}
