package iris.client_bff.events;

import iris.client_bff.events.model.Location;
import iris.client_bff.events.model.Location.LocationIdentifier;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Jens Kutzsche
 */
public interface LocationRepository extends JpaRepository<Location, LocationIdentifier> {}
