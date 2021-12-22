package iris.client_bff.iris_messages.validation;

import iris.client_bff.core.utils.ValidationHelper;
import lombok.RequiredArgsConstructor;
import org.apache.tika.Tika;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@Component
@RequiredArgsConstructor
public class IrisMessageFileValidator implements ConstraintValidator<IrisMessageFileConstraint, MultipartFile> {

    private final ValidationHelper validationHelper;

    @Override
    public boolean isValid(MultipartFile file, ConstraintValidatorContext ctx) {
        if (file == null) return true;
        try {
            Tika tika = new Tika();
            String type = tika.detect(file.getInputStream(), file.getOriginalFilename());
            MediaType.valueOf(type);
        } catch (Throwable e) {
            return false;
        }
        if (validationHelper.isPossibleAttack(file.getOriginalFilename(), "fileName", false)) {
            return false;
        }
        return file.getOriginalFilename() != null;
    }

}