package r2r.servicios;

import com.google.gson.Gson;
import entityjson.LugarJson;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.ws.rs.PathParam;
import r2r.persistencia.entidades.Lugar;
import r2r.persistencia.facades.CategoriaFacade;
import r2r.persistencia.facades.ComentarioFacade;
import r2r.persistencia.facades.FotoFacade;
import r2r.persistencia.facades.LugarFacade;

@Stateless
@Path("/datos")
public class r2rServicio {

    @EJB
    private CategoriaFacade categoriaFacade;
    @EJB
    private LugarFacade lugarFacade;
    @EJB
    private FotoFacade fotoFacade;
    @EJB
    private ComentarioFacade comentarioFacade;

    public CategoriaFacade getCategoriaFacade() {
        return categoriaFacade;
    }

    public LugarFacade getLugarFacade() {
        return lugarFacade;
    }

    public FotoFacade getFotoFacade() {
        return fotoFacade;
    }

    public ComentarioFacade getComentarioFacade() {
        return comentarioFacade;
    }

    public r2rServicio() {
    }

    //================== METODOS CATEGORIA ==================
    @GET
    @Path("/categoria/get/{timestamp}")
    @Produces("application/json")
    public String getCategoriaJson(@PathParam("timestamp") Long timeStamp) {
        Gson gson = new Gson();
        String json = gson.toJson(getCategoriaFacade().getListCategoriasByFecha(timeStamp));
        return json;
    }

    //================== METODOS LUGAR ==================
    @GET
    @Path("/lugar/get/{timestamp}")
    @Produces("application/json")
    public String getLugarJson(@PathParam("timestamp") Long timeStamp) {
        Gson gson = new Gson();
        String json = gson.toJson(getLugarFacade().getListaLugaresTMP(timeStamp));
        return json;
    }

    //================== METODOS USUARIO ==================
    @PUT
    @Path("/usuario/put")
    @Consumes("application/json")
    public void putUsuarioJson(String content) {
//        categoria.setNombre(content);
    }

    //================== METODOS COMENTARIOS ==================
    @GET
    @Path("/comentario/get/{timestamp}")
    @Produces("application/json")
    public String getComentarioJson(@PathParam("timestamp") Long timeStamp) {
        Gson gson = new Gson();
        String json = gson.toJson(getComentarioFacade().getComentariosJsonsTMP(timeStamp));
        return json;
    }

    @PUT
    @Path("/comentario/put")
    @Consumes("application/json")
    public void putComentarioJson(String content) {
//        categoria.setNombre(content);
    }

    //================== METODOS FOTO ==================
    @GET
    @Path("/foto/get/{timestamp}")
    @Produces("application/json")
    public String getFotoJson(@PathParam("timestamp") Long timeStamp) {
        Gson gson = new Gson();
        String json = gson.toJson(getFotoFacade().getListaFotosTMP(timeStamp));
        return json;
    }
}
