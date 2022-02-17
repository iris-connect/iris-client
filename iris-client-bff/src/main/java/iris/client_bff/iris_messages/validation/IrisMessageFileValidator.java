package iris.client_bff.iris_messages.validation;

import iris.client_bff.core.utils.ValidationHelper;
import iris.client_bff.iris_messages.web.IrisMessageInsertFileDto;
import lombok.RequiredArgsConstructor;
import org.apache.tika.Tika;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Base64;

@Component
@RequiredArgsConstructor
public class IrisMessageFileValidator implements ConstraintValidator<IrisMessageFileConstraint, IrisMessageInsertFileDto> {

    private final ValidationHelper validationHelper;

    @Override
    public boolean isValid(IrisMessageInsertFileDto file, ConstraintValidatorContext ctx) {
        if (file == null) return true;
        try {
            Tika tika = new Tika();
            String type = tika.detect(Base64.getDecoder().decode(file.getContent()));
            MediaType.valueOf(type);
        } catch (Throwable e) {
            return false;
        }
        if (validationHelper.isPossibleAttack(file.getName(), "fileName", false)) {
            return false;
        }
        return file.getName() != null;
    }

}