package se.citerus.dddsample.infrastructure.persistence.inmemory;

import se.citerus.dddsample.domain.model.location.Location;
import se.citerus.dddsample.domain.model.location.LocationRepository;
import se.citerus.dddsample.domain.model.location.UnLocode;
import se.citerus.dddsample.infrastructure.sampledata.SampleLocations;

import java.util.List;

public class LocationRepositoryInMem implements LocationRepository {

  public Location find(UnLocode unLocode) {
    var locations = SampleLocations.getAll();
    for (Location location : locations) {
      if (location.unLocode().equals(unLocode)) {
        return location;
      }
    }
    return null;
  }

  public List<Location> getAll() {
    return SampleLocations.getAll();
  }

  @Override
  public Location store(Location location) {
    return location;
  }

}
