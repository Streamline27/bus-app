package lv.proofit.busapp.feature.tax.rates;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lv.proofit.busapp.BusAppProperties;
import lv.proofit.busapp.api.tax.rates.TaxRatesResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDate;

@Slf4j
@Service
@RequiredArgsConstructor
public class TaxRatesApiClient {

    private final BusAppProperties properties;

    private final RestTemplate restTemplate;

    public TaxRatesResponse get(LocalDate date) {
        ResponseEntity<TaxRatesResponse> response = restTemplate.getForEntity(getUrl(date), TaxRatesResponse.class);
        HttpStatus code = response.getStatusCode();
        if (code != HttpStatus.OK) {
            throw new IllegalStateException("Could not fetch data from tax rates service! Bad response code:[" + code + "]");
        }
        return response.getBody();
    }

    private String getUrl(LocalDate date) {
        return UriComponentsBuilder.fromHttpUrl(properties.getTaxRatesServiceUrl() + "/tax-rates")
                .queryParam("date", date)
                .build()
                .toUriString();
    }
}
