package r2r.persistencia.controllers;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import r2r.persistencia.entidades.Foto;
import r2r.util.JsfUtil;
import r2r.util.JsfUtil.PersistAction;
import r2r.persistencia.facades.FotoFacade;
import java.io.Serializable;
import java.nio.file.Files;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;
import r2r.persistencia.entidades.Lugar;

@ManagedBean(name = "fotoController")
@SessionScoped
public class FotoController implements Serializable {

    @EJB
    private FotoFacade ejbFacade;
    private List<Foto> items = null, itemsGroupByLugar = null;
    private Foto selected;
    private List<String> nomFotos = null;
    private final List<String> fotosTMP;
    private List<UploadedFile> listFotos;
    private Lugar lugar;
    private final String ruta;
    private static final String FILE_SEPARATOR = System.getProperty("file.separator");

    public FotoController() {
        listFotos = new ArrayList<>();
        fotosTMP = new ArrayList<>();
        lugar = new Lugar();
        ruta = ResourceBundle.getBundle("/Bundle").getString("Uploaded") + FILE_SEPARATOR + "lugar" + FILE_SEPARATOR;
    }

    public Foto getSelected() {
        return selected;
    }

    public void setSelected(Foto selected) {
        this.selected = selected;
    }

    protected void setEmbeddableKeys() {
    }

    protected void initializeEmbeddableKey() {
    }

    public List<UploadedFile> getListFotos() {
        return listFotos;
    }

    public void setListFotos(List<UploadedFile> listFotos) {
        this.listFotos = listFotos;
    }

    private FotoFacade getFacade() {
        return ejbFacade;
    }

    public Lugar getLugar() {
        return lugar;
    }

    public void setLugar(Lugar lugar) {
        this.lugar = lugar;
    }

    public List<String> getFotosTMP() {
        return fotosTMP;
    }

    public Foto prepareCreate() {
        selected = new Foto();
        initializeEmbeddableKey();
        return selected;
    }

    public void setLugar2(String lugar) {
        Lugar l = getFacade().getLugarByNombre(lugar);
        setLugar(l);
    }

    public void create() {
        if (getListFotos().size() > 0) {
            guardarAdjuntos(selected.getLugar().getNombre());
            selected.setBorrado(0);
            Calendar fecha = Calendar.getInstance();
            selected.setFecha(fecha.getTime());
            if (getFacade().add(selected, getFotosTMP())) {
                getListFotos().clear();
                getFotosTMP().clear();
                JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("FotoCreated"));
            } else {
                for (String f : getFotosTMP()) {
                    File img = new File(ruta + f);
                    if (img.exists()) {
                        if (img.delete()) {
                            System.out.println("La imagen " + f + " se a borrado satisfactoriamente");
                        } else {
                            System.out.println("La imagen " + f + " no se pudo borrar");
                        }
                    } else {
                        System.out.println("La imagen " + f + " no existe");
                    }
                }
            }
        } else {
            JsfUtil.addErrorMessage("Debe tener al menos una imagen agregada");
        }

//        persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("FotoCreated"));
        if (!JsfUtil.isValidationFailed()) {
            items = null;    // Invalidate list of items to trigger re-query.
            itemsGroupByLugar = null;
        }
    }

//    public void update() {
//        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("FotoUpdated"));
//    }
    public void destroy() {
        persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("FotoDeleted"));
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
            itemsGroupByLugar = null;
        }
    }

    public List<Foto> getItems() {
        if (items == null) {
            items = getFacade().findAll();
        }
        return items;
    }

    public List<Foto> getItemsGroupByLugar() {
        if (itemsGroupByLugar == null) {
            itemsGroupByLugar = getFacade().getFotosGroupByLugar();
        }
        return itemsGroupByLugar;
    }

    public List<String> getNomFotos() {
        nomFotos = null;
        if (getLugar() != null) {
            nomFotos = getFacade().getFotosByLugar(getLugar());
        }
        return nomFotos;
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

    public List<Foto> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<Foto> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }

    private String nombreRepetido(String nombre) {
        if (getFotosTMP().contains(nombre)) {
            int corte = nombre.lastIndexOf(".");
            int length = nombre.length();

            String nom = nombre.substring(0, corte);
            String ext = nombre.substring(corte, length);

            nombre = nom + "1" + ext;
            nombre = nombreRepetido(nombre);
        } else {
            getFotosTMP().add(nombre);
        }
        return nombre;
    }

    @FacesConverter(forClass = Foto.class)
    public static class FotoControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            FotoController controller = (FotoController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "fotoController");
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
            if (object instanceof Foto) {
                Foto o = (Foto) object;
                return getStringKey(o.getId());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), Foto.class.getName()});
                return null;
            }
        }

    }

    public void handleFileUpload(FileUploadEvent event) throws IOException {
        UploadedFile arc;
        UploadedFile file = event.getFile();
        if (getListFotos().size() >= 1) {
            for (UploadedFile filesList1 : getListFotos()) {
                arc = filesList1;
                if (arc.getFileName().equals(file.getFileName())) {
                    JsfUtil.addErrorMessage(event.getFile().getFileName() + " ya esta cargado");
                    return;
                }
            }

            if (getListFotos().add(file)) {
                copyFile(file.getFileName(), file.getInputstream());
                JsfUtil.addSuccessMessage(event.getFile().getFileName() + " Esta cargado");
            } else {
                JsfUtil.addErrorMessage(event.getFile().getFileName() + " no se a cargado");
            }

        } else {
            if (getListFotos().add(file)) {
                copyFile(file.getFileName(), file.getInputstream());
                JsfUtil.addSuccessMessage(event.getFile().getFileName() + " Esta cargado");
            }
        }
    }

    public void copyFile(String fileName, InputStream in) {
        try {
            OutputStream out = new FileOutputStream(new File(System.getProperty("java.io.tmpdir") + FILE_SEPARATOR + fileName));
            int read = 0;
            byte[] bytes = new byte[1024];
            while ((read = in.read(bytes)) != -1) {
                out.write(bytes, 0, read);
            }
            in.close();
            out.flush();
            out.close();
            System.out.println("Nueva foto creada");
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public void quitarAdjunto(UploadedFile archivo) {
        listFotos.remove(archivo);
    }

    public static String fileRename(String fileName, String lugar) {
        int corte = fileName.lastIndexOf(".");
        int length = fileName.length();

        String Extention = fileName.substring(corte, length);

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd.HH-mm-ss.SSS");
        Date date = new Date();

        String newName = lugar + "." + dateFormat.format(date) + Extention;

        System.out.println("=======New name=========");
        System.out.println("New name: " + newName);
        System.out.println("=======New name=========");

        return newName;
    }

    private void guardarAdjuntos(String lugar) {
        try {
            lugar = lugar.toLowerCase().replaceAll("\\s", "_");
            for (UploadedFile file : listFotos) {
                String nuevoNombre = fileRename(file.getFileName(), lugar);
                String r = ruta + nombreRepetido(nuevoNombre);
                File dirDestino = new File(r);

                File dirOrigen = new File(System.getProperty("java.io.tmpdir") + File.separator + file.getFileName());
                Files.move(dirOrigen.toPath(), dirDestino.toPath());
            }
        } catch (Exception e) {
            System.out.println("==========ERROR==========");
            e.printStackTrace();
            System.out.println("==========ERROR==========");
        }
    }

}
