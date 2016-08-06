package fi.ohtu.mobilityprofile.data;

import com.orm.query.Condition;
import com.orm.query.Select;

import java.util.List;
import java.util.ArrayList;

import fi.ohtu.mobilityprofile.domain.FavouritePlace;

/**
 * DAO used for saving and reading FavouritePlaces to/from database.
 */
public class FavouritePlaceDao {

    /**
     * Returns all the user's favourite places.
     *
     * @return list of all favourite places
     */
    public static List<FavouritePlace> getAllFavouritePlaces() {
        return FavouritePlace.listAll(FavouritePlace.class);
    }

    /**
     * Returns all the users's favourite places order by counter of favourite place.
     * @param limit the maximum number of favourite places to search for
     * @return List of all favourite places, the biggest counter is first.
     */
    public static List<FavouritePlace> FindAmountOrderByCounter(int limit) {
        List<FavouritePlace> favouritePlaces = Select.from(FavouritePlace.class)
                .orderBy("counter DESC")
                .limit(limit + "")
                .list();

        return favouritePlaces;
    }

    /**
     * Returns a list of names of favourite places.
     *
     * @return string array list
     */
    public static ArrayList<String> getNamesOfFavouritePlaces() {
        List<FavouritePlace> places = FavouritePlace.listAll(FavouritePlace.class);
        ArrayList<String> names = new ArrayList<String>();

        for (FavouritePlace fav : places) {
            names.add(fav.getName());
        }

        return names;
    }

    /**
     * Returns a favourite place where the name matches the given one.
     *
     * @param name name of the favourite place
     * @return Favourite place
     */
    public static FavouritePlace getFavouritePlaceByName(String name) {
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
     * Returns a favourite place where the address matches the given one.
     *
     * @param address address of the favourite place
     * @return favourite place
     */
    public static FavouritePlace findFavouritePlaceByAddress(String address) {
        List<FavouritePlace> favourites = Select.from(FavouritePlace.class)
                .where(Condition.prop("address").eq(address))
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
     *
     * @param favourite Favourite place to be saved
     */
    public static void insertFavouritePlace(FavouritePlace favourite) {
        favourite.save();
    }

    /**
     * Deletes one favourite place from the database.
     *
     * @param name name of the favourite place
     */
    public static void deleteFavouritePlace(String name) {
        FavouritePlace.deleteAll(FavouritePlace.class, "name = ?", name);
    }

    /**
     * Deletes one favourite place from the database by id.
     *
     * @param id id of the favourite place
     */
    public static void deleteFavouritePlaceById(Long id) {
        List<FavouritePlace> favourites = Select.from(FavouritePlace.class)
                .where(Condition.prop("id").eq(id))
                .limit("1")
                .list();

        assert favourites.size() <= 1 : "Invalid SQL query: only one or zero entities should have been returned!";

        if (favourites.size() == 1) {
            favourites.get(0).delete();
        }

    }

    /**
     * Deletes all favourite places from the database.
     */
    public static void deleteAllData() {
        FavouritePlace.deleteAll(FavouritePlace.class);
    }
}
