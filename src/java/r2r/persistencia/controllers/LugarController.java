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
import javax.faces.bean.ViewScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import org.primefaces.event.map.MarkerDragEvent;
import org.primefaces.model.map.DefaultMapModel;
import org.primefaces.model.map.LatLng;
import org.primefaces.model.map.MapModel;
import org.primefaces.model.map.Marker;
import r2r.persistencia.entidades.Categoria;

@ManagedBean(name = "lugarController")
@ViewScoped
public class LugarController implements Serializable {

    private MapModel draggableModel;
    private Marker marker;
    private LatLng coordenadaPrincipal;
    private double latitud = 0, longitud = 0;
    private boolean arrastrarMarker;

    public MapModel getDraggableModel() {
        return draggableModel;
    }

    public void onMarkerDrag(MarkerDragEvent event) {
        marker = event.getMarker();
        JsfUtil.addSuccessMessage("Marcador fijado\n Lat:" + marker.getLatlng().getLat() + ", \nLng:" + marker.getLatlng().getLng());
    }

    @EJB
    private LugarFacade lugarFacade;
    private List<Lugar> items = null, itemsByBorrado = null;
    private Lugar lugar;

    public LugarController() {
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

    public Lugar prepareCreate() {
        arrastrarMarker = true;
        lugar = new Lugar();
        latitud = 4.410836;
        longitud = -76.153943;
        agregarMarcador(latitud, longitud);
        initializeEmbeddableKey();
        return lugar;
    }

    public void getCoordenadas(boolean arrastrarMarker) {
        this.arrastrarMarker = arrastrarMarker;
        if (lugar != null) {
            latitud = lugar.getLatitud();
            longitud = lugar.getLongitud();
        } else {
            latitud = 4.410836;
            longitud = -76.153943;
        }
        agregarMarcador(latitud, longitud);
    }

    public void create() {
        lugar.setBorrado(0);
        Calendar fecha = Calendar.getInstance();
        lugar.setFecha(fecha.getTime());
        lugar.setPuntaje(0);
        lugar.setLongitud(marker.getLatlng().getLng());
        lugar.setLatitud(marker.getLatlng().getLat());

        persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("LugarCreated"));
        if (!JsfUtil.isValidationFailed()) {
            items = null;
            itemsByBorrado = null;
        }
    }

    public void update() {
        Calendar fecha = Calendar.getInstance();
        lugar.setFecha(fecha.getTime());
        lugar.setLongitud(marker.getLatlng().getLng());
        lugar.setLatitud(marker.getLatlng().getLat());
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
            itemsByBorrado = null;
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

    private void agregarMarcador(double lat, double lng) {
        draggableModel = new DefaultMapModel();
        coordenadaPrincipal = new LatLng(lat, lng);
        draggableModel.addOverlay(new Marker(coordenadaPrincipal, "Coordenadas"));
        for (Marker premarker : draggableModel.getMarkers()) {
            premarker.setDraggable(arrastrarMarker);
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
    
    public String getDescripcion(String desc){
        if (desc.length() >  76 ) {
            return desc.substring(0, 76) + "...";
        }else{
            return desc;
        }
    }

}
