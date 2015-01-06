package r2r.servicios;

import com.google.gson.Gson;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.ws.rs.PathParam;
import r2r.persistencia.facades.CategoriaFacade;

@Stateless
@Path("/datos")
public class r2rServicio {

    @EJB
    private CategoriaFacade categoriaFacade;

    public CategoriaFacade getCategoriaFacade() {
        return categoriaFacade;
    }

    public r2rServicio() {
    }

    //================== METODOS CATEGORIA ==================
    @GET
    @Path("/categoria/get/{timestamp}")
    @Produces("application/json")
    public String getCategoriaJson(@PathParam("timestamp") Long timeStamp) {
        
        System.out.println("================= CATEGORIAS ===============");
        System.out.println("" + timeStamp);
        System.out.println("================= CATEGORIAS ===============");
        
        Gson gson = new Gson();
        String json = gson.toJson(getCategoriaFacade().getListCategotiasByFecha(timeStamp));
        return json;
    }

    @PUT
    @Path("/categoria/put")
    @Consumes("application/json")
    public void putCategoriaJson(String content) {
//        categoria.setNombre(content);
    }

    @DELETE
    @Path("/categoria/delete")
    @Consumes("application/json")
    public void deleteCategoriaJson(String content) {
//        categoria.setNombre(content);
    }

    //================== METODOS LUGAR ==================
    @GET
    @Path("/lugar/get/{timestamp}")
    @Produces("application/json")
    public String getLugarJson(@PathParam("timestamp") Long timeStamp) {
        return "hola";
    }

    @PUT
    @Path("/lugar/put")
    @Consumes("application/json")
    public void putLugarJson(String content) {
//        categoria.setNombre(content);
    }

    @DELETE
    @Path("/lugar/delete")
    @Consumes("application/json")
    public void deleteLugarJson(String content) {
//        categoria.setNombre(content);
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
        return "hola";
    }

    @PUT
    @Path("/comentario/put")
    @Consumes("application/json")
    public void putComentarioJson(String content) {
//        categoria.setNombre(content);
    }

    @DELETE
    @Path("/comentario/delete")
    @Consumes("application/json")
    public void deleteComentarioJson(String content) {
//        categoria.setNombre(content);
    }

    //================== METODOS FOTO ==================
    @GET
    @Path("/foto/get/{timestamp}")
    @Produces("application/json")
    public String getFotoJson(@PathParam("timestamp") Long timeStamp) {
        return "hola";
    }
}
