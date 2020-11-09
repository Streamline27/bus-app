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

import static lv.proofit.busapp.feature.draft.price.PriceUtils.computeLuggagePrice;
import static lv.proofit.busapp.feature.draft.price.PriceUtils.computePrice;

@Slf4j
@Service
@RequiredArgsConstructor
public class DraftPriceService {

    private final BasePriceService basePriceService;
    private final TaxRatesApiClinet taxRatesApiClinet;

    public DraftPriceResponse calculatePrices(DraftPriceRequest request) {
        log.info("Draft price: Computing price. destinationTerminalName:[{}], numPassengers:[{}]",
                request.getDestinationTerminalName(),
                request.getPassengers().size()
        );
        List<PassengerPrice> priceList = getPassengerPrices(request);
        return DraftPriceResponse.builder()
                .destinationTerminalName(request.getDestinationTerminalName())
                .total(getTotalPrice(priceList))
                .prices(priceList)
                .build();
    }

    private List<PassengerPrice> getPassengerPrices(DraftPriceRequest request) {
        double basePrice = basePriceService.getBy(request.getDestinationTerminalName());
        double taxRatePercent = taxRatesApiClinet.getTaxRate(LocalDate.now()).getTaxRate();
        List<PassengerPrice> passengerPriceList = new ArrayList<>();
        for (Passenger passenger : request.getPassengers()) {
            double tickerPrice = computePrice(basePrice, passenger.getDiscountPercent(), taxRatePercent);
            double luggagePrice = computeLuggagePrice(basePrice, taxRatePercent, passenger.getNumberOfBags());
            PassengerPrice passengerPrice = PassengerPrice.builder()
                    .passenger(passenger)
                    .ticketPrice(tickerPrice)
                    .luggagePrice(luggagePrice)
                    .build();
            passengerPriceList.add(passengerPrice);
        }
        return passengerPriceList;
    }

    private Double getTotalPrice(List<PassengerPrice> prices) {
        return prices.stream()
                .map(PassengerPrice::getTotalPrice)
                .reduce(0D, Double::sum);
    }
}
