package ac.uk.bristol.cs.santa.grotto.business.data;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Created by csxds on 26/11/2017.
 */

public interface GrottoRepository extends CrudRepository<Grotto, Long> {
    List<Grotto> findByName(String name);
}
