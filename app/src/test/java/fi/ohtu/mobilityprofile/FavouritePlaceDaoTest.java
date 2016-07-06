package fi.ohtu.mobilityprofile;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

<<<<<<< HEAD
import fi.ohtu.mobilityprofile.data.FavouritePlace;
import fi.ohtu.mobilityprofile.data.FavouritePlaceDao;

import static org.junit.Assert.*;

@RunWith(RobolectricTestRunner.class)
@Config(manifest = "src/main/AndroidManifest.xml", constants = BuildConfig.class, sdk = 21)
public class FavouritePlaceDaoTest {
    private FavouritePlaceDao favouriteDao;
    private FavouritePlace fav;

    @Before
    public void setUp() {
        favouriteDao = new FavouritePlaceDao();
        fav = new FavouritePlace("Kumpulan kampus", "Kumpulan kampus");
        Robolectric.setupActivity(MainActivityStub.class);
    }

    @Test
    public void testInsertAndFind() {

        favouriteDao.insertFavouritePlace(fav);
        FavouritePlace result = favouriteDao.getFavouritePlaceByName("Kumpulan kampus");
        assertTrue(result != null);
        assertEquals(fav.getName(), result.getName());
    }

    @Test
    public void testFindUnknown() {
        assertEquals(null, favouriteDao.getFavouritePlaceByName("Kumpula"));

        favouriteDao.insertFavouritePlace(fav);

        assertEquals(null, favouriteDao.getFavouritePlaceByName("asf"));
    }

    @Test
    public void testFindAllFavourites() {
        favouriteDao.insertFavouritePlace(fav);
        favouriteDao.insertFavouritePlace(new FavouritePlace("Rautatieasema", "Päärautatieasema"));
        favouriteDao.insertFavouritePlace(new FavouritePlace("Kauppa", "Kauppakatu 1"));

        assertEquals(3, favouriteDao.getAllFavouritePlaces().size());
    }

    @Test
    public void testDeleteFavourite() {
        favouriteDao.insertFavouritePlace(fav);
        assertEquals(fav.getName(), favouriteDao.getFavouritePlaceByName("Kumpulan kampus").getName());
        favouriteDao.deleteFavouritePlace("Kumpulan kampus");
        assertTrue(favouriteDao.getFavouritePlaceByName("Kumpulan kampus") == null);

    }
}
=======
import java.util.List;

import static org.junit.Assert.*;

import fi.ohtu.mobilityprofile.data.FavouritePlace;
import fi.ohtu.mobilityprofile.data.FavouritePlaceDao;

@RunWith(RobolectricTestRunner.class)
@Config(manifest = "src/main/AndroidManifest.xml", constants = BuildConfig.class, sdk = 21)
public class FavouritePlaceDaoTest {
    
    private FavouritePlaceDao favouritePlaceDao;
    
    @Before
    public void setup() {
        this.favouritePlaceDao = new FavouritePlaceDao();
        Robolectric.setupActivity(MainActivityStub.class);
    }
    
    @Test
    public void insertFavouritePlaceTest() {
        // favouritePlaceDao.insertFavouritePlace(new FavouritePlace("koti", "Kotitie 10"));
        FavouritePlace fav = new FavouritePlace("koti", "Kotitie 10");
        fav.save();
        
        assertEquals(favouritePlaceDao.getFavouritePlaceByName("koti").getAddress(), "Kotitie 10");
    }
    
    @Test
    public void getFavouritePlaceNamesTest() {
        favouritePlaceDao.insertFavouritePlace(new FavouritePlace("koti", "Kotitie 10"));
        favouritePlaceDao.insertFavouritePlace(new FavouritePlace("koulu", "Kumpula"));
        
        assertEquals(favouritePlaceDao.getNamesOfFavouritePlaces().size(), 2);
        assertEquals(favouritePlaceDao.getNamesOfFavouritePlaces().get(0), "koti");
    }
}
>>>>>>> tietokanta
