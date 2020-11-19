package lv.proofit.busapp.utils;

import lombok.Builder;
import lombok.Value;
import org.junit.jupiter.api.Assertions;

@Value
@Builder
public class TestApiResult<T> {
    T body;
    int status;

    public TestApiResult<T> assertStatusIs(int expectedStatus) {
        Assertions.assertEquals(expectedStatus, this.status);
        return this;
    }
}
