package lv.proofit.busapp.feature.draft.price;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class Calculator {

    public static double computePrice(double basePrice, double discountPercent, double taxPercent) {
        double taxMultiplier = (taxPercent + 100) / 100;
        double discountMultiplier = (100 - discountPercent) / 100;
        double rawPrice = basePrice * taxMultiplier * discountMultiplier;
        return BigDecimal.valueOf(rawPrice).setScale(2, RoundingMode.HALF_DOWN).doubleValue();
    }
}
