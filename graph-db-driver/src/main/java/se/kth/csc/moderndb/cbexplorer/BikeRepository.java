package se.kth.csc.moderndb.cbexplorer;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * Repository used for pull specific information about Bikes in the current database.
 *
 * Created by mhotan on 4/21/14.
 */
@RepositoryRestResource(collectionResourceRel = "bike", path = "bike")
public interface BikeRepository extends PagingAndSortingRepository<Bike, Long> {

}
