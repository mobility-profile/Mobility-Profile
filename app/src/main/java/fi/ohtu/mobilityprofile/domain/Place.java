package fi.ohtu.mobilityprofile.domain;

import android.location.Address;

import com.orm.SugarRecord;

import java.util.Locale;

/**
 * Class is used to save places assumed significant to the user (ie. places where he/she spends some
 * time and not just points on the road).
 */
public class Place extends SugarRecord {
    private String name;
    private String addressLine1;
    private String addressLine2;
    private String addressLine3;
    private Coordinate coordinate;
    private String adminArea;
    private String countryCode;
    private String countryName;
    private String featureName;
    private String locality;
    private String postalCode;
    private boolean favourite;


    /**
     *
     */
    public Place() {
        this.name = "name";
        this.coordinate = new Coordinate(new Float(0), new Float(0));
        this.coordinate.save();
        this.favourite = false;
    }

    /**
     * Creates Place.
     *
     * @param name    Name of the Place
     * @param address Address of the Place
     */
    public Place(String name, Address address) {
        this.name = name;
        this.coordinate = new Coordinate((float) address.getLatitude(), (float) address.getLongitude());
        this.coordinate.save();
        this.favourite = false;
        setAddress(address);
    }

    public Coordinate getCoordinate() {
        return this.coordinate;
    }

    public void setCoordinate(Coordinate coordinate) {
        this.coordinate = coordinate;
        this.coordinate.save();
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    /**
     * Returns the distance between this Place and given coordinate
     *
     * @param coordinate coordinate to be compared
     * @return distance
     */
    public double distanceTo(Coordinate coordinate) {
        return this.coordinate.distanceTo(coordinate);
    }

//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (o == null || getClass() != o.getClass()) return false;
//
//        Place that = (Place) o;
//
//        return this.coordinate.equals(that.getCoordinate());
//
//    }

    public boolean isFavourite() {
        return favourite;
    }

    /**
     *
     * @param favourite true if favourited, false if not
     */
    public void setFavourite(boolean favourite) {
        this.favourite = favourite;
        this.save();
    }

    /**
     * Get Address object of the Place
     * @return Address
     */
    public Address getAddress() {
        Address address = new Address(Locale.getDefault());
        address.setAdminArea(adminArea);
        address.setCountryCode(countryCode);
        address.setCountryName(countryName);
        address.setFeatureName(featureName);
        if (coordinate != null) {
            address.setLatitude(coordinate.getLatitude());
            address.setLongitude(coordinate.getLongitude());
        }
        address.setLocality(locality);
        address.setPostalCode(postalCode);
        if (addressLine1 != null) address.setAddressLine(0, addressLine1);
        if (addressLine2 != null) address.setAddressLine(1, addressLine2);
        if (addressLine3 != null) address.setAddressLine(2, addressLine3);
        return address;
    }

    /**
     * Saves the Address data
     * @param address address to be saved
     */
    public void setAddress(Address address) {
        this.adminArea = address.getAdminArea();
        this.countryCode = address.getCountryCode();
        this.countryName = address.getCountryName();
        this.featureName = address.getFeatureName();
        this.locality = address.getLocality();
        this.postalCode = address.getPostalCode();

        int addressLines = address.getMaxAddressLineIndex();
        if (addressLines > 0) addressLine1 = address.getAddressLine(0);
        if (addressLines > 1) addressLine2 = address.getAddressLine(1);
        if (addressLines > 2) addressLine3 = address.getAddressLine(2);
    }

    /**
     * Returns a line of the address numbered by the given index
     * @param index index of the address line
     * @return address line or null if no such line is present
     */
    public String getAddressLine(int index) {
        if (index == 0) return addressLine1;
        if (index == 1) return addressLine2;
        if (index == 2) return addressLine3;
        return null;
    }

    @Override
    public String toString() {
        return getAddress().toString();
    }
}
