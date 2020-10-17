package lv.proofit.busapp;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lv.proofit.busapp.api.draft.price.request.DraftPriceRequest;
import lv.proofit.busapp.api.draft.price.response.DraftPriceResponse;
import lv.proofit.busapp.api.tax.rates.TaxRatesResponse;
import lv.proofit.busapp.feature.draft.price.DraftPriceService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.time.LocalDate;

@Slf4j
@RestController
@RequiredArgsConstructor
public class Controller {

    private final DraftPriceService draftPriceService;

    @PostMapping("/draft-price")
    public DraftPriceResponse calculateDraftPrice(@Valid @RequestBody DraftPriceRequest request) {
        return draftPriceService.calculatePrices(request);
    }

    /* This is made purely to simulate 3RD party endpoint and to showcase TaxRatesApiClient.class */
    @GetMapping("/tax-rates")
    public TaxRatesResponse getTaxRatesResponse(
            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
    ) {
        log.info("Tax rates: Requested rates at:[{}]", date);
        return TaxRatesResponse.builder()
                .taxRate(new BigDecimal(21))
                .build();
    }
}
