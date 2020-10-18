package lv.proofit.busapp.feature.draft.price;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lv.proofit.busapp.api.draft.price.request.DraftPriceRequest;
import lv.proofit.busapp.api.draft.price.request.Passenger;
import lv.proofit.busapp.api.draft.price.response.DraftPriceResponse;
import lv.proofit.busapp.api.draft.price.response.PassengerPrice;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class DraftPriceService {

    private static final double LUGGAGE_DISCOUNT_PERCENT = 70;

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
        double basePrice = basePriceService.getBy(request.getDestinationTerminalName());
        double taxRatePercent = taxRatesApiClient.get(LocalDate.now()).getTaxRate();
        List<PassengerPrice> passengerPriceList = new ArrayList<>();
        for (Passenger passenger : request.getPassengers()) {
            double tickerPrice = Calculator.computePrice(basePrice, passenger.getAge().getDiscountPercent(), taxRatePercent);
            double luggagePrice = Calculator.computePrice(basePrice, LUGGAGE_DISCOUNT_PERCENT, taxRatePercent) * passenger.getNumberOfBags();
            PassengerPrice passengerPrice = PassengerPrice.builder()
                    .passenger(passenger)
                    .ticketPrice(tickerPrice)
                    .luggagePrice(luggagePrice)
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
