package fi.ohtu.mobilityprofile.domain;

import android.util.Log;

import com.orm.SugarRecord;

import fi.ohtu.mobilityprofile.data.PlaceDao;

/**
 * Class is used to save places assumed significant to the user (ie. places where he/she spends some
 * time and not just points on the road).
 */
public class Place extends SugarRecord {
    private String name;
    private String address;
    private boolean favourite;
    private Coordinate coordinate;

    /**
     *
     */
    public Place() {
        this.name = "name";
        this.address = "address";
        this.favourite = false;
        this.coordinate = new Coordinate(0f, 0f);
    }

    /**
     * Creates Place.
     * @param name Name of the Place
     * @param address Address of the Place
     */
    public Place(String name, String address) {
        this.name = name;
        this.address = address;
        this.favourite = false;
        this.coordinate = null;
    }

    /**
     * Creates Place.
     * @param name Name of the Place
     * @param address Address of the Place
     * @param coordinate Coordinate object representing the coordinates of the place
     */
    public Place(String name, String address, Coordinate coordinate) {
        this.name = name;
        this.address = address;
        this.favourite = false;
        this.coordinate = coordinate;
    }

    public Coordinate getCoordinate() {
        return this.coordinate;
    }

    public void setCoordinate(Coordinate coordinate) {
        this.coordinate = coordinate;
    }

    public String getAddress() {
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
        return this.coordinate.distanceTo(coordinate);
    }

    @Override
    public boolean delete() {
        try {
            this.coordinate.delete();
        } catch (Exception e) {
            Log.i("Place", "Place didn't have coordinates!");
        }

        return super.delete();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Place that = (Place) o;

        return coordinate.equals(that.coordinate);

    }

    public boolean isFavourite() {
        return favourite;
    }

    public void setFavourite(boolean favourite) {
        this.favourite = favourite;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return getAddress();
    }
}
