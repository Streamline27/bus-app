package lv.proofit.busapp.api.response;

import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.Builder;
import lombok.Data;
import lombok.Value;

import java.math.BigDecimal;
import java.util.List;

@Value
@Builder
public class DraftPriceResponse {

    @Schema(example = "Vilnus, Lithuania")
    String destinationTerminalName;
    List<PassengerPrice> prices;
    BigDecimal total;
}
