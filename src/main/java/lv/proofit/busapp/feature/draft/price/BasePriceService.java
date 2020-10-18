package lv.proofit.busapp.feature.draft.price;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BasePriceService {

    private final BasePriceRepository repository;

    public double getBy(String busTerminalName) {
        return repository.findByTerminalName(busTerminalName)
                .orElseThrow(() -> new IllegalArgumentException("Could not find bus terminal with name:["+ busTerminalName +"]"))
                .getPrice();
    }
}
