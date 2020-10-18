package lv.proofit.busapp.api.draft.price.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.List;

@Data
@Builder
public class DraftPriceResponse {

    @Schema(example = "Vilnius, Lithuania")
    String destinationTerminalName;
    List<PassengerPrice> prices;
    double total;
}
