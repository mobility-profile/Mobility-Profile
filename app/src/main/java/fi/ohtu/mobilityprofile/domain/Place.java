package fi.ohtu.mobilityprofile.domain;

import android.location.Address;
import android.util.Log;

import com.orm.SugarRecord;

import java.util.Locale;

import fi.ohtu.mobilityprofile.data.PlaceDao;

/**
 * Class is used to save places assumed significant to the user (ie. places where he/she spends some
 * time and not just points on the road).
 */
public class Place extends SugarRecord {
    private String name;
    private Address address;
    private boolean favourite;

    /**
     *
     */
    public Place() {
        this.name = "name";
        this.address = new Address(Locale.getDefault());
        this.favourite = false;
    }

    /**
     * Creates Place.
     * @param name Name of the Place
     * @param address Address of the Place
     */
    public Place(String name, Address address) {
        this.name = name;
        this.address = address;
        this.favourite = false;
    }

    public Coordinate getCoordinate() {
        return new Coordinate(new Float(this.address.getLatitude()), new Float(this.address.getLongitude()));
    }

    public void setCoordinate(Coordinate coordinate) {
        this.address.setLatitude(coordinate.getLatitude());
        this.address.setLongitude(coordinate.getLongitude());
    }

    public Address getAddress() {
        return address;
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
        return getCoordinate().distanceTo(coordinate);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Place that = (Place) o;

        return address.equals(that.address);

    }

    public boolean isFavourite() {
        return favourite;
    }

    public void setFavourite(boolean favourite) {
        this.favourite = favourite;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return getAddress().toString();
    }
}
