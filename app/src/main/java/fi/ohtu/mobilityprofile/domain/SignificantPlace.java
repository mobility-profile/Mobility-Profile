package fi.ohtu.mobilityprofile.domain;

import com.orm.SugarRecord;

/**
 * Class is used to save places assumed significant to the user (ie. places where he/she spends some
 * time and not just points on the road).
 */
public class SignificantPlace extends SugarRecord implements HasAddress {
    private String name;
    private String address;
    private Coordinate coordinate;

    /**
     *
     */
    public SignificantPlace() {
    }

    /**
     * Creates SignificantPlace.
     * @param coordinate Coordinate object representing the coordinates of the place
     */
    public SignificantPlace(String name, String address, Coordinate coordinate) {
        this.name = name;
        this.address = address;
        this.coordinate = coordinate;
    }

    @Override
    public Coordinate getCoordinate() { return this.coordinate; }

    public String getAddress() { return address; }

    public String getName() { return name; }

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
}
