package fi.ohtu.mobilityprofile.domain;

import com.orm.SugarRecord;

import org.json.JSONObject;

/**
 * Class is used to save the user's favourite places.
 */
public class FavouritePlace extends SugarRecord implements SignificantPlace{
    String name;
    String address;
    Coordinate coordinate;
    int counter;

    /**
     *
     */
    public FavouritePlace() {

    }

    /**
     * Creates FavouritePlace.
     * @param name the name the user has given to this location.
     * @param address the address of the location.
     */
    public FavouritePlace(String name, String address) {
        this.name = name;
        this.address = address;
        this.counter = 1;
    }

    public FavouritePlace(String name, String address, Coordinate coordinate) {
        this.name = name;
        this.address = address;
        this.coordinate = coordinate;
        this.counter = 1;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public Coordinate getCoordinate() {
        return coordinate;
    }
    
    public int getCounter() {
        return counter;
    }
    
    public void increaseCounter() {
        counter++;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setCoordinate(Coordinate coordinate) {
        this.coordinate = coordinate;
    }

    public void setCounter(int count) {
        this.counter = count;
    }

    @Override
    public String toString() {
        return this.name + ": " + this.address;
    }

    @Override
    public Long getID() {
        return this.getId();
    }
}
