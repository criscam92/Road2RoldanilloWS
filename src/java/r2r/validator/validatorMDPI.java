package r2r.validator;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
import org.primefaces.model.UploadedFile;
import r2r.util.JsfUtil;

@FacesValidator("validatorMDPI")
public class validatorMDPI implements Validator {

    private final int WIDTH = 36;
    private final int HEIGHT = 48;

    @Override
    public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {

        if (!((UploadedFile) value).getFileName().equals("")) {
            System.out.println("NOT NULL MDPI");
            if (!JsfUtil.isValidImg(WIDTH, HEIGHT, (UploadedFile) value)) {
                JsfUtil.addErrorMessage("La imagen del campo MDPI debe tener un tamaño de " + WIDTH + "x" + HEIGHT + " Píxeles");
//                throw new ValidatorException(JsfUtil.getLastMessege());
            }
        } else {
            System.out.println("NULL MDPI");
            JsfUtil.addErrorMessage("El campo MDPI es requerido");
//            throw new ValidatorException(JsfUtil.getLastMessege());
        }
    }

}
