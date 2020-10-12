package lv.proofit.busapp.feature;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lv.proofit.busapp.api.ExceptionResponse;
import lv.proofit.busapp.api.request.DraftPriceRequest;
import lv.proofit.busapp.api.response.DraftPriceResponse;
import lv.proofit.busapp.feature.ticker.price.TickerPriceService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RestControllerAdvice
@RequiredArgsConstructor
public class Controller {

    private final TickerPriceService tickerPriceService;

    @PostMapping("/price")
    public DraftPriceResponse calculatePrice(@RequestBody DraftPriceRequest request) {
        return tickerPriceService.calculatePrices(request);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(Exception.class)
    public ExceptionResponse handle(Exception ex) {
        log.error("Error during request processing.", ex);
        return new ExceptionResponse(ex.getMessage());
    }
}
