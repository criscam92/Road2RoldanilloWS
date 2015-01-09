package r2r.validator;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
import org.primefaces.model.UploadedFile;
import r2r.util.JsfUtil;

@FacesValidator("validatorXHDPI")
public class validatorXHDPI implements Validator {

    private final int WIDTH = 72;
    private final int HEIGHT = 96;

    @Override
    public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {

        if (!((UploadedFile) value).getFileName().equals("")) {
            System.out.println("NOT NULL XHDPI");
            if (!JsfUtil.isValidImg(WIDTH, HEIGHT, (UploadedFile) value)) {
                JsfUtil.addErrorMessage("La imagen del campo XHDPI debe tener un tamaño de " + WIDTH + "x" + HEIGHT + " Píxeles");
//                throw new ValidatorException(JsfUtil.getLastMessege());
            }
        } else {
            System.out.println("NULL XHDPI");
            JsfUtil.addErrorMessage("El campo XHDPI es requerido");
//            throw new ValidatorException(JsfUtil.getLastMessege());
        }
    }

}
