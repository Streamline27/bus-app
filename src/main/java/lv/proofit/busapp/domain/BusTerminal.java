package lv.proofit.busapp.domain;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.math.BigDecimal;

@Data
@Entity
public class BusTerminal {
    @Id
    private String name;
    private BigDecimal price;
}
