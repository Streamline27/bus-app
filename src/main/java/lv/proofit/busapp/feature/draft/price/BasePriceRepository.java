package lv.proofit.busapp.feature.draft.price;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BasePriceRepository extends CrudRepository<BasePrice, String> {

    Optional<BasePrice> findByTerminalName(String busTerminalName);
}
