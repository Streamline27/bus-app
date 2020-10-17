package lv.proofit.busapp.api.draft.price.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
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
