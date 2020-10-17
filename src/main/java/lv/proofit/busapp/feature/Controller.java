package lv.proofit.busapp.feature;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lv.proofit.busapp.api.draft.price.request.DraftPriceRequest;
import lv.proofit.busapp.api.draft.price.response.DraftPriceResponse;
import lv.proofit.busapp.api.tax.rates.TaxRatesResponse;
import lv.proofit.busapp.feature.draft.price.DraftPriceService;
import lv.proofit.busapp.feature.tax.rates.TaxRatesService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;

@Slf4j
@RestController
@RequiredArgsConstructor
public class Controller {

    private final DraftPriceService draftPriceService;
    private final TaxRatesService taxRatesService;

    @PostMapping("/draft-price")
    public DraftPriceResponse calculateDraftPrice(@Valid @RequestBody DraftPriceRequest request) {
        return draftPriceService.calculatePrices(request);
    }

    @GetMapping("/tax-rates")
    public TaxRatesResponse getTaxRatesResponse(
            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate localDate
    ) {
        return taxRatesService.getRatesAt(localDate);
    }
}
