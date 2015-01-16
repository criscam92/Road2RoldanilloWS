package r2r.validator;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import org.primefaces.model.UploadedFile;
import r2r.util.JsfUtil;

@FacesValidator("validatorMDPI")
public class validatorMDPI implements Validator {

    private final int WIDTH = 36;
    private final int HEIGHT = 48;

    @Override
    public void validate(FacesContext context, UIComponent component, Object value) {
        JsfUtil.validarImagen(WIDTH, HEIGHT, (UploadedFile) value, "MDPI");
    }

}
