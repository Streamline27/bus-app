package lv.proofit.busapp.feature.ticker.price;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lv.proofit.busapp.api.request.DraftPriceRequest;
import lv.proofit.busapp.api.request.Passenger;
import lv.proofit.busapp.api.response.DraftPriceResponse;
import lv.proofit.busapp.api.response.PassengerPrice;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class TickerPriceService {

    private static final BigDecimal LUGGAGE_DISCOUNT_PERCENT = new BigDecimal(70);

    private final BasePriceService basePriceService;
    private final TaxRateService taxRateService;

    public DraftPriceResponse calculatePrices(DraftPriceRequest request) {
        List<PassengerPrice> passengerPriceList = getPassengerPrices(request);
        return DraftPriceResponse.builder()
                .destinationTerminalName(request.getDestinationTerminalName())
                .total(getTotal(passengerPriceList))
                .prices(passengerPriceList)
                .build();
    }

    private List<PassengerPrice> getPassengerPrices(DraftPriceRequest request) {
        log.info("Requesting draft price. destinationTerminalName:[{}], numPassengers:[{}]",
                request.getDestinationTerminalName(),
                request.getPassengers().size()
        );
        BigDecimal basePrice = basePriceService.getBy(request.getDestinationTerminalName());
        BigDecimal taxRatePercent = taxRateService.get();
        Calculator calculator = new Calculator(basePrice);
        List<PassengerPrice> passengerPriceList = new ArrayList<>();
        for (Passenger passenger : request.getPassengers()) {
            BigDecimal tickerPrice = calculator.computePrice(passenger.getAge().getDiscountPercent(), taxRatePercent);
            BigDecimal luggagePrice = calculator.computePrice(LUGGAGE_DISCOUNT_PERCENT, taxRatePercent).multiply(passenger.getNumberOfBags());
            PassengerPrice passengerPrice = PassengerPrice.builder()
                    .passenger(passenger)
                    .ticketPrice(tickerPrice)
                    .luggagePrice(luggagePrice)
                    .build();
            passengerPriceList.add(passengerPrice);
        }
        return passengerPriceList;
    }

    private BigDecimal getTotal(List<PassengerPrice> prices) {
        return prices.stream()
                .map(PassengerPrice::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
