package lv.proofit.busapp.feature.draft.price;

import lv.proofit.busapp.Constants;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class PriceUtils {

    public static double computeLuggagePrice(double basePrice, double taxRate, int numberOfBags) {
        return computePrice(basePrice, Constants.LUGGAGE_DISCOUNT_PERCENT, taxRate) * numberOfBags;
    }

    public static double computePrice(double basePrice, double discountPercent, double taxPercent) {
        double taxMultiplier = (taxPercent + 100) / 100;
        double discountMultiplier = (100 - discountPercent) / 100;
        double rawPrice = basePrice * taxMultiplier * discountMultiplier;
        return BigDecimal.valueOf(rawPrice).setScale(2, RoundingMode.HALF_DOWN).doubleValue();
    }
}
