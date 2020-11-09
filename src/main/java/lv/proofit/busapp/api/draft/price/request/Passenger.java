package lv.proofit.busapp.api.draft.price.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Value;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

@Value
@Builder
public class Passenger {

    @Schema(example = "Vladislav")
    @NotNull
    String name;

    @Schema(example = "2")
    @PositiveOrZero
    int numberOfBags;

    @NotNull
    PassengerAge age;

    public double getDiscountPercent() {
        return this.age.getDiscountPercent();
    }
}
