package lv.proofit.busapp.api.exceptions;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class ErrorDto {
    @Schema(example = "draftPriceRequest")
    public String objectName;

    @Schema(example = "passengers")
    public String fieldName;

    @Schema(example = "must not be null")
    public String message;
}
