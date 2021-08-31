package iris.client_bff.core.sync;

import iris.client_bff.core.sync.SyncTimes.DataTypes;

import org.springframework.data.repository.CrudRepository;

public interface SyncTimesRepository extends CrudRepository<SyncTimes, DataTypes> {}
