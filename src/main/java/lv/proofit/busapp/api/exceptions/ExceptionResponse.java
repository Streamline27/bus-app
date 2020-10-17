package lv.proofit.busapp.api.exceptions;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lv.proofit.busapp.feature.exceptions.NotValidExceptionMapper;

import java.util.Set;

@Value
@Builder
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ExceptionResponse {

    @Schema(example = NotValidExceptionMapper.VALIDATION_EXCEPTION_MESSAGE)
    String message;
    Set<ErrorDto> errors;

    public static ExceptionResponse with(String message) {
        return ExceptionResponse.builder()
                .message(message)
                .build();
    }
}
