package lv.proofit.busapp.feature.tax.rates;

import lombok.extern.slf4j.Slf4j;
import lv.proofit.busapp.api.tax.rates.TaxRatesResponse;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;

@Slf4j
@Service
public class TaxRatesService {
    public TaxRatesResponse getRatesAt(LocalDate date) {
        log.info("Tax rates: Requested rates at:[{}]", date);
        return TaxRatesResponse.builder()
                .taxRate(new BigDecimal(21))
                .build();
    }
}
