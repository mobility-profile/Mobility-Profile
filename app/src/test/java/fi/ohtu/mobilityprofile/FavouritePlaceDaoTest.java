package fi.ohtu.mobilityprofile;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

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