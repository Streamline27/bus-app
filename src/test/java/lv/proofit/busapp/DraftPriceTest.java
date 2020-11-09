package lv.proofit.busapp;


import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;
import com.github.tomakehurst.wiremock.client.WireMock;
import lombok.Builder;
import lv.proofit.busapp.api.draft.price.request.DraftPriceRequest;
import lv.proofit.busapp.api.draft.price.request.Passenger;
import lv.proofit.busapp.api.draft.price.request.PassengerAge;
import lv.proofit.busapp.api.draft.price.response.DraftPriceResponse;
import lv.proofit.busapp.api.draft.price.response.PassengerPrice;
import lv.proofit.busapp.feature.draft.price.BasePrice;
import lv.proofit.busapp.feature.draft.price.BasePriceRepository;
import lv.proofit.busapp.utils.IntegrationTest;
import lv.proofit.busapp.utils.TestApiClient;
import lv.proofit.busapp.utils.TestApiResult;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DraftPriceTest extends IntegrationTest {

    @Autowired
    private BasePriceRepository repository;

    @Autowired
    private TestApiClient client;

    public static final String PASSENGER_NAME_1 = "Vladislav";
    public static final String PASSENGER_NAME_2 = "Egor";
    private static final String EXPECTED_DESTINATION_TERMINAL = "Some station, Somewhere else";

    @ParameterizedTest
    @MethodSource("whereSinglePassengerParamsAre")
    public void shouldReturnCorrectResultForSinglePassenger(SinglePassengerArguments args) throws Exception {
        // given:
        taxRateServiceReturnsTaxRateEqualTo(args.taxRate);
        basePriceForExpectedDestinationTerminalIs(args.basePrice);

        // when:
        TestApiResult<DraftPriceResponse> response = calculateDraftPriceFor(List.of(args.passenger));

        // then:
        DraftPriceResponse draftPriceResponse = response.assertStatusIs(200).getBody();

        assertTerminalNameIsAsExpected(draftPriceResponse);
        assertPriceListSizeIs(1, draftPriceResponse);
        assertPricesForPassengerAre(args.tickerPrice, args.luggagePrice, args.passenger.getName(), draftPriceResponse);
        assertTotalPriceEqualsTo(args.totalPrice, draftPriceResponse);
    }

    private static Stream<SinglePassengerArguments> whereSinglePassengerParamsAre() {
        return Stream.of(
                SinglePassengerArguments.builder() // ADULT, NO TAX, NO LUGGAGE
                        .passenger(adultPassenger(PASSENGER_NAME_1))
                        .taxRate(0).basePrice(10)
                        .tickerPrice(10).luggagePrice(0)
                        .totalPrice(10)
                        .build(),
                SinglePassengerArguments.builder() // ADULT, WITH TAX, NO LUGGAGE
                        .passenger(adultPassenger(PASSENGER_NAME_1))
                        .taxRate(10).basePrice(10)
                        .tickerPrice(11).luggagePrice(0)
                        .totalPrice(11)
                        .build(),
                SinglePassengerArguments.builder() // CHILD, NO TAX, NO LUGGAGE
                        .passenger(childPassenger(PASSENGER_NAME_1))
                        .taxRate(0).basePrice(10)
                        .tickerPrice(5).luggagePrice(0)
                        .totalPrice(5)
                        .build(),
                SinglePassengerArguments.builder() // CHILD, WITH TAX, NO LUGGAGE
                        .passenger(childPassenger(PASSENGER_NAME_1))
                        .taxRate(10).basePrice(10)
                        .tickerPrice(5.5).luggagePrice(0)
                        .totalPrice(5.5)
                        .build(),
                SinglePassengerArguments.builder() // ADULT, NO TAX, WITH LUGGAGE
                        .passenger(adultPassenger(PASSENGER_NAME_1, 1))
                        .taxRate(0).basePrice(10)
                        .tickerPrice(10).luggagePrice(3)
                        .totalPrice(13)
                        .build(),
                SinglePassengerArguments.builder() // ADULT, WITH TAX, WITH LUGGAGE
                        .passenger(adultPassenger(PASSENGER_NAME_1, 1))
                        .taxRate(10).basePrice(10)
                        .tickerPrice(11).luggagePrice(3.3)
                        .totalPrice(14.3)
                        .build(),
                SinglePassengerArguments.builder() // CHILD, NO TAX, WITH LUGGAGE
                        .passenger(childPassenger(PASSENGER_NAME_1, 1))
                        .taxRate(0).basePrice(10)
                        .tickerPrice(5).luggagePrice(3)
                        .totalPrice(8)
                        .build(),
                SinglePassengerArguments.builder() // CHILD, WITH TAX, WITH LUGGAGE
                        .passenger(childPassenger(PASSENGER_NAME_1, 1))
                        .taxRate(10).basePrice(10)
                        .tickerPrice(5.5).luggagePrice(3.3)
                        .totalPrice(8.8)
                        .build()
        );
    }

    @ParameterizedTest
    @MethodSource("whereTwoPassengerParamsAre")
    public void shouldReturnCorrectResultForTwoPassengers(TwoPassengerArguments args) throws Exception {
        // given:
        taxRateServiceReturnsTaxRateEqualTo(args.taxRate);
        basePriceForExpectedDestinationTerminalIs(args.basePrice);

        // when:
        TestApiResult<DraftPriceResponse> response = calculateDraftPriceFor(List.of(args.passenger1, args.passenger2));

        // then:
        DraftPriceResponse draftPriceResponse = response.assertStatusIs(200).getBody();

        assertTerminalNameIsAsExpected(draftPriceResponse);
        assertPriceListSizeIs(2, draftPriceResponse);
        assertPricesForPassengerAre(args.tickerPrice1, args.luggagePrice1, args.passenger1.getName(), draftPriceResponse);
        assertPricesForPassengerAre(args.tickerPrice2, args.luggagePrice2, args.passenger2.getName(), draftPriceResponse);
        assertTotalPriceEqualsTo(args.totalPrice, draftPriceResponse);
    }

    private static Stream<TwoPassengerArguments> whereTwoPassengerParamsAre() {
        return Stream.of(
                TwoPassengerArguments.builder() // Happy path
                        .taxRate(21).basePrice(10)
                        .passenger1(adultPassenger(PASSENGER_NAME_1, 2)).passenger2(childPassenger(PASSENGER_NAME_2, 1))
                        .tickerPrice1(12.10).tickerPrice2(6.05)
                        .luggagePrice1(7.26).luggagePrice2(3.63)
                        .totalPrice(29.04)
                        .build(),
                TwoPassengerArguments.builder() // Two ADULTS, NO TAX, NO LUGGAGE
                        .taxRate(0).basePrice(10)
                        .passenger1(adultPassenger(PASSENGER_NAME_1)).passenger2(adultPassenger(PASSENGER_NAME_2))
                        .tickerPrice1(10).tickerPrice2(10)
                        .luggagePrice1(0).luggagePrice2(0)
                        .totalPrice(20)
                        .build(),
                TwoPassengerArguments.builder() // Two ADULTS, WITH TAX, NO LUGGAGE
                        .taxRate(10).basePrice(10)
                        .passenger1(adultPassenger(PASSENGER_NAME_1)).passenger2(adultPassenger(PASSENGER_NAME_2))
                        .tickerPrice1(11).tickerPrice2(11)
                        .luggagePrice1(0).luggagePrice2(0)
                        .totalPrice(22)
                        .build(),
                TwoPassengerArguments.builder() // Two ADULTS, NO TAX, 1st WITH 1 luggage
                        .taxRate(0).basePrice(10)
                        .passenger1(adultPassenger(PASSENGER_NAME_1, 1)).passenger2(adultPassenger(PASSENGER_NAME_2))
                        .tickerPrice1(10).tickerPrice2(10)
                        .luggagePrice1(3).luggagePrice2(0)
                        .totalPrice(23)
                        .build(),
                TwoPassengerArguments.builder() // Two ADULTS, WITH TAX, 1st WITH 1 luggage
                        .taxRate(10).basePrice(10)
                        .passenger1(adultPassenger(PASSENGER_NAME_1, 1)).passenger2(adultPassenger(PASSENGER_NAME_2))
                        .tickerPrice1(11).tickerPrice2(11)
                        .luggagePrice1(3.3).luggagePrice2(0)
                        .totalPrice(25.3)
                        .build(),
                TwoPassengerArguments.builder() // Two ADULTS, NO TAX, 1st WITH 2 luggage
                        .taxRate(0).basePrice(10)
                        .passenger1(adultPassenger(PASSENGER_NAME_1, 2)).passenger2(adultPassenger(PASSENGER_NAME_2))
                        .tickerPrice1(10).tickerPrice2(10)
                        .luggagePrice1(6).luggagePrice2(0)
                        .totalPrice(26)
                        .build(),
                TwoPassengerArguments.builder() // Two ADULTS, WITH TAX, 1st WITH 2 luggage
                        .taxRate(10).basePrice(10)
                        .passenger1(adultPassenger(PASSENGER_NAME_1, 2)).passenger2(adultPassenger(PASSENGER_NAME_2))
                        .tickerPrice1(11).tickerPrice2(11)
                        .luggagePrice1(6.6).luggagePrice2(0)
                        .totalPrice(28.6)
                        .build(),
                TwoPassengerArguments.builder() // ADULT AND CHILD, NO TAX, NO LUGGAGE
                        .taxRate(0).basePrice(10)
                        .passenger1(adultPassenger(PASSENGER_NAME_1)).passenger2(childPassenger(PASSENGER_NAME_2))
                        .tickerPrice1(10).tickerPrice2(5)
                        .luggagePrice1(0).luggagePrice2(0)
                        .totalPrice(15)
                        .build(),
                TwoPassengerArguments.builder() // ADULT AND CHILD, WITH TAX, NO LUGGAGE
                        .taxRate(10).basePrice(10)
                        .passenger1(adultPassenger(PASSENGER_NAME_1)).passenger2(childPassenger(PASSENGER_NAME_2))
                        .tickerPrice1(11).tickerPrice2(5.5)
                        .luggagePrice1(0).luggagePrice2(0)
                        .totalPrice(16.5)
                        .build(),
                TwoPassengerArguments.builder() // ADULT AND CHILD, WITH TAX, CHILD WITH LUGGAGE
                        .taxRate(10).basePrice(10)
                        .passenger1(adultPassenger(PASSENGER_NAME_1)).passenger2(childPassenger(PASSENGER_NAME_2, 1))
                        .tickerPrice1(11).tickerPrice2(5.5)
                        .luggagePrice1(0).luggagePrice2(3.3)
                        .totalPrice(19.8)
                        .build(),
                TwoPassengerArguments.builder() // TWO CHILDREN, WITH TAX, ONE WITH LUGGAGE
                        .taxRate(10).basePrice(10)
                        .passenger1(childPassenger(PASSENGER_NAME_1)).passenger2(childPassenger(PASSENGER_NAME_2, 1))
                        .tickerPrice1(5.5).tickerPrice2(5.5)
                        .luggagePrice1(0).luggagePrice2(3.3)
                        .totalPrice(14.3)
                        .build()
        );
    }


    private void assertPricesForPassengerAre(double priceForTicker, double priceForLuggage, String passengerName, DraftPriceResponse response) {
        assertEquals(priceForTicker, getPassengerPrice(passengerName, response.getPrices()).getTicketPrice());
        assertEquals(priceForLuggage, getPassengerPrice(passengerName, response.getPrices()).getLuggagePrice());
    }


    private PassengerPrice getPassengerPrice(String name, List<PassengerPrice> priceList) {
        return priceList.stream().filter(it -> it.getPassenger().getName().equals(name)).findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }

    private void assertTotalPriceEqualsTo(double total, DraftPriceResponse draftPriceResponse) {
        assertEquals(total, draftPriceResponse.getTotal());
    }

    private void assertTerminalNameIsAsExpected(DraftPriceResponse draftPriceResponse) {
        assertEquals(EXPECTED_DESTINATION_TERMINAL, draftPriceResponse.getDestinationTerminalName());
    }

    private void assertPriceListSizeIs(int i, DraftPriceResponse draftPriceResponse) {
        assertEquals(i, draftPriceResponse.getPrices().size());
    }

    private static Passenger childPassenger(String name) {
        return childPassenger(name, 0);
    }

    private static Passenger adultPassenger(String name) {
        return adultPassenger(name, 0);
    }

    private static Passenger childPassenger(String name, int numBags) {
        return Passenger.builder()
                .name(name)
                .age(PassengerAge.CHILD)
                .numberOfBags(numBags)
                .build();
    }

    private static Passenger adultPassenger(String name, int numBags) {
        return Passenger.builder()
                .name(name)
                .age(PassengerAge.ADULT)
                .numberOfBags(numBags)
                .build();
    }

    private TestApiResult<DraftPriceResponse> calculateDraftPriceFor(List<Passenger> passenger2) throws Exception {
        DraftPriceRequest request = DraftPriceRequest.builder()
                .destinationTerminalName(EXPECTED_DESTINATION_TERMINAL)
                .passengers(passenger2)
                .build();
        return client.calculateDraftPrice(request);
    }

    private void basePriceForExpectedDestinationTerminalIs(double price) {
        repository.save(
                new BasePrice()
                    .setTerminalName(EXPECTED_DESTINATION_TERMINAL)
                    .setPrice(price)
        );
    }

    public void taxRateServiceReturnsTaxRateEqualTo(double taxRate) {
        wireMockService.taxRateServiceReturnsTaxRateEqualTo(taxRate);
    }

    @Builder
    private static class TwoPassengerArguments {
        Passenger passenger1;
        Passenger passenger2;
        double basePrice;
        double taxRate;
        double tickerPrice1;
        double luggagePrice1;
        double tickerPrice2;
        double luggagePrice2;
        double totalPrice;

        @Override
        public String toString() {
            return "passengerAge1:[" + passenger1.getAge() + "], passengerLuggage1:[" + passenger1.getNumberOfBags() + "] " +
                    "passengerAge2:[" + passenger2.getAge() + "], passengerLuggage2:[" + passenger2.getNumberOfBags() + "] " +
                    "taxRate:[" + taxRate + "], basePrice:[" + basePrice + "]";
        }
    }

    @Builder
    private static class SinglePassengerArguments {
        Passenger passenger;
        double basePrice;
        double taxRate;
        double tickerPrice;
        double luggagePrice;
        double totalPrice;

        @Override
        public String toString() {
            return "passangeAge:[" + passenger.getAge() + "], passengerLuggage:["+ passenger.getNumberOfBags() +"], taxRate:[" + taxRate + "], basePrice:[" + basePrice + "]";
        }
    }
}
