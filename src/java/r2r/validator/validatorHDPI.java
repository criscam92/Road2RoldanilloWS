package r2r.validator;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
import org.primefaces.model.UploadedFile;
import r2r.util.JsfUtil;

@FacesValidator("validatorHDPI")
public class validatorHDPI implements Validator {

    private final int WIDTH = 54;
    private final int HEIGHT = 72;   

    @Override
    public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {

        if (!((UploadedFile) value).getFileName().equals("")) {
            if (!JsfUtil.isValidImg(WIDTH, HEIGHT, (UploadedFile) value)) {
                JsfUtil.addErrorMessage("La imagen del campo HDPI debe tener un tamaño de " + WIDTH + "x" + HEIGHT + " Píxeles");
            }
        } else {
            JsfUtil.addErrorMessage("El campo HDPI es requerido");
        }
    }

}
