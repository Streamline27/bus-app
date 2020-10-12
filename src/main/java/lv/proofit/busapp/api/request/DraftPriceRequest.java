package lv.proofit.busapp.api.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import lombok.Value;

import java.util.List;

@Value
@Builder
public class DraftPriceRequest {

    @Schema(example = "Vilnus, Lithuania")
    String destinationTerminalName;
    List<Passenger> passengers;
}
