package lv.proofit.busapp.api.tax.rates;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class TaxRatesResponse {
    @Schema(example = "21")
    double taxRate;
}
