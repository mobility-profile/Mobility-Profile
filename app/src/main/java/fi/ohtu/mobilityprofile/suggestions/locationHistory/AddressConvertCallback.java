package fi.ohtu.mobilityprofile.suggestions.locationHistory;

import fi.ohtu.mobilityprofile.domain.Coordinate;

public interface AddressConvertCallback {
    void addressConverted(String address, Coordinate coordinate);
}
