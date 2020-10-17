package lv.proofit.busapp.api.draft.price.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Value;
import lv.proofit.busapp.api.draft.price.request.Passenger;

import java.math.BigDecimal;

@Value
@Builder
public class PassengerPrice {

    Passenger passenger;
    double ticketPrice;
    double luggagePrice;

    @JsonIgnore
    public Double getTotalPrice() {
        return ticketPrice + luggagePrice;
    }
}
