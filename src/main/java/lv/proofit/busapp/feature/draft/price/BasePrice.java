package lv.proofit.busapp.feature.draft.price;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.Entity;
import javax.persistence.Id;

@Data
@Accessors(chain = true)
@Entity
public class BasePrice {
    @Id
    private String terminalName;
    private double price;
}
