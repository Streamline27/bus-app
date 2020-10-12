package lv.proofit.busapp.api.request;

import lombok.Getter;

import java.math.BigDecimal;

@Getter
public enum PassengerAge {

    ADULT(0),
    CHILD(50);

    private final BigDecimal discountPercent;

    PassengerAge(int discount) {
        this.discountPercent = new BigDecimal(discount);
    }
}
