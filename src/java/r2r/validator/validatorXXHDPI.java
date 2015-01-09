package r2r.validator;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
import org.primefaces.model.UploadedFile;
import r2r.util.JsfUtil;

@FacesValidator("validatorXXHDPI")
public class validatorXXHDPI implements Validator {

    private final int WIDTH = 108;
    private final int HEIGHT = 144;

    @Override
    public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
        JsfUtil.validarImagen(WIDTH, HEIGHT, (UploadedFile) value, "XXHDPI");
    }

}
