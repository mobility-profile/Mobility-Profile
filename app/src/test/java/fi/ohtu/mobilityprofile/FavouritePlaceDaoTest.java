package fi.ohtu.mobilityprofile;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import fi.ohtu.mobilityprofile.domain.FavouritePlace;
import fi.ohtu.mobilityprofile.data.FavouritePlaceDao;

import java.util.List;

import static org.junit.Assert.*;

@RunWith(RobolectricTestRunner.class)
@Config(manifest = "src/main/AndroidManifestTest.xml", constants = BuildConfig.class, sdk = 21)
public class FavouritePlaceDaoTest {
    private FavouritePlaceDao favouriteDao;
    private FavouritePlace fav;

    @Before
    public void setUp() {
        favouriteDao = new FavouritePlaceDao();
        fav = new FavouritePlace("Kumpulan kampus", "Kumpulan kampus");
        fav.setCounter(10);
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

    @Test
    public void getFavouritePlaceNamesTest() {
        favouriteDao.insertFavouritePlace(new FavouritePlace("koti", "Kotitie 10"));
        favouriteDao.insertFavouritePlace(new FavouritePlace("koulu", "Kumpula"));
        
        assertEquals(favouriteDao.getNamesOfFavouritePlaces().size(), 2);
        assertEquals(favouriteDao.getNamesOfFavouritePlaces().get(0), "koti");
    }

    @Test
    public void testDeleteById() {
        FavouritePlace favouritePlace = new FavouritePlace("Jee", "Kumpula");
        favouriteDao.insertFavouritePlace(favouritePlace);

        assertEquals(favouritePlace.getName(), favouriteDao.getFavouritePlaceByName("Jee").getName());

        favouriteDao.deleteFavouritePlaceById(favouritePlace.getId());

        assertEquals(null, favouriteDao.getFavouritePlaceByName("Jee"));
    }

    @Test
    public void testDeleteAll() {
        favouriteDao.insertFavouritePlace(new FavouritePlace("koti", "Kotitie 10"));
        favouriteDao.insertFavouritePlace(new FavouritePlace("koulu", "Kumpula"));

        assertEquals(favouriteDao.getNamesOfFavouritePlaces().size(), 2);

        FavouritePlaceDao.deleteAllData();

        assertEquals(favouriteDao.getNamesOfFavouritePlaces().size(), 0);
    }
    
    @Test
    public void testFindAllOrderByCounter() {
        favouriteDao.insertFavouritePlace(fav);
        favouriteDao.insertFavouritePlace(new FavouritePlace("Koti", "kotitie 5"));
        List<FavouritePlace> places = favouriteDao.FindAmountOrderByCounter(3);
        
        assertEquals("Kumpulan kampus", places.get(0).getName());
    }
    
}
