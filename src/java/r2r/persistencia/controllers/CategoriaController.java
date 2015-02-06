package r2r.persistencia.controllers;

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
    private List<Categoria> items = null, itemsByBorrado = null;
    private Categoria selected;
    private UploadedFile mdpi, hdpi, xhdpi, xxhdpi;
    private final Map<String, UploadedFile> mapImagenes = new HashMap<>();
    public static boolean imagenValida;

    public CategoriaController() {
        imagenValida = true;
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
        if (imagenValida) {
            if (!getFacade().getCategoriaByNombre(selected.getNombre())) {
                if (createAndUpdate()) {
                    persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("CategoriaCreated"));
                    if (!JsfUtil.isValidationFailed()) {
                        items = null;
                        itemsByBorrado = null;
                        guardarImagenes(selected.getIcono());
                    }
                }
            } else {
                JsfUtil.addErrorMessage("La categoria " + selected.getNombre() + " ya se encuentra creada");
            }
        }
    }

    public void update() {
        if (imagenValida) {
            if (!getFacade().getCategoriaByNombre(selected.getNombre())) {
                if (createAndUpdate()) {
                    persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("CategoriaUpdated"));
                    guardarImagenes(selected.getIcono());
                }
            } else {
                JsfUtil.addErrorMessage("La categoria " + selected.getNombre() + " ya se encuentra creada");
            }
        }
    }

    public void destroy() {
        System.out.println("ENTRE A DESTRUIR");
        if (!getFacade().getLugarByCategoria(selected.getId())) {
            System.out.println("LUGAR NO EXISTE");
            Calendar fecha = Calendar.getInstance();
            selected.setFecha(fecha.getTime());
            selected.setBorrado(1);
            persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("CategoriaDeleted"));
            if (!JsfUtil.isValidationFailed()) {
                selected = null;
                items = null;
                itemsByBorrado = null;
            }
        } else {
            System.out.println("LUGAR EXISTE");
            JsfUtil.addErrorMessage("No es posible borrar la categoria " + selected.getNombre() + " porque esta siendo utilizada por 1 o más lugares");
        }
    }

    public List<Categoria> getItems() {
        if (items == null) {
            items = getFacade().findAll();
        }
        return items;
    }

    public List<Categoria> getItemsByBorrado() {
        if (itemsByBorrado == null) {
            itemsByBorrado = getFacade().getListCategoriasByBorrado(0);
        }
        return itemsByBorrado;
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

    private void guardarImagenes(String nombreImagen) {
        try {
            for (Map.Entry<String, UploadedFile> entrySet : getMapImagenes().entrySet()) {
                String key = entrySet.getKey();
                UploadedFile value = entrySet.getValue();
                String path = ResourceBundle.getBundle("/Bundle").getString("Uploaded") + FILE_SEPARATOR + key + FILE_SEPARATOR + "categoria" + FILE_SEPARATOR + nombreImagen.toLowerCase().replaceAll("\\s", "_");
                System.out.println("PATH: " + path);
                JsfUtil.copyFile(value, path);
            }
        } catch (Exception e) {
            System.out.println("\n\n==================== ERROR GUARDANDO LAS IMAGENES DE LA CATEGORIA ====================");
            e.printStackTrace();
            System.out.println("==================== ERROR GUARDANDO LAS IMAGENES DE LA CATEGORIA ====================\n\n");
        }
    }

    private void llenarMapa() {
        mapImagenes.put("mdpi", getMdpi());
        mapImagenes.put("hdpi", getHdpi());
        mapImagenes.put("xhdpi", getXhdpi());
        mapImagenes.put("xxhdpi", getXxhdpi());
    }

    private boolean createAndUpdate() {
        boolean result = false;
        if (getMdpi() != null && getHdpi() != null && getXhdpi() != null && getXxhdpi() != null) {
            llenarMapa();
            selected.setIcono(selected.getNombre().toLowerCase().replaceAll("\\s", "_") + ".png");
            Calendar fecha = Calendar.getInstance();
            selected.setFecha(fecha.getTime());

//            System.out.println("\n\n========== DATOS CATEGORIA ==========");
//            System.out.println("NOMBRE: " + selected.getNombre());
//            System.out.println("ICONO: " + selected.getIcono());
//            System.out.println("BORRADO: " + selected.getBorrado());
//            System.out.println("FECHA: " + selected.getFecha());
//            System.out.println("MDPI: " + getMdpi().getFileName());
//            System.out.println("HDPI: " + getHdpi().getFileName());
//            System.out.println("XHDPI: " + getXhdpi().getFileName());
//            System.out.println("XXHDPI: " + getXxhdpi().getFileName());
//            System.out.println("========== DATOS CATEGORIA ==========\n\n");

            result = true;
        }
        return result;
    }

}
