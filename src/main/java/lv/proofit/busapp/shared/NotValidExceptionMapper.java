package lv.proofit.busapp.shared;

import lv.proofit.busapp.api.exceptions.ErrorDto;
import lv.proofit.busapp.api.exceptions.ExceptionResponse;
import org.springframework.stereotype.Component;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.Set;
import java.util.stream.Collectors;

@Component
public class NotValidExceptionMapper {

    public static final String VALIDATION_EXCEPTION_MESSAGE = "Request validation failed!";

    public ExceptionResponse map(MethodArgumentTypeMismatchException ex) {
        return ExceptionResponse.builder()
                .message("Failed to convert:["+ ex.getValue() +"]. Parameter name:["+ ex.getName() +"]")
                .build();
    }

    public ExceptionResponse map(MethodArgumentNotValidException ex) {
        return ExceptionResponse.builder()
                .message(VALIDATION_EXCEPTION_MESSAGE)
                .errors(mapErrors(ex))
                .build();
    }

    public Set<ErrorDto> mapErrors(MethodArgumentNotValidException ex) {
        return ex.getBindingResult().getFieldErrors()
                .stream()
                .map(this::map)
                .collect(Collectors.toSet());
    }

    public ErrorDto map(FieldError error) {
        return ErrorDto.builder()
                .fieldName(error.getField())
                .objectName(error.getObjectName())
                .message(error.getDefaultMessage())
                .build();
    }
}
