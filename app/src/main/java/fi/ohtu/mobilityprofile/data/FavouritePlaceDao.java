package fi.ohtu.mobilityprofile.data;

import com.orm.query.Condition;
import com.orm.query.Select;

import java.nio.channels.SelectableChannel;
import java.util.List;

/**
 * DAO used for saving and reading FavouritePlaces to/from database.
 */
public class FavouritePlaceDao {

    /**
     * Returns all the user's favourite places.
     * @return
     */
    public List<FavouritePlace> getAllFavouritePlaces() {
        return FavouritePlace.listAll(FavouritePlace.class);
    }

    /**
     * Returns a favourite place where the name matches the given one.
     * @param name name of the favourite place
     * @return Favourite place
     */
    public FavouritePlace getFavouritePlaceByName(String name) {
        List<FavouritePlace> favourites = Select.from(FavouritePlace.class)
                .where(Condition.prop("name").eq(name))
                .limit("1")
                .list();

        assert favourites.size() <= 1 : "Invalid SQL query: only one or zero entities should have been returned!";

        if (favourites.size() == 0) {
            return null;
        }
        return favourites.get(0);
    }

    /**
     * Saves a favourite place in the database.
     * @param favourite Favourite place to be saved
     */
    public void insertFavouritePlace(FavouritePlace favourite) {
        favourite.save();
    }

    /**
     * Deletes one favourite place from the database.
     * @param name name of the favourite place
     */
    public void deleteFavouritePlace(String name) {
        List<FavouritePlace> favourites = Select.from(FavouritePlace.class)
                .where(Condition.prop("name").eq(name))
                .limit("1")
                .list();

        assert favourites.size() <= 1 : "Invalid SQL query: only one or zero entities should have been returned!";

        if (favourites.size() == 1) {
            FavouritePlace fav = favourites.get(0);
            fav.delete();
        }
    }

    /**
     * Deletes all favourite places from the database.
     */
    public void deleteAllData() {
        FavouritePlace.deleteAll(FavouritePlace.class);
    }
}