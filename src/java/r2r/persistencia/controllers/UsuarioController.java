package r2r.persistencia.controllers;

import r2r.persistencia.facades.UsuarioFacade;
import java.io.Serializable;
import java.util.List;
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
import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;
import r2r.persistencia.entidades.Usuario;
import r2r.seguridad.Encrypt;
import r2r.util.JsfUtil;
import r2r.util.TipoUsuario;

@ManagedBean(name = "usuarioController")
@SessionScoped
public class UsuarioController implements Serializable {

    @EJB
    private UsuarioFacade ejbFacade;
    private List<Usuario> items = null;
    private Usuario selected;
    private String pass1, pass2;
    private String passTMP;
    private final String uiError = "ui-state-error";
    private String errorPass;
    private String errorUser;
    private boolean disable;

    public UsuarioController() {
        selected = new Usuario();
        disable = true;
    }

    public Usuario getSelected() {
        return selected;
    }

    public void setSelected(Usuario selected) {
        this.selected = selected;
    }

    public String getErrorPass() {
        return errorPass;
    }

    public void setErrorPass(String errorPass) {
        this.errorPass = errorPass;
    }

    public String getErrorUser() {
        return errorUser;
    }

    public void setErrorUser(String errorUser) {
        this.errorUser = errorUser;
    }

    public boolean isDisable() {
        return disable;
    }

    public void setDisable(boolean disable) {
        this.disable = disable;
    }

    public String getPass1() {
        return pass1;
    }

    public void setPass1(String pass1) {
        this.pass1 = pass1;
    }

    public String getPass2() {
        return pass2;
    }

    public void setPass2(String pass2) {
        this.pass2 = pass2;
    }

    protected void setEmbeddableKeys() {
    }

    protected void initializeEmbeddableKey() {
    }

    private UsuarioFacade getFacade() {
        return ejbFacade;
    }

    public Usuario prepareCreate() {
        selected = new Usuario();
        initializeEmbeddableKey();
        return selected;
    }

    public void preparateEdit() {
        passTMP = selected.getContrasena();
        System.out.println("passTMP: " + passTMP);
    }

    public void create() {
        if (!getFacade().getUsuarioByNombre(selected)) {
            if (pass1.equals(pass2)) {
                selected.setContrasena(Encrypt.getStringMessageDigest(pass1));

                persist(JsfUtil.PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("UsuarioCreated"));
                if (!JsfUtil.isValidationFailed()) {
                    items = null;
                    getUIError(3);
                    setDisable(true);
                    RequestContext.getCurrentInstance().execute("PF('UsuarioCreateDialog').hide()");
                }
            } else {
                getUIError(2);
                JsfUtil.addErrorMessage("Las contraseñas no coinciden");
            }
        } else {
            getUIError(1);
            JsfUtil.addErrorMessage("El usuario " + selected.getUsuario() + " ya esta registrado");
        }
    }

    public void update() {
        if (!getFacade().getUsuarioByNombre(selected)) {
            if (!pass1.trim().isEmpty() && !pass2.trim().isEmpty()) {
                if (pass1.equals(pass2)) {
                    getUIError(2);
                    selected.setContrasena(Encrypt.getStringMessageDigest(pass1));
                } else {
                    JsfUtil.addErrorMessage("Las contraseñas no coinciden");
                    return;
                }
            } else {
                selected.setContrasena(passTMP);
            }

            getUIError(3);
            persist(JsfUtil.PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("UsuarioUpdated"));
            setDisable(false);
            RequestContext.getCurrentInstance().execute("PF('UsuarioEditDialog').hide()");
        } else {
            getUIError(1);
            JsfUtil.addErrorMessage("El usuario " + selected.getUsuario() + " ya esta registrado");
        }
    }

    public void destroy() {
        persist(JsfUtil.PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("UsuarioDeleted"));
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
            setDisable(true);
        }
    }

    public List<Usuario> getItems() {
        if (items == null) {
            items = getFacade().findAll();
        }
        return items;
    }

    private void persist(JsfUtil.PersistAction persistAction, String successMessage) {
        if (selected != null) {
            setEmbeddableKeys();
            try {
                if (persistAction != JsfUtil.PersistAction.DELETE) {
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

    public List<Usuario> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<Usuario> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }

    @FacesConverter(forClass = Usuario.class)
    public static class UsuarioControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            UsuarioController controller = (UsuarioController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "usuarioController");
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
            if (object instanceof Usuario) {
                Usuario o = (Usuario) object;
                return getStringKey(o.getId());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + Usuario.class.getName());
            }
        }

    }

    public String getTipoUsuario(int tipo) {
        return TipoUsuario.getFromValue(tipo).getDetalle();
    }

    public TipoUsuario[] getTipoUsuarios() {
        return TipoUsuario.values();
    }

    /**
     *
     * @param tipo entero que indica el campo que sera marcado como error(1 para
     * el usuario, 2 para la contraseña, 3 limpia los campos marcados como
     * error)
     *
     */
    private void getUIError(int tipo) {
        switch (tipo) {
            case 1:
                setErrorPass("");
                setErrorUser(uiError);
                break;
            case 2:
                setErrorUser("");
                setErrorPass(uiError);
                break;
            case 3:
                setErrorPass("");
                setErrorUser("");
                break;
        }
    }

    public void onRowSelect(SelectEvent event) {
        setDisable(false);
    }

    public void onRowUnselect(UnselectEvent event) {
        setDisable(true);
    }

}
