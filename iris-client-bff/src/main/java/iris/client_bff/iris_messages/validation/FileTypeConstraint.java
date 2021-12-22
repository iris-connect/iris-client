package iris.client_bff.iris_messages.validation;

import iris.client_bff.ui.messages.ErrorMessages;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = FileTypeValidator.class)
@Target( { ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.TYPE_USE })
@Retention(RetentionPolicy.RUNTIME)
public @interface FileTypeConstraint {
    String message() default ErrorMessages.INVALID_FILE_TYPE;
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
