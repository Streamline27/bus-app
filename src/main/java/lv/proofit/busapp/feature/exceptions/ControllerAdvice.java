package lv.proofit.busapp.feature.exceptions;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lv.proofit.busapp.api.exceptions.ExceptionResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class ControllerAdvice {

    private final NotValidExceptionMapper exceptionMapper;

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(Exception.class)
    public ExceptionResponse handle(Exception ex) {
        log.error("Error during request processing.", ex);
        return ExceptionResponse.with(ex.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ExceptionResponse handle(MethodArgumentNotValidException ex) {
        log.error("Request validation failed.");
        return exceptionMapper.map(ex);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({MethodArgumentTypeMismatchException.class})
    public ExceptionResponse handle(MethodArgumentTypeMismatchException ex) {
        String message = "Request validation failed. Could not convert:["+ ex.getValue() +"]. Parameter name:["+ ex.getName() +"]";
        log.error(message);
        return ExceptionResponse.with(message);
    }
}
