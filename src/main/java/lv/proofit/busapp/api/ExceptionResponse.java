package lv.proofit.busapp.api;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Value;

@Value
@AllArgsConstructor
public class ExceptionResponse {

    public String message;
}
