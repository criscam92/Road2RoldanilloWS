package r2r.servicios;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ResourceBundle;
import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ImageServlet extends HttpServlet {

    private static final String PATH = ResourceBundle.getBundle("/Bundle").getString("Uploaded");
    private static final String FILE_SEPARATOR = System.getProperty("file.separator");

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/plain");

        try (OutputStream out = response.getOutputStream()) {

            String pathIngfo = request.getPathInfo();
            String[] parts = pathIngfo.split("/");
            for (String part : parts) {
                System.out.println(part);
            }
            String size = parts[1];

            String type = parts[2];

            String fileName = parts[3];

            String extension = fileName.split("\\.")[fileName.split("\\.").length - 1];

            File f = new File(PATH + FILE_SEPARATOR + size + FILE_SEPARATOR
                    + type + FILE_SEPARATOR + fileName);
            System.out.println(f.getAbsolutePath());
            BufferedImage bi = ImageIO.read(f);
            ImageIO.write(bi, extension, out);
            out.close();
        }

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