package r2r.util;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.model.SelectItem;
import javax.imageio.ImageIO;
import org.primefaces.model.UploadedFile;
import r2r.persistencia.controllers.CategoriaController;

public class JsfUtil {

    public static SelectItem[] getSelectItems(List<?> entities, boolean selectOne) {
        int size = selectOne ? entities.size() + 1 : entities.size();
        SelectItem[] items = new SelectItem[size];
        int i = 0;
        if (selectOne) {
            items[0] = new SelectItem("", "---");
            i++;
        }
        for (Object x : entities) {
            items[i++] = new SelectItem(x, x.toString());
        }
        return items;
    }

    public static boolean isValidationFailed() {
        return FacesContext.getCurrentInstance().isValidationFailed();
    }

    public static void addErrorMessage(Exception ex, String defaultMsg) {
        String msg = ex.getLocalizedMessage();
        if (msg != null && msg.length() > 0) {
            addErrorMessage(msg);
        } else {
            addErrorMessage(defaultMsg);
        }
    }

    public static void addErrorMessages(List<String> messages) {
        messages.stream().forEach((message) -> {
            addErrorMessage(message);
        });
    }

    public static void addErrorMessage(String msg) {
        FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, msg);
        FacesContext.getCurrentInstance().addMessage(null, facesMsg);
    }

    public static void addSuccessMessage(String msg) {
        FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_INFO, msg, msg);
        FacesContext.getCurrentInstance().addMessage("successInfo", facesMsg);
    }

    public static String getRequestParameter(String key) {
        return FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get(key);
    }

    public static Object getObjectFromRequestParameter(String requestParameterName, Converter converter, UIComponent component) {
        String theId = JsfUtil.getRequestParameter(requestParameterName);
        return converter.getAsObject(FacesContext.getCurrentInstance(), component, theId);
    }

    public static enum PersistAction {

        CREATE,
        DELETE,
        UPDATE
    }

    public static boolean isValidImg(int width, int height, UploadedFile img) {
        boolean isValidImg = false;
        try {
            BufferedImage bi = ImageIO.read(new ByteArrayInputStream((byte[]) img.getContents()));
            if (width == bi.getWidth() && height == bi.getHeight()) {
                isValidImg = true;
            }
        } catch (Exception e) {
            isValidImg = false;
            e.printStackTrace();
        }
        return isValidImg;
    }

    public static void validarImagen(int WIDTH, int HEIGHT, UploadedFile file, String tNombre) {
        String nomFile = "", ext = "";
        try {
            nomFile = file.getFileName();
            ext = nomFile.substring(nomFile.length() - 4, nomFile.length());
        } catch (Exception e) {
            nomFile = "";
            ext = "";
        }

        if (!nomFile.equals("")) {
            if (!ext.equals(".png")) {
                JsfUtil.addErrorMessage("El tipo de archivo del campo " + tNombre + " debe ser .png");
                CategoriaController.imagenValida = false;
            } else {
                if (!JsfUtil.isValidImg(WIDTH, HEIGHT, file)) {
                    JsfUtil.addErrorMessage("La imagen del campo " + tNombre + " debe tener un tamaño de " + WIDTH + "x" + HEIGHT + " Píxeles");
                    CategoriaController.imagenValida = false;
                }
            }
        } else {
            JsfUtil.addErrorMessage("El campo " + tNombre + " es requerido");
            CategoriaController.imagenValida = false;
        }
    }

}
