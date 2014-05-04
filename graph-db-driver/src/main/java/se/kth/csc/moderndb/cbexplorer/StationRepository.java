package se.kth.csc.moderndb.cbexplorer;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * Created by mhotan on 4/21/14.
 */
@RepositoryRestResource(collectionResourceRel = "station", path = "station")
public interface StationRepository extends PagingAndSortingRepository<Station, Long> {

}
