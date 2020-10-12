package lv.proofit.busapp.api.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import lombok.Value;

import java.math.BigDecimal;

@Value
@Builder
public class Passenger {

    @Schema(example = "Vladislav")
    String name;

    @Schema(example = "2")
    BigDecimal numberOfBags;
    PassengerAge age;
}
