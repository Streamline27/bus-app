package lv.proofit.busapp.api.draft.price.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Value;
import lombok.experimental.FieldNameConstants;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Value
@Builder
@FieldNameConstants
public class DraftPriceRequest {

    @Schema(example = "Vilnus, Lithuania")
    @NotBlank
    String destinationTerminalName;

    @NotNull
    List<Passenger> passengers;
}
