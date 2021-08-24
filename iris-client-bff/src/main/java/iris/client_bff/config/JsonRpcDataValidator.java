package iris.client_bff.config;

import iris.client_bff.ui.messages.ErrorMessages;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.Set;
import java.util.stream.Collectors;


@Configuration
@RequiredArgsConstructor
public class JsonRpcDataValidator {

    private final Validator validator;

    public void validateData(Object data) {
        Set<ConstraintViolation<Object>> constraintViolations = validator.validate(data);
        if (!constraintViolations.isEmpty())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    ErrorMessages.INVALID_INPUT + ": " + constraintViolations.stream().map(violation -> String.format("%s: %s", violation.getPropertyPath(), violation.getMessage())).collect(Collectors.joining(", "))
            );
    }

}
