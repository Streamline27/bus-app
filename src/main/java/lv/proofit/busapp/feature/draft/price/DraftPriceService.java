package lv.proofit.busapp.feature.draft.price;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lv.proofit.busapp.api.draft.price.request.DraftPriceRequest;
import lv.proofit.busapp.api.draft.price.request.Passenger;
import lv.proofit.busapp.api.draft.price.response.DraftPriceResponse;
import lv.proofit.busapp.api.draft.price.response.PassengerPrice;
import lv.proofit.busapp.feature.tax.rates.TaxRatesApiClient;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class DraftPriceService {

    private static final BigDecimal LUGGAGE_DISCOUNT_PERCENT = new BigDecimal(70);

    private final BasePriceService basePriceService;
    private final TaxRatesApiClient taxRatesApiClient;

    public DraftPriceResponse calculatePrices(DraftPriceRequest request) {
        log.info("Draft price: Computing price. destinationTerminalName:[{}], numPassengers:[{}]",
                request.getDestinationTerminalName(),
                request.getPassengers().size()
        );
        List<PassengerPrice> passengerPriceList = getPassengerPrices(request);
        return DraftPriceResponse.builder()
                .destinationTerminalName(request.getDestinationTerminalName())
                .total(getTotal(passengerPriceList))
                .prices(passengerPriceList)
                .build();
    }

    private List<PassengerPrice> getPassengerPrices(DraftPriceRequest request) {
        BigDecimal basePrice = basePriceService.getBy(request.getDestinationTerminalName());
        BigDecimal taxRatePercent = taxRatesApiClient.get(LocalDate.now()).getTaxRate();
        Calculator calculator = new Calculator(basePrice);
        List<PassengerPrice> passengerPriceList = new ArrayList<>();
        for (Passenger passenger : request.getPassengers()) {
            BigDecimal tickerPrice = calculator.computePrice(passenger.getAge().getDiscountPercent(), taxRatePercent);
            BigDecimal luggagePrice = calculator.computePrice(LUGGAGE_DISCOUNT_PERCENT, taxRatePercent).multiply(new BigDecimal(passenger.getNumberOfBags()));
            PassengerPrice passengerPrice = PassengerPrice.builder()
                    .passenger(passenger)
                    .ticketPrice(tickerPrice.doubleValue())
                    .luggagePrice(luggagePrice.doubleValue())
                    .build();
            passengerPriceList.add(passengerPrice);
        }
        return passengerPriceList;
    }

    private Double getTotal(List<PassengerPrice> prices) {
        return prices.stream()
                .map(PassengerPrice::getTotalPrice)
                .reduce(0D, Double::sum);
    }
}
