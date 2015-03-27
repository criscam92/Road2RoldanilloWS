/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package r2r.servicios;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import r2r.entityjson.ComentarioJson;
import r2r.persistencia.entidades.Comentario;
import r2r.persistencia.entidades.Lugar;
import r2r.persistencia.facades.ComentarioFacade;
import r2r.persistencia.facades.LugarFacade;

/**
 *
 * @author arturo
 */
@ManagedBean
public class ComentarioServlet extends HttpServlet {

    @EJB
    private ComentarioFacade comentarioFacade;
    @EJB
    private LugarFacade lugarFacade;
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/plain;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            String body = convertInputStreamToString(request.getReader());
            System.out.println("Peticion: ==================================================");
            System.out.println(body);
            JsonArray array = new JsonArray();
            try {
                ComentarioJson[] comentarios = new Gson().fromJson(body,ComentarioJson[].class);
 
                for (ComentarioJson comentarioAuxiliar : comentarios) {
                        Comentario comentario = new Comentario();
                        comentario.setId(comentarioAuxiliar.getId());
                        Lugar lugar = lugarFacade.find(comentarioAuxiliar.getLugar());
                        comentario.setLugar(lugar);
                        comentario.setBorrado(comentarioAuxiliar.getBorrado());
                        comentario.setDetalle(comentarioAuxiliar.getDetalle());
                        comentario.setFecha(comentarioAuxiliar.getFecha());
                        comentario.setPuntaje(comentarioAuxiliar.getPuntaje());
                        comentario.setUsuarioId(comentarioAuxiliar.getUsuario());
                        comentario.setUsuarioNombre(comentarioAuxiliar.getUsuarioNombre());
                        comentarioFacade.create(comentario);
                        
                        JsonObject object = new JsonObject();
                        object.addProperty("idServidor", comentario.getId());
                        object.addProperty("idDispositivo", comentarioAuxiliar.getId());
                        array.add(object);
                        
                }
                String respuesta = new Gson().toJson(array);
                out.write(respuesta);
                System.out.println("Respuesta:======================================================");
                System.out.println(respuesta);
 
            } catch (Exception e) {
                e.printStackTrace();
            }
           
//            out.write("lol");
        }
        
    }
    
    private String convertInputStreamToString(BufferedReader reader) throws IOException{
        String line;
        String result = "";
        while((line = reader.readLine()) != null)
            result += line;
        return result;
 
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
