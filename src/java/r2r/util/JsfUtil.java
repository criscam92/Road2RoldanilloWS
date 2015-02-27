package r2r.util;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.model.SelectItem;
import javax.imageio.ImageIO;
import org.apache.commons.io.IOUtils;
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
        for (String message : messages) {
            addErrorMessage(message);
        }
    }

    public static void addErrorMessage(String msg) {
        FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, msg);
        FacesContext.getCurrentInstance().addMessage(null, facesMsg);
    }

    public static void addSuccessMessage(String msg) {
        FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_INFO, msg, msg);
        FacesContext.getCurrentInstance().addMessage("successInfo", facesMsg);
    }

    public static void addWarnMessage(String msg) {
        FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_WARN, msg, msg);
        FacesContext.getCurrentInstance().addMessage(null, facesMsg);
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
            BufferedImage bi = ImageIO.read(new ByteArrayInputStream(IOUtils.toByteArray(img.getInputstream())));
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
        System.out.println("VALIDATE");
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

    public static void copyFile(UploadedFile img, String rDestino) throws IOException {
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
            System.out.println("Nuevo archivo creado!");
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public static String removeCaracteresEspeciales(String input) {
        String original = "áàäéèëíìïóòöúùuñçÁÀÄÉÈËÍÌÏÓÒÖÚÙÜÑÇ";
        String ascii = "aaaeeeiiiooouuuncAAAEEEIIIOOOUUUNC";
        String output = input;
        for (int i = 0; i < original.length(); i++) {
            output = output.replace(original.charAt(i), ascii.charAt(i));
        }
        return simbolos(output);
    }

    public static String simbolos(String input) {
        String[] simbolos = new String[]{"¨", "º", "-", "~", "#", "@", "|",
            "!", "\\", "·", "$", "%", "&", "/", "(", ")", "?", "'", "¡", "¿",
            "[", "^", "`", "]", "+", "}", "{", "¨", "´", ">", "< ", ";", ",",
            ":", "."};

        String output = input;
        for (String s : simbolos) {
            System.out.println("SIMBOLO ELIMINADO: " + s);
            output = output.replace("" + s, "");
        }

        return output;
    }

}
