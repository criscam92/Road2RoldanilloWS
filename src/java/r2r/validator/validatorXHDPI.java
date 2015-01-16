package r2r.validator;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import org.primefaces.model.UploadedFile;
import r2r.util.JsfUtil;

@FacesValidator("validatorXHDPI")
public class validatorXHDPI implements Validator {

    private final int WIDTH = 72;
    private final int HEIGHT = 96;

    @Override
    public void validate(FacesContext context, UIComponent component, Object value) {
        JsfUtil.validarImagen(WIDTH, HEIGHT, (UploadedFile) value, "XHDPI");
    }

}
