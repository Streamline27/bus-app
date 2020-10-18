package lv.proofit.busapp.api.draft.price.request;

import lombok.Getter;
import lombok.RequiredArgsConstructor;


@Getter
@RequiredArgsConstructor
public enum PassengerAge {

    ADULT(0),
    CHILD(50);

    private final double discountPercent;
}
