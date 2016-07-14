package fi.ohtu.mobilityprofile.domain;

import com.orm.SugarRecord;

/**
 * Class is used to save the user's favourite places.
 */
public class FavouritePlace extends SugarRecord {
    String name;
    String address;
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

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
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
    
    public void setCounter(int count) {
        this.counter = count;
    }

    @Override
    public String toString() {
        return this.name + ": " + this.address;
    }
}
