package fi.ohtu.mobilityprofile.util.geocoding;

import fi.ohtu.mobilityprofile.domain.Coordinate;

public interface AddressConvertListener {
    void addressConverted(String address, Coordinate coordinate);
}
