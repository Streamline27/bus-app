package lv.proofit.busapp.api.draft.price.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
public class DraftPriceResponse {

    @Schema(example = "Vilnius, Lithuania")
    String destinationTerminalName;
    List<PassengerPrice> prices;
    double total;
}
