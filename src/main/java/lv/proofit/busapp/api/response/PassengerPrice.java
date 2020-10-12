package lv.proofit.busapp.api.response;

import lombok.Builder;
import lombok.Data;
import lombok.Value;
import lv.proofit.busapp.api.request.Passenger;

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
