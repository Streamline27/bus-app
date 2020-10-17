package lv.proofit.busapp;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties("buss-app")
public class BusAppProperties {
    private String taxRatesServiceUrl;
}
