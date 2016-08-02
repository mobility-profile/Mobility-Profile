package fi.ohtu.mobilityprofile.domain;

/**
 * Interface for all classes that have a coordinate which should be geocoded and saved as an address to the object
 */
public interface HasAddress {
    public void updateAddress(String address);
    public Coordinate getCoordinate();
}
