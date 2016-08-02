package fi.ohtu.mobilityprofile.domain;

import com.orm.SugarRecord;

/**
 * Class is used to save places assumed significant to the user (ie. places where he/she spends some
 * time and not just points on the road).
 */
public class SignificantPlace extends SugarRecord implements HasAddress {
    private String name;
    private String address;
    private boolean favourite;
    private boolean removed;
    private Coordinate coordinate;

    /**
     *
     */
    public SignificantPlace() {
    }

    /**
     * Creates SignificantPlace.
     * @param name Name of the SignificantPlace
     * @param address Address of the SignificantPlace
     * @param coordinate Coordinate object representing the coordinates of the place
     */
    public SignificantPlace(String name, String address, Coordinate coordinate) {
        this.name = name;
        this.address = address;
        this.favourite = false;
        this.removed = false;
        this.coordinate = coordinate;
    }

    @Override
    public Coordinate getCoordinate() { return this.coordinate; }

    public String getAddress() {
        return address;
    }

    public String getName() {
        return name;
    }

    public double distanceTo(SignificantPlace significantPlace) {
        return this.coordinate.distanceTo(significantPlace.getCoordinate());
    }

    @Override
    public long save() {
        this.coordinate.save();
        return super.save();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SignificantPlace that = (SignificantPlace) o;

        return coordinate.equals(that.coordinate);

    }

    @Override
    public void updateAddress(String address) {
        System.out.println(address);
        this.address = address;
        this.save();
    }

    public boolean isFavourite() {
        return favourite;
    }

    public void setFavourite(boolean favourite) {
        this.favourite = favourite;
    }

    public boolean isRemoved() {
        return removed;
    }

    public void setRemoved(boolean removed) {
        this.removed = removed;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
