package lv.proofit.busapp.api.draft.price.request;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lv.proofit.busapp.Constants;


@Getter
@RequiredArgsConstructor
public enum PassengerAge {

    ADULT(Constants.ADULT_DISCOUNT_PERCENT),
    CHILD(Constants.CHILD_DISCOUNT_PERCENT);

    private final double discountPercent;
}
