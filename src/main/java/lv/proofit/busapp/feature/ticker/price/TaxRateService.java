package lv.proofit.busapp.feature.ticker.price;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class TaxRateService {

    public BigDecimal get() {
        return new BigDecimal(21);
    }
}
