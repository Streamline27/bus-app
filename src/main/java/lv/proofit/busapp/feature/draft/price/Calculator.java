package lv.proofit.busapp.feature.draft.price;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class Calculator {

    private static final BigDecimal HUNDRED = new BigDecimal(100);

    private final BigDecimal basePrice;

    public Calculator(BigDecimal basePrice) {
        this.basePrice = basePrice;
    }

    public BigDecimal computePrice(BigDecimal discountPercent, BigDecimal taxPercent) {
        BigDecimal discountMultiplier = HUNDRED.add(discountPercent.negate()).divide(HUNDRED, 2, RoundingMode.HALF_UP);
        BigDecimal taxMultiplier = taxPercent.add(HUNDRED).divide(HUNDRED, 2, RoundingMode.HALF_UP);
        return this.basePrice.multiply(discountMultiplier).multiply(taxMultiplier);
    }
}
