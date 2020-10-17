package lv.proofit.busapp.api.draft.price.response;

import lombok.Builder;
import lombok.Value;
import lv.proofit.busapp.api.draft.price.request.Passenger;

import java.math.BigDecimal;

@Value
@Builder
public class PassengerPrice {

    Passenger passenger;
    BigDecimal ticketPrice;
    BigDecimal luggagePrice;

    public BigDecimal getTotalPrice() {
        return ticketPrice.add(luggagePrice);
    }
}
