package fi.ohtu.mobilityprofile.domain;

/**
 *
 */
public interface HasCoordinate {
    public Coordinate getCoordinate();
    public double distanceTo(HasCoordinate hasCoordinate);
}
