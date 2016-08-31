package fi.ohtu.mobilityprofile.domain;

import android.location.Address;

import com.orm.SugarRecord;

import java.util.HashMap;
import java.util.Locale;

/**
 * Class is used to save places assumed significant to the user (ie. places where he/she spends some
 * time and not just points on the road).
 */
public class Place extends SugarRecord {
    private String name;
    private HashMap<Integer, String> addressLines;
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
        this.favourite = false;
    }

    /**
     * Creates Place.
     * @param name Name of the Place
     * @param address Address of the Place
     */
    public Place(String name, Address address) {
        this.name = name;
        this.coordinate = new Coordinate((float) address.getLatitude(), (float)address.getLongitude());
        this.favourite = false;

        setAddress(address);
        this.coordinate.save();
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
     * @param coordinate coordinate to be compared
     * @return distance
     */
    public double distanceTo(Coordinate coordinate) {
        return this.coordinate.distanceTo(coordinate);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Place that = (Place) o;

        return this.coordinate.equals(that.getCoordinate());

    }

    public boolean isFavourite() {
        return favourite;
    }

    public void setFavourite(boolean favourite) {
        this.favourite = favourite;
        this.save();
    }

    public Address getAddress() {
        Address address = new Address(Locale.getDefault());
        address.setAdminArea(adminArea);
        address.setCountryCode(countryCode);
        address.setCountryName(countryName);
        address.setFeatureName(featureName);
        address.setLatitude(coordinate.getLatitude());
        address.setLongitude(coordinate.getLongitude());
        address.setLocality(locality);
        address.setPostalCode(postalCode);

        for (int i = 0; i < addressLines.size(); i++) {
            address.setAddressLine(i, addressLines.get(i));
        }
        return address;
    }

    public void setAddress(Address address) {
        this.adminArea = address.getAdminArea();
        this.countryCode = address.getCountryCode();
        this.countryName = address.getCountryName();
        this.featureName = address.getFeatureName();
        this.locality = address.getLocality();
        this.postalCode = address.getPostalCode();

        addressLines = new HashMap<>();
        for (int i = 0; i < address.getMaxAddressLineIndex(); i++) {
            addressLines.put(i, address.getAddressLine(i));
        }
    }

    public String getAddressLine(int index) {
        return addressLines.get(index);
    }
}
