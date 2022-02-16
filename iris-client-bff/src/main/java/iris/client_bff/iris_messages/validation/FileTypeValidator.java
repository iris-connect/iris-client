package iris.client_bff.iris_messages.validation;

import iris.client_bff.iris_messages.web.IrisMessageInsertFileDto;
import org.apache.tika.Tika;
import org.springframework.http.MediaType;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Base64;
import java.util.Objects;

public class FileTypeValidator implements ConstraintValidator<FileTypeConstraint, IrisMessageInsertFileDto> {

    //@todo: move ALLOWED_TYPES to a better place
    public static final String[] ALLOWED_TYPES = {"image/*"};

    @Override
    public void initialize(FileTypeConstraint constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(IrisMessageInsertFileDto file, ConstraintValidatorContext ctx) {
        if (file == null) return true;
        try {
            Tika tika = new Tika();
            String type = tika.detect(Base64.getDecoder().decode(file.getContent()));
            MediaType mediaType = MediaType.valueOf(type);
            for (String allowedType : ALLOWED_TYPES) {
                String[] typeMap = allowedType.split("/");
                if (typeMap.length != 2) return false;
                if (mediaType.getType().equals(typeMap[0])) {
                    if (Objects.equals(typeMap[1], "*")) return true;
                    if (mediaType.getSubtype().equals(typeMap[1])) return true;
                }
            }
        } catch (Throwable e) {
            return false;
        }
        return false;
    }
}
