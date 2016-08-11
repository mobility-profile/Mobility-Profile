package fi.ohtu.mobilityprofile.util.geocoding;

import fi.ohtu.mobilityprofile.domain.Coordinate;

/**
 * Interface for receiving callbacks after an address or coordinates have been converted.
 */
public interface AddressConvertListener {
    /**
     * This method will be called once the address/coordinates have been converted.
     *
     * @param address Address
     * @param coordinate Coordinates
     */
    void addressConverted(String address, Coordinate coordinate);
}
