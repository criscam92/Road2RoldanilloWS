package r2r.persistencia.controllers;

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
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import org.primefaces.model.UploadedFile;

@ManagedBean(name = "categoriaController")
@SessionScoped
public class CategoriaController implements Serializable {

    @EJB
    private CategoriaFacade categoriaFacade;
    private static final String FILE_SEPARATOR = System.getProperty("file.separator");
    private List<Categoria> items = null;
    private Categoria selected;
    private UploadedFile mdpi, hdpi, xhdpi, xxhdpi;
    private final Map<String, UploadedFile> mapImagenes = new HashMap<>();

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
        if (getMdpi() != null && getHdpi() != null && getXhdpi() != null && getXxhdpi() != null) {
            llenarMapa();
            selected.setIcono(selected.getNombre() + ".png");
            Calendar fecha = Calendar.getInstance();
            selected.setFecha(fecha.getTime());
            selected.setBorrado(0);
        }

        persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("CategoriaCreated"));
        if (!JsfUtil.isValidationFailed()) {
            items = null;
            guardarImagenes(selected.getIcono());

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

        Integer getKey(String value) {
            Integer key;
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

    private void guardarImagenes(String nombreImagen) {
        try {
            for (Map.Entry<String, UploadedFile> entrySet : getMapImagenes().entrySet()) {
                String key = entrySet.getKey();
                UploadedFile value = entrySet.getValue();
                String path = ResourceBundle.getBundle("/Bundle").getString("Uploaded") + FILE_SEPARATOR + key + FILE_SEPARATOR + nombreImagen;
                System.out.println("PATH: " + path);
                copyFile(value, path);
            }
        } catch (Exception e) {
            System.out.println("==========ERROR==========");
            e.printStackTrace();
            System.out.println("==========ERROR==========");
        }
    }

    private void llenarMapa() {
        mapImagenes.put("mdpi", getMdpi());
        mapImagenes.put("hdpi", getHdpi());
        mapImagenes.put("xhdpi", getXhdpi());
        mapImagenes.put("xxhdpi", getXxhdpi());
    }

}
