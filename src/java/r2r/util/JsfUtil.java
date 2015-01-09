package r2r.util;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.model.SelectItem;
import javax.imageio.ImageIO;
import org.primefaces.model.UploadedFile;

public class JsfUtil {

//    private static FacesMessage lastMessege;
//
//    public static FacesMessage getLastMessege() {
//        return lastMessege;
//    }
//
//    public static void setLastMessege(FacesMessage lastMessege) {
//        JsfUtil.lastMessege = lastMessege;
//    }

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
//        setLastMessege(facesMsg);
    }

    public static void addSuccessMessage(String msg) {
        FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_INFO, msg, msg);
        FacesContext.getCurrentInstance().addMessage("successInfo", facesMsg);
//        setLastMessege(facesMsg);
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

    public static String fileRename(UploadedFile file) {
        return fileRename(file.getFileName());
    }

    public static String fileRename(String fileName) {
        int corte = fileName.lastIndexOf(".");
        int length = fileName.length();

        String Name = fileName.substring(0, corte);
        String Extention = fileName.substring(corte, length);

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd.HH-mm-ss");
        Date date = new Date();

        String newName = Name + "." + dateFormat.format(date) + Extention;

        System.out.println("=======New name=========");
        System.out.println("New name: " + newName);
        System.out.println("=======New name=========");

        return newName;
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

}
