package lv.proofit.busapp.domain;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BusTerminalRepository extends CrudRepository<BusTerminal, String> {

    Optional<BusTerminal> findByName(String busTerminalName);
}
