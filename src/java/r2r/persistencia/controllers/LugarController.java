package r2r.persistencia.controllers;

import r2r.persistencia.entidades.Lugar;
import r2r.util.JsfUtil;
import r2r.util.JsfUtil.PersistAction;
import r2r.persistencia.facades.LugarFacade;

import java.io.Serializable;
import java.util.Calendar;
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
import org.primefaces.event.map.MarkerDragEvent;
import org.primefaces.model.map.DefaultMapModel;
import org.primefaces.model.map.LatLng;
import org.primefaces.model.map.MapModel;
import org.primefaces.model.map.Marker;

@ManagedBean(name = "lugarController")
@SessionScoped
public class LugarController implements Serializable {

    private MapModel draggableModel;
    private Marker marker;
    private LatLng coordenadaPrincipal;

    public MapModel getDraggableModel() {
        return draggableModel;
    }

    public void onMarkerDrag(MarkerDragEvent event) {
        marker = event.getMarker();
        JsfUtil.addSuccessMessage("Marcador fijado\n Lat:" + marker.getLatlng().getLat() + ", Lng:" + marker.getLatlng().getLng());
    }

    @EJB
    private LugarFacade lugarFacade;
    private List<Lugar> items = null, itemsByBorrado = null;
    private Lugar lugar;

    public LugarController() {
        agregarMarcador();
    }

    public Lugar getLugar() {
        return lugar;
    }

    public void setLugar(Lugar lugar) {
        this.lugar = lugar;
    }

    protected void setEmbeddableKeys() {
    }

    protected void initializeEmbeddableKey() {
    }

    private LugarFacade getFacade() {
        return lugarFacade;
    }

    public Double getPuntaje(Integer id) {
        return getFacade().getPuntaje(id);
    }

    public Lugar prepareCreate() {
        lugar = new Lugar();
        initializeEmbeddableKey();
        return lugar;
    }

    public void create() {
        lugar.setBorrado(0);
        Calendar fecha = Calendar.getInstance();
        lugar.setFecha(fecha.getTime());
        lugar.setPuntaje(0.0F);
        persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("LugarCreated"));
        if (!JsfUtil.isValidationFailed()) {
            items = null;
        }
    }

    public void update() {
        Calendar fecha = Calendar.getInstance();
        lugar.setFecha(fecha.getTime());
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("LugarUpdated"));
    }

    public void destroy() {
        Calendar fecha = Calendar.getInstance();
        lugar.setFecha(fecha.getTime());
        lugar.setBorrado(1);
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("LugarDeleted"));
        if (!JsfUtil.isValidationFailed()) {
            lugar = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public List<Lugar> getItems() {
        if (items == null) {
            items = getFacade().findAll();
        }
        return items;
    }

    public List<Lugar> getItemsByBorrado() {
        if (itemsByBorrado == null) {
            itemsByBorrado = getFacade().getListLugarByBorrado(0);
        }
        return itemsByBorrado;
    }

    private void persist(PersistAction persistAction, String successMessage) {
        if (lugar != null) {
            setEmbeddableKeys();
            try {
                if (persistAction != PersistAction.DELETE) {
                    getFacade().edit(lugar);
                } else {
                    getFacade().remove(lugar);
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

    public List<Lugar> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<Lugar> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }

    private void agregarMarcador() {
        draggableModel = new DefaultMapModel();
        coordenadaPrincipal = new LatLng(4.410836, -76.153943);
        draggableModel.addOverlay(new Marker(coordenadaPrincipal, "Coordenadas"));
        for (Marker premarker : draggableModel.getMarkers()) {
            premarker.setDraggable(true);
        }
    }

    @FacesConverter(forClass = Lugar.class)
    public static class LugarControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            LugarController controller = (LugarController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "lugarController");
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
            if (object instanceof Lugar) {
                Lugar o = (Lugar) object;
                return getStringKey(o.getId());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), Lugar.class.getName()});
                return null;
            }
        }

    }

}
