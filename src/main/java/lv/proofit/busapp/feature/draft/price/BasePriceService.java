package lv.proofit.busapp.feature.draft.price;

import lombok.RequiredArgsConstructor;
import lv.proofit.busapp.domain.BusTerminalRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class BasePriceService {

    private final BusTerminalRepository repository;

    public BigDecimal getBy(String busTerminalName) {
        return repository.findByName(busTerminalName)
                .orElseThrow(() -> new IllegalArgumentException("Could not find bus terminal with name:["+ busTerminalName +"]"))
                .getPrice();
    }
}
