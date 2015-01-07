package r2r.persistencia.controllers;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import r2r.persistencia.entidades.Categoria;
import r2r.util.JsfUtil;
import r2r.util.JsfUtil.PersistAction;
import r2r.persistencia.facades.CategoriaFacade;
import java.io.Serializable;
import java.nio.file.Files;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.imageio.ImageIO;
import javax.servlet.ServletContext;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

@ManagedBean(name = "categoriaController")
@SessionScoped
public class CategoriaController implements Serializable {

    @EJB
    private CategoriaFacade categoriaFacade;
    private List<Categoria> items = null;
    private Categoria selected;
    private UploadedFile mdpi, hdpi, xhdpi, xxhdpi;
    private Map<String, UploadedFile> mapImagenes = new HashMap<>();
    private int MDPI = 48, HDPI = 72, XHDPI = 96, XXHDPI = 180;

    public CategoriaController() {
    }

    public Categoria getSelected() {
        return selected;
    }

    public void setSelected(Categoria selected) {
        this.selected = selected;
    }

    public UploadedFile getMdpi() {
        return mdpi;
    }

    public void setMdpi(UploadedFile mdpi) {
        this.mdpi = mdpi;
    }

    public UploadedFile getHdpi() {
        return hdpi;
    }

    public void setHdpi(UploadedFile hdpi) {
        this.hdpi = hdpi;
    }

    public UploadedFile getXhdpi() {
        return xhdpi;
    }

    public void setXhdpi(UploadedFile xhdpi) {
        this.xhdpi = xhdpi;
    }

    public UploadedFile getXxhdpi() {
        return xxhdpi;
    }

    public void setXxhdpi(UploadedFile xxhdpi) {
        this.xxhdpi = xxhdpi;
    }

    public Map<String, UploadedFile> getMapImagenes() {
        return mapImagenes;
    }

    protected void setEmbeddableKeys() {
    }

    protected void initializeEmbeddableKey() {
    }

    private CategoriaFacade getFacade() {
        return categoriaFacade;
    }

    public Categoria prepareCreate() {
        selected = new Categoria();
        initializeEmbeddableKey();
        return selected;
    }

    public void create() {
        if (mdpi != null && hdpi != null && xhdpi != null && xxhdpi != null) {
            selected.setIcono(selected.getNombre() + ".png");
            Calendar fecha = Calendar.getInstance();
            selected.setFecha(fecha.getTime());
        } else {
            System.out.println("== DATOS VACIO ==\n");
        }

        System.out.println("================= DATOS ===============");
        System.out.println("NOMBRE: " + selected.getNombre());
        System.out.println("ICONO: " + selected.getIcono());
        System.out.println("FECHA: " + selected.getFecha());
        System.out.println("================= DATOS ===============");

        persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("CategoriaCreated"));
        if (!JsfUtil.isValidationFailed()) {
            items = null;    // Invalidate list of items to trigger re-query.
        } else {
            guardarAdjuntos(selected.getIcono());
        }
    }

    public void update() {
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("CategoriaUpdated"));
    }

    public void destroy() {
        persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("CategoriaDeleted"));
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public List<Categoria> getItems() {
        if (items == null) {
            items = getFacade().findAll();
        }
        return items;
    }

    private void persist(PersistAction persistAction, String successMessage) {
        if (selected != null) {
            setEmbeddableKeys();
            try {
                if (persistAction != PersistAction.DELETE) {
                    getFacade().edit(selected);
                } else {
                    getFacade().remove(selected);
                }
                JsfUtil.addSuccessMessage(successMessage);
            } catch (EJBException ex) {
                String msg = "";
                Throwable cause = ex.getCause();
                if (cause != null) {
                    msg = cause.getLocalizedMessage();
                }
                if (msg.length() > 0) {
                    JsfUtil.addErrorMessage(msg);
                } else {
                    JsfUtil.addErrorMessage(ex, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
                }
            } catch (Exception ex) {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
                JsfUtil.addErrorMessage(ex, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            }
        }
    }

    public List<Categoria> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<Categoria> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }

    @FacesConverter(forClass = Categoria.class)
    public static class CategoriaControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            CategoriaController controller = (CategoriaController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "categoriaController");
            return controller.getFacade().find(getKey(value));
        }

        java.lang.Integer getKey(String value) {
            java.lang.Integer key;
            key = Integer.valueOf(value);
            return key;
        }

        String getStringKey(java.lang.Integer value) {
            StringBuilder sb = new StringBuilder();
            sb.append(value);
            return sb.toString();
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof Categoria) {
                Categoria o = (Categoria) object;
                return getStringKey(o.getId());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), Categoria.class.getName()});
                return null;
            }
        }

    }

    public void handleFileUpload(FileUploadEvent event, int tamano) {
        String msg = "";
        UploadedFile imgTMP = event.getFile();
        String key = "";
        try {
            if (tamano == HDPI) {
                mdpi = imgTMP;
                key = "mdpi";
            } else if (tamano == HDPI) {
                hdpi = imgTMP;
                key = "hdpi";
            } else if (tamano == XHDPI) {
                xhdpi = imgTMP;
                key = "xhdpi";
            } else if (tamano == XXHDPI) {
                xxhdpi = imgTMP;
                key = "xxhdpi";
            }

            if (!isValidImg(tamano, imgTMP)) {
                JsfUtil.addErrorMessage("La imagen debe tener un tamaño de " + tamano + "x" + tamano + " Píxeles");
            } else {
                mapImagenes.put(key, mdpi);
            }
        } catch (Exception e) {
        } finally {
            imgTMP = null;
        }
    }

    private boolean isValidImg(int tamanoImg, UploadedFile img) {
        boolean isValidImg = false;
        try {
            BufferedImage bi = ImageIO.read(new ByteArrayInputStream((byte[]) img.getContents()));
            if (tamanoImg == bi.getWidth() && tamanoImg == bi.getHeight()) {
                isValidImg = true;
            }
        } catch (Exception e) {
            isValidImg = false;
            e.printStackTrace();
        }
        return isValidImg;
    }

    public void copyFile(UploadedFile img, String rDestino) throws IOException {

        try {
            InputStream is = img.getInputstream();
            OutputStream out = new FileOutputStream(new File(rDestino));
            int read = 0;
            byte[] bytes = new byte[1024];
            while ((read = is.read(bytes)) != -1) {
                out.write(bytes, 0, read);
            }
            is.close();
            out.flush();
            out.close();
            System.out.println("New file created!");
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    private void guardarAdjuntos(String nombreImagen) {
        try {
            for (Map.Entry<String, UploadedFile> entrySet : getMapImagenes().entrySet()) {
                String key = entrySet.getKey();
                UploadedFile value = entrySet.getValue();
                ServletContext servletContext = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
                String ruta = servletContext.getRealPath("/resources/" + key + "/" + nombreImagen);
                copyFile(value, ruta);
            }
        } catch (Exception e) {
            System.out.println("==========ERROR==========");
            e.printStackTrace();
            System.out.println("==========ERROR==========");
        }
    }

}
