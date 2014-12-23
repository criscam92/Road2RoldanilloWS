package r2r.persistencia.controllers;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import r2r.persistencia.entidades.Categoria;
import r2r.util.JsfUtil;
import r2r.util.JsfUtil.PersistAction;
import r2r.persistencia.facades.CategoriaFacade;
import java.io.Serializable;
import java.util.List;
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
import org.apache.commons.io.FilenameUtils;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

@ManagedBean(name = "categoriaController")
@SessionScoped
public class CategoriaController implements Serializable {

    @EJB
    private CategoriaFacade categoriaFacade;
    private List<Categoria> items = null;
    private Categoria selected;
    private boolean hidden = false;
    private UploadedFile img48, img72, img96, img180;
    private List<UploadedFile> ListImgsTMP;

    public CategoriaController() {
    }

    public Categoria getSelected() {
        return selected;
    }

    public void setSelected(Categoria selected) {
        this.selected = selected;
    }

    public UploadedFile getImg48() {
        return img48;
    }

    public boolean isHidden() {
        return hidden;
    }

    public void setHidden(boolean hidden) {
        this.hidden = hidden;
    }

    public void setImg48(UploadedFile img48) {
        this.img48 = img48;
    }

    public UploadedFile getImg72() {
        return img72;
    }

    public void setImg72(UploadedFile img72) {
        this.img72 = img72;
    }

    public UploadedFile getImg96() {
        return img96;
    }

    public void setImg96(UploadedFile img96) {
        this.img96 = img96;
    }

    public UploadedFile getImg180() {
        return img180;
    }

    public void setImg180(UploadedFile img180) {
        this.img180 = img180;
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
        persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("CategoriaCreated"));
        if (!JsfUtil.isValidationFailed()) {
            items = null;    // Invalidate list of items to trigger re-query.
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

    public void handleFileUpload48X48(FileUploadEvent event) {
        System.out.println("Entre Evento 48");
        addImg(event, 48, img48);
    }

    public void handleFileUpload72X72(FileUploadEvent event) {
        System.out.println("Entre Evento 72");
        addImg(event, 72, img72);
    }

    public void handleFileUpload96X96(FileUploadEvent event) {
        System.out.println("Entre Evento 96");
        addImg(event, 96, img96);
    }

    public void handleFileUpload180X180(FileUploadEvent event) {
        System.out.println("Entre Evento 180");
        addImg(event, 180, img180);
    }

    private void addImg(FileUploadEvent event, int tamañoImg, UploadedFile img) {
        setHidden(false);
        FacesMessage msg = null;
        img = event.getFile();
        System.out.println("Archivo " + img.getFileName());

        if (!isValidImg(tamañoImg, img)) {
            msg = new FacesMessage(FacesMessage.SEVERITY_WARN, "Error", "La imagen debe tener un tamaño de " + tamañoImg + "x" + tamañoImg + " Píxeles");
        } else {
            if (!ListImgsTMP.add(img)) {
                msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Ocurrio un error agregando la imagen");
            } else {
                setHidden(true);
            }
        }

        FacesContext.getCurrentInstance().addMessage(null, msg);
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

}
