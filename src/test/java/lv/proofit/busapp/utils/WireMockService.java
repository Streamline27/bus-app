package lv.proofit.busapp.utils;

import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.ResponseDefinitionBuilder;
import com.github.tomakehurst.wiremock.client.WireMock;
import org.springframework.stereotype.Component;

import java.io.StringWriter;
import java.util.Map;

import static com.github.tomakehurst.wiremock.client.WireMock.matching;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;

@Component
public class WireMockService {

    private final WireMockServer wireMockSerer;

    private final Mustache taxRateResponseTemplate;

    public WireMockService() {
        this.wireMockSerer = new WireMockServer(options().port(8089));
        this.wireMockSerer.start();

        MustacheFactory mf = new DefaultMustacheFactory();
        this.taxRateResponseTemplate = mf.compile("templates/tax-rate.mustache");
    }

    public void taxRateServiceReturnsTaxRateEqualTo(double taxRate) {
        String json = jsonOf(taxRateResponseTemplate, Map.of("taxRate", taxRate));
        this.wireMockSerer.stubFor(
                WireMock.get(WireMock.urlPathEqualTo("/tax-rates"))
                        .withQueryParam("date", matching(".*"))
                        .willReturn(aJsonResponseWithBody(json))
        );
    }

    private ResponseDefinitionBuilder aJsonResponseWithBody(String json) {
        return WireMock.aResponse()
                .withHeader("Content-Type", "application/json")
                .withBody(json);
    }

    private String jsonOf(Mustache template, Object scope) {
        StringWriter writer = new StringWriter();
        template.execute(writer, scope);
        return writer.toString();
    }
}
