package fi.ohtu.mobilityprofile.SuggestionTests;

import android.content.Context;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.TimeZone;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static org.junit.Assert.*;

import fi.ohtu.mobilityprofile.BuildConfig;
import fi.ohtu.mobilityprofile.MainActivity;
import fi.ohtu.mobilityprofile.MainActivityStub;
import fi.ohtu.mobilityprofile.data.GpsPointDao;
import fi.ohtu.mobilityprofile.data.PlaceDao;
import fi.ohtu.mobilityprofile.data.VisitDao;
import fi.ohtu.mobilityprofile.domain.Coordinate;
import fi.ohtu.mobilityprofile.domain.GpsPoint;
import fi.ohtu.mobilityprofile.domain.Place;
import fi.ohtu.mobilityprofile.suggestions.locationHistory.GpsPointClusterizer;

/**
 * To create more test data run Gps logger (https://play.google.com/store/apps/details?id=com.mendhak.Gpslogger&hl=en)
 * and edit resulting gpx file with Atom or other regex capable text editor by running these commands:
 * find: <course>[\s\S]*?</course>|<speed>[\s\S]*?</speed>|<geoidheight>[\s\S]*?</geoidheight>|<src>[\s\S]*?</src>|<sat>[\s\S]*?</sat>|<hdop>[\s\S]*?</hdop>|<vdop>[\s\S]*?</vdop>|<pdop>[\s\S]*?</pdop>|<ele>[\s\S]*?</ele>
 * replace:
 * find: (<trkpt[\s\S]*?>)(<time[\s\S]*?</trkpt>)
 * replace: $2 $1
 * find: <time>([\s\S]*?)</time></trkpt> <trkpt
 * replace: new GpsPoint(getTimeStamp("$1"),
 * find: lat="([\S]*?)" lon="([\S]*?)">
 * replace: $1f, $2f),
 */

@RunWith(RobolectricTestRunner.class)
@Config(manifest = "src/main/AndroidManifestTest.xml", constants = BuildConfig.class, sdk = 21)
public class GpsPointClusterizerTest {

    private class TestObject {
        private List<Coordinate> correctCoordinates;
        private List<GpsPoint> testData;

        public TestObject(List<Coordinate> correctCoordinates, List<GpsPoint> testData) {
            this.correctCoordinates = correctCoordinates;
            this.testData = testData;
        }

        public List<Coordinate> getCorrectCoordinates() {
            return this.correctCoordinates;
        }

        public List<GpsPoint> getTestData() {
            return this.testData;
        }
    }

    private Context context;
    private VisitDao visitDao;
    private PlaceDao placeDao;

    private List<TestObject> testObjects;

    @Before
    public void setUp() {

        this.context = Robolectric.setupActivity(MainActivityStub.class);
        this.visitDao = new VisitDao();
        this.placeDao = new PlaceDao();
        this.testObjects = new ArrayList<TestObject>() {{
            add(
                    new TestObject(

                            // Caloniuksenkatu 9, Mannerheimintie 3, Kaivokatu 1, Tallinnanaukio 3, Lokitie 30, Myrskyläntie 22, Katariina Saksilaisen katu 11, Siltavoudintie 19
                            new ArrayList<Coordinate>() {{
                                add(new Coordinate(60.174549f, 24.921098f));
                                add(new Coordinate(60.169451f, 24.940711f));
                                //add(new Coordinate(60.170684f, 24.940258f));
                                add(new Coordinate(60.210944f, 25.080121f));
                                add(new Coordinate(60.209643f, 25.127018f));
                                add(new Coordinate(60.224398f, 24.969551f));
                                add(new Coordinate(60.214679f, 24.985255f));
                                add(new Coordinate(60.234701f, 24.966538f));
                            }},

                            deleteRandomPlacesFromList(createListOfPlaces(
                                    new GpsPoint(getTimeStamp("2016-07-27T06:58:38Z"), 0, 60.174633f, 24.9209466f),
                                    new GpsPoint(getTimeStamp("2016-07-27T07:02:03Z"), 0, 60.1745624f, 24.9209915f),
                                    new GpsPoint(getTimeStamp("2016-07-27T07:03:35Z"), 0, 60.1746326f, 24.9210397f),
                                    new GpsPoint(getTimeStamp("2016-07-27T07:05:18Z"), 0, 60.1746341f, 24.9209779f),
                                    new GpsPoint(getTimeStamp("2016-07-27T07:08:48Z"), 0, 60.1744503f, 24.9211184f),
                                    new GpsPoint(getTimeStamp("2016-07-27T07:10:14Z"), 0, 60.1745926f, 24.9210411f),
                                    new GpsPoint(getTimeStamp("2016-07-27T07:12:34Z"), 0, 60.1746042f, 24.9210429f),
                                    new GpsPoint(getTimeStamp("2016-07-27T07:17:34Z"), 0, 60.1746191f, 24.9210321f),
                                    new GpsPoint(getTimeStamp("2016-07-27T07:27:55Z"), 0, 60.1746613f, 24.9210688f),
                                    new GpsPoint(getTimeStamp("2016-07-27T07:29:15Z"), 0, 60.1746622f, 24.9210497f),
                                    new GpsPoint(getTimeStamp("2016-07-27T07:30:16Z"), 0, 60.1746625f, 24.9210684f),
                                    new GpsPoint(getTimeStamp("2016-07-27T07:31:17Z"), 0, 60.1746537f, 24.921052f),
                                    new GpsPoint(getTimeStamp("2016-07-27T07:32:35Z"), 0, 60.1746608f, 24.9210527f),
                                    new GpsPoint(getTimeStamp("2016-07-27T07:34:14Z"), 0, 60.1746613f, 24.9210688f),
                                    new GpsPoint(getTimeStamp("2016-07-27T07:35:15Z"), 0, 60.1746613f, 24.9210688f),
                                    new GpsPoint(getTimeStamp("2016-07-27T07:36:16Z"), 0, 60.1746613f, 24.9210688f),
                                    new GpsPoint(getTimeStamp("2016-07-27T07:37:34Z"), 0, 60.1746681f, 24.9210674f),
                                    new GpsPoint(getTimeStamp("2016-07-27T07:38:36Z"), 0, 60.1746289f, 24.920997f),
                                    new GpsPoint(getTimeStamp("2016-07-27T07:39:52Z"), 0, 60.1745948f, 24.9209641f),
                                    new GpsPoint(getTimeStamp("2016-07-27T07:52:55Z"), 0, 60.1746391f, 24.9210231f),
                                    new GpsPoint(getTimeStamp("2016-07-27T07:57:54Z"), 0, 60.1746116f, 24.9210547f),
                                    new GpsPoint(getTimeStamp("2016-07-27T08:03:35Z"), 0, 60.1746786f, 24.9210858f),
                                    new GpsPoint(getTimeStamp("2016-07-27T08:12:35Z"), 0, 60.1746633f, 24.9210536f),
                                    new GpsPoint(getTimeStamp("2016-07-27T08:17:34Z"), 0, 60.1746557f, 24.9210599f),
                                    new GpsPoint(getTimeStamp("2016-07-27T08:22:35Z"), 0, 60.1746686f, 24.9210777f),
                                    new GpsPoint(getTimeStamp("2016-07-27T08:27:34Z"), 0, 60.1746665f, 24.921088f),
                                    new GpsPoint(getTimeStamp("2016-07-27T08:32:34Z"), 0, 60.1746594f, 24.921043f),
                                    new GpsPoint(getTimeStamp("2016-07-27T08:37:34Z"), 0, 60.174674f, 24.9210541f),
                                    new GpsPoint(getTimeStamp("2016-07-27T08:42:34Z"), 0, 60.1746575f, 24.9210705f),
                                    new GpsPoint(getTimeStamp("2016-07-27T08:47:35Z"), 0, 60.174678f, 24.9210815f),
                                    new GpsPoint(getTimeStamp("2016-07-27T08:52:55Z"), 0, 60.1746792f, 24.9211038f),
                                    new GpsPoint(getTimeStamp("2016-07-27T08:57:34Z"), 0, 60.1746802f, 24.9210885f),
                                    new GpsPoint(getTimeStamp("2016-07-27T09:02:45Z"), 0, 60.17515368357197f, 24.922616244373724f),
                                    new GpsPoint(getTimeStamp("2016-07-27T09:07:35Z"), 0, 60.1741947f, 24.9327942f),
                                    new GpsPoint(getTimeStamp("2016-07-27T09:12:44Z"), 0, 60.17059513513583f, 24.9370966279521f),
                                    new GpsPoint(getTimeStamp("2016-07-27T09:18:16Z"), 0, 60.169498f, 24.9404239f),
                                    new GpsPoint(getTimeStamp("2016-07-27T09:22:55Z"), 0, 60.1695103f, 24.9403907f),
                                    new GpsPoint(getTimeStamp("2016-07-27T09:27:36Z"), 0, 60.1694834f, 24.9403108f),
                                    new GpsPoint(getTimeStamp("2016-07-27T09:32:36Z"), 0, 60.1695056f, 24.9404729f),
                                    new GpsPoint(getTimeStamp("2016-07-27T09:37:55Z"), 0, 60.1706229f, 24.9403399f),
                                    new GpsPoint(getTimeStamp("2016-07-27T09:42:35Z"), 0, 60.1706879f, 24.9395516f),
                                    new GpsPoint(getTimeStamp("2016-07-27T09:43:44Z"), 0, 60.1706919f, 24.9395599f),
                                    new GpsPoint(getTimeStamp("2016-07-27T09:44:45Z"), 0, 60.1706925f, 24.939586f),
                                    new GpsPoint(getTimeStamp("2016-07-27T09:46:19Z"), 0, 60.1706893f, 24.9395817f),
                                    new GpsPoint(getTimeStamp("2016-07-27T09:47:35Z"), 0, 60.1706925f, 24.9395646f),
                                    new GpsPoint(getTimeStamp("2016-07-27T09:49:14Z"), 0, 60.1706906f, 24.9395669f),
                                    new GpsPoint(getTimeStamp("2016-07-27T09:50:15Z"), 0, 60.1706897f, 24.9395704f),
                                    new GpsPoint(getTimeStamp("2016-07-27T09:52:42Z"), 0, 60.1698417f, 24.9459814f),
                                    new GpsPoint(getTimeStamp("2016-07-27T09:55:04Z"), 0, 60.1875875f, 24.9616515f),
                                    new GpsPoint(getTimeStamp("2016-07-27T10:02:55Z"), 0, 60.2043921f, 25.0432578f),
                                    new GpsPoint(getTimeStamp("2016-07-27T10:07:36Z"), 0, 60.2104967f, 25.0795844f),
                                    new GpsPoint(getTimeStamp("2016-07-27T10:12:36Z"), 0, 60.2115116f, 25.0822428f),
                                    new GpsPoint(getTimeStamp("2016-07-27T10:17:36Z"), 0, 60.2102777f, 25.0797745f),
                                    new GpsPoint(getTimeStamp("2016-07-27T10:22:39Z"), 0, 60.209331954671356f, 25.076194349261733f),
                                    new GpsPoint(getTimeStamp("2016-07-27T10:27:36Z"), 0, 60.2064083f, 25.0768823f),
                                    new GpsPoint(getTimeStamp("2016-07-27T10:37:35Z"), 0, 60.2073393f, 25.1240485f),
                                    new GpsPoint(getTimeStamp("2016-07-27T10:42:36Z"), 0, 60.2094753f, 25.1271599f),
                                    new GpsPoint(getTimeStamp("2016-07-27T10:47:34Z"), 0, 60.2094645f, 25.1271627f),
                                    new GpsPoint(getTimeStamp("2016-07-27T10:48:55Z"), 0, 60.2094632f, 25.1271611f),
                                    new GpsPoint(getTimeStamp("2016-07-27T10:52:34Z"), 0, 60.2094597f, 25.1271604f),
                                    new GpsPoint(getTimeStamp("2016-07-27T10:57:36Z"), 0, 60.2094635f, 25.1271615f),
                                    new GpsPoint(getTimeStamp("2016-07-27T11:02:36Z"), 0, 60.2094576f, 25.1271616f),
                                    new GpsPoint(getTimeStamp("2016-07-27T11:07:35Z"), 0, 60.2094531f, 25.1271572f),
                                    new GpsPoint(getTimeStamp("2016-07-27T11:08:36Z"), 0, 60.2094807f, 25.1271599f),
                                    new GpsPoint(getTimeStamp("2016-07-27T11:09:37Z"), 0, 60.209459f, 25.1271603f),
                                    new GpsPoint(getTimeStamp("2016-07-27T11:10:39Z"), 0, 60.2094791f, 25.1271612f),
                                    new GpsPoint(getTimeStamp("2016-07-27T11:11:50Z"), 0, 60.2094805f, 25.1271572f),
                                    new GpsPoint(getTimeStamp("2016-07-27T11:13:14Z"), 0, 60.209456f, 25.127158f),
                                    new GpsPoint(getTimeStamp("2016-07-27T11:14:15Z"), 0, 60.2094792f, 25.1271609f),
                                    new GpsPoint(getTimeStamp("2016-07-27T11:15:16Z"), 0, 60.209466f, 25.1271578f),
                                    new GpsPoint(getTimeStamp("2016-07-27T11:16:32Z"), 0, 60.2094645f, 25.1271574f),
                                    new GpsPoint(getTimeStamp("2016-07-27T11:17:34Z"), 0, 60.2094512f, 25.1271613f),
                                    new GpsPoint(getTimeStamp("2016-07-27T11:19:14Z"), 0, 60.2094645f, 25.1271574f),
                                    new GpsPoint(getTimeStamp("2016-07-27T11:20:26Z"), 0, 60.2094507f, 25.1271591f),
                                    new GpsPoint(getTimeStamp("2016-07-27T11:22:35Z"), 0, 60.2094742f, 25.1271595f),
                                    new GpsPoint(getTimeStamp("2016-07-27T11:27:34Z"), 0, 60.2094743f, 25.1271621f),
                                    new GpsPoint(getTimeStamp("2016-07-27T11:32:36Z"), 0, 60.209466f, 25.1271578f),
                                    new GpsPoint(getTimeStamp("2016-07-27T11:37:35Z"), 0, 60.2094625f, 25.1271574f),
                                    new GpsPoint(getTimeStamp("2016-07-27T11:42:34Z"), 0, 60.209466f, 25.1271578f),
                                    new GpsPoint(getTimeStamp("2016-07-27T11:47:36Z"), 0, 60.209452f, 25.1271591f),
                                    new GpsPoint(getTimeStamp("2016-07-27T11:52:35Z"), 0, 60.2094625f, 25.1271574f),
                                    new GpsPoint(getTimeStamp("2016-07-27T11:57:34Z"), 0, 60.2094687f, 25.1271601f),
                                    new GpsPoint(getTimeStamp("2016-07-27T11:58:36Z"), 0, 60.209463f, 25.1271572f),
                                    new GpsPoint(getTimeStamp("2016-07-27T11:59:55Z"), 0, 60.209458f, 25.1271614f),
                                    new GpsPoint(getTimeStamp("2016-07-27T12:02:34Z"), 0, 60.2094646f, 25.1271578f),
                                    new GpsPoint(getTimeStamp("2016-07-27T12:04:14Z"), 0, 60.209475f, 25.1271591f),
                                    new GpsPoint(getTimeStamp("2016-07-27T12:05:15Z"), 0, 60.2094742f, 25.1271595f),
                                    new GpsPoint(getTimeStamp("2016-07-27T12:06:16Z"), 0, 60.2094676f, 25.1271559f),
                                    new GpsPoint(getTimeStamp("2016-07-27T12:07:34Z"), 0, 60.20947f, 25.1271595f),
                                    new GpsPoint(getTimeStamp("2016-07-27T12:08:36Z"), 0, 60.2094541f, 25.1271595f),
                                    new GpsPoint(getTimeStamp("2016-07-27T12:10:14Z"), 0, 60.209466f, 25.1271578f),
                                    new GpsPoint(getTimeStamp("2016-07-27T12:11:32Z"), 0, 60.2094645f, 25.1271559f),
                                    new GpsPoint(getTimeStamp("2016-07-27T12:12:35Z"), 0, 60.209469f, 25.1271584f),
                                    new GpsPoint(getTimeStamp("2016-07-27T12:13:36Z"), 0, 60.2094749f, 25.1271575f),
                                    new GpsPoint(getTimeStamp("2016-07-27T12:14:37Z"), 0, 60.2094641f, 25.1271573f),
                                    new GpsPoint(getTimeStamp("2016-07-27T12:16:14Z"), 0, 60.2094632f, 25.127161f),
                                    new GpsPoint(getTimeStamp("2016-07-27T12:17:34Z"), 0, 60.2094674f, 25.127157f),
                                    new GpsPoint(getTimeStamp("2016-07-27T12:19:14Z"), 0, 60.209454f, 25.1271577f),
                                    new GpsPoint(getTimeStamp("2016-07-27T12:20:15Z"), 0, 60.209472f, 25.1271594f),
                                    new GpsPoint(getTimeStamp("2016-07-27T12:21:16Z"), 0, 60.2094581f, 25.1271603f),
                                    new GpsPoint(getTimeStamp("2016-07-27T12:22:34Z"), 0, 60.2094554f, 25.1271619f),
                                    new GpsPoint(getTimeStamp("2016-07-27T12:23:41Z"), 0, 60.209459f, 25.1271603f),
                                    new GpsPoint(getTimeStamp("2016-07-27T12:25:14Z"), 0, 60.2094521f, 25.1271594f),
                                    new GpsPoint(getTimeStamp("2016-07-27T12:26:15Z"), 0, 60.2094483f, 25.1271573f),
                                    new GpsPoint(getTimeStamp("2016-07-27T12:27:34Z"), 0, 60.2094514f, 25.1271591f),
                                    new GpsPoint(getTimeStamp("2016-07-27T12:28:36Z"), 0, 60.2094547f, 25.1271618f),
                                    new GpsPoint(getTimeStamp("2016-07-27T12:30:01Z"), 0, 60.2094648f, 25.1271573f),
                                    new GpsPoint(getTimeStamp("2016-07-27T12:31:14Z"), 0, 60.2094613f, 25.1271569f),
                                    new GpsPoint(getTimeStamp("2016-07-27T12:32:34Z"), 0, 60.2094492f, 25.1271591f),
                                    new GpsPoint(getTimeStamp("2016-07-27T12:33:36Z"), 0, 60.20947f, 25.1271595f),
                                    new GpsPoint(getTimeStamp("2016-07-27T12:35:20Z"), 0, 60.2094496f, 25.1271586f),
                                    new GpsPoint(getTimeStamp("2016-07-27T12:36:21Z"), 0, 60.2094534f, 25.1271608f),
                                    new GpsPoint(getTimeStamp("2016-07-27T12:37:34Z"), 0, 60.2094484f, 25.1271574f),
                                    new GpsPoint(getTimeStamp("2016-07-27T12:38:36Z"), 0, 60.2094521f, 25.1271594f),
                                    new GpsPoint(getTimeStamp("2016-07-27T12:40:14Z"), 0, 60.2094707f, 25.1271574f),
                                    new GpsPoint(getTimeStamp("2016-07-27T12:41:16Z"), 0, 60.2094837f, 25.1271591f),
                                    new GpsPoint(getTimeStamp("2016-07-27T12:42:35Z"), 0, 60.2094511f, 25.1271586f),
                                    new GpsPoint(getTimeStamp("2016-07-27T12:43:36Z"), 0, 60.2094748f, 25.1271615f),
                                    new GpsPoint(getTimeStamp("2016-07-27T12:44:37Z"), 0, 60.2094665f, 25.1271578f),
                                    new GpsPoint(getTimeStamp("2016-07-27T12:46:14Z"), 0, 60.2094676f, 25.1271559f),
                                    new GpsPoint(getTimeStamp("2016-07-27T12:47:34Z"), 0, 60.2094581f, 25.1271603f),
                                    new GpsPoint(getTimeStamp("2016-07-27T12:49:12Z"), 0, 60.2094552f, 25.1271594f),
                                    new GpsPoint(getTimeStamp("2016-07-27T12:52:34Z"), 0, 60.2094601f, 25.1271594f),
                                    new GpsPoint(getTimeStamp("2016-07-27T12:53:36Z"), 0, 60.2094613f, 25.1271599f),
                                    new GpsPoint(getTimeStamp("2016-07-27T12:55:15Z"), 0, 60.2094618f, 25.1271574f),
                                    new GpsPoint(getTimeStamp("2016-07-27T12:56:16Z"), 0, 60.2094657f, 25.127159f),
                                    new GpsPoint(getTimeStamp("2016-07-27T12:57:17Z"), 0, 60.20946f, 25.1271618f),
                                    new GpsPoint(getTimeStamp("2016-07-27T12:58:19Z"), 0, 60.2094658f, 25.1271628f),
                                    new GpsPoint(getTimeStamp("2016-07-27T13:00:01Z"), 0, 60.2094618f, 25.1271574f),
                                    new GpsPoint(getTimeStamp("2016-07-27T13:01:14Z"), 0, 60.209459f, 25.1271617f),
                                    new GpsPoint(getTimeStamp("2016-07-27T13:02:59Z"), 0, 60.2094669f, 25.1271575f),
                                    new GpsPoint(getTimeStamp("2016-07-27T13:04:14Z"), 0, 60.209459f, 25.1271603f),
                                    new GpsPoint(getTimeStamp("2016-07-27T13:05:49Z"), 0, 60.2094126f, 25.1272139f),
                                    new GpsPoint(getTimeStamp("2016-07-27T13:07:10Z"), 0, 60.2094137f, 25.1272109f),
                                    new GpsPoint(getTimeStamp("2016-07-27T13:08:12Z"), 0, 60.2094484f, 25.1271574f),
                                    new GpsPoint(getTimeStamp("2016-07-27T13:09:14Z"), 0, 60.2094735f, 25.1271615f),
                                    new GpsPoint(getTimeStamp("2016-07-27T13:10:16Z"), 0, 60.2094692f, 25.1271578f),
                                    new GpsPoint(getTimeStamp("2016-07-27T13:11:17Z"), 0, 60.2094542f, 25.1271599f),
                                    new GpsPoint(getTimeStamp("2016-07-27T13:12:19Z"), 0, 60.2094652f, 25.1271628f),
                                    new GpsPoint(getTimeStamp("2016-07-27T13:13:20Z"), 0, 60.2094492f, 25.127156f),
                                    new GpsPoint(getTimeStamp("2016-07-27T13:14:57Z"), 0, 60.2094576f, 25.1271562f),
                                    new GpsPoint(getTimeStamp("2016-07-27T13:15:59Z"), 0, 60.2094492f, 25.127156f),
                                    new GpsPoint(getTimeStamp("2016-07-27T13:17:27Z"), 0, 60.2094463f, 25.1271617f),
                                    new GpsPoint(getTimeStamp("2016-07-27T13:18:29Z"), 0, 60.20935351339071f, 25.126918329333304f),
                                    new GpsPoint(getTimeStamp("2016-07-27T13:19:50Z"), 0, 60.2094651f, 25.1271565f),
                                    new GpsPoint(getTimeStamp("2016-07-27T13:20:52Z"), 0, 60.2094736f, 25.1271612f),
                                    new GpsPoint(getTimeStamp("2016-07-27T13:21:54Z"), 0, 60.2094728f, 25.127156f),
                                    new GpsPoint(getTimeStamp("2016-07-27T13:22:55Z"), 0, 60.2094746f, 25.1271622f),
                                    new GpsPoint(getTimeStamp("2016-07-27T13:23:57Z"), 0, 60.2094746f, 25.1271622f),
                                    new GpsPoint(getTimeStamp("2016-07-27T13:25:13Z"), 0, 60.2094746f, 25.1271622f),
                                    new GpsPoint(getTimeStamp("2016-07-27T13:26:39Z"), 0, 60.2094746f, 25.1271622f),
                                    new GpsPoint(getTimeStamp("2016-07-27T13:28:14Z"), 0, 60.2094659f, 25.1271594f),
                                    new GpsPoint(getTimeStamp("2016-07-27T13:29:16Z"), 0, 60.2094714f, 25.1271565f),
                                    new GpsPoint(getTimeStamp("2016-07-27T13:30:18Z"), 0, 60.2094626f, 25.1271616f),
                                    new GpsPoint(getTimeStamp("2016-07-27T13:31:20Z"), 0, 60.2094748f, 25.1271615f),
                                    new GpsPoint(getTimeStamp("2016-07-27T13:32:40Z"), 0, 60.2094538f, 25.1271594f),
                                    new GpsPoint(getTimeStamp("2016-07-27T13:34:14Z"), 0, 60.2094606f, 25.1271599f),
                                    new GpsPoint(getTimeStamp("2016-07-27T13:35:15Z"), 0, 60.2094576f, 25.1271562f),
                                    new GpsPoint(getTimeStamp("2016-07-27T13:36:17Z"), 0, 60.2094735f, 25.1271611f),
                                    new GpsPoint(getTimeStamp("2016-07-27T13:37:35Z"), 0, 60.2094764f, 25.1271622f),
                                    new GpsPoint(getTimeStamp("2016-07-27T13:38:36Z"), 0, 60.2094561f, 25.1271628f),
                                    new GpsPoint(getTimeStamp("2016-07-27T13:40:14Z"), 0, 60.2094672f, 25.127158f),
                                    new GpsPoint(getTimeStamp("2016-07-27T13:41:54Z"), 0, 60.2094577f, 25.1271586f),
                                    new GpsPoint(getTimeStamp("2016-07-27T13:43:20Z"), 0, 60.2094576f, 25.1271616f),
                                    new GpsPoint(getTimeStamp("2016-07-27T13:44:25Z"), 0, 60.209471f, 25.1271568f),
                                    new GpsPoint(getTimeStamp("2016-07-27T13:45:27Z"), 0, 60.2094576f, 25.1271617f),
                                    new GpsPoint(getTimeStamp("2016-07-27T13:46:38Z"), 0, 60.2094501f, 25.1271604f),
                                    new GpsPoint(getTimeStamp("2016-07-27T13:47:43Z"), 0, 60.2094605f, 25.1271584f),
                                    new GpsPoint(getTimeStamp("2016-07-27T13:49:20Z"), 0, 60.209465f, 25.1271628f),
                                    new GpsPoint(getTimeStamp("2016-07-27T13:50:21Z"), 0, 60.2094712f, 25.1271562f),
                                    new GpsPoint(getTimeStamp("2016-07-27T13:51:23Z"), 0, 60.2094676f, 25.1271628f),
                                    new GpsPoint(getTimeStamp("2016-07-27T13:52:35Z"), 0, 60.2094608f, 25.1271584f),
                                    new GpsPoint(getTimeStamp("2016-07-27T13:53:36Z"), 0, 60.2094588f, 25.1271572f),
                                    new GpsPoint(getTimeStamp("2016-07-27T13:54:38Z"), 0, 60.2094588f, 25.1271572f),
                                    new GpsPoint(getTimeStamp("2016-07-27T13:55:56Z"), 0, 60.2094665f, 25.1271617f),
                                    new GpsPoint(getTimeStamp("2016-07-27T13:56:58Z"), 0, 60.209459f, 25.1271617f),
                                    new GpsPoint(getTimeStamp("2016-07-27T13:58:00Z"), 0, 60.2094498f, 25.1271598f),
                                    new GpsPoint(getTimeStamp("2016-07-27T13:59:01Z"), 0, 60.2094552f, 25.1271594f),
                                    new GpsPoint(getTimeStamp("2016-07-27T14:00:24Z"), 0, 60.2094549f, 25.1271611f),
                                    new GpsPoint(getTimeStamp("2016-07-27T14:01:26Z"), 0, 60.2094542f, 25.1271599f),
                                    new GpsPoint(getTimeStamp("2016-07-27T14:02:59Z"), 0, 60.2094588f, 25.1271572f),
                                    new GpsPoint(getTimeStamp("2016-07-27T14:04:14Z"), 0, 60.209457f, 25.1271568f),
                                    new GpsPoint(getTimeStamp("2016-07-27T14:05:16Z"), 0, 60.2094484f, 25.1271574f),
                                    new GpsPoint(getTimeStamp("2016-07-27T14:06:18Z"), 0, 60.2094613f, 25.1271578f),
                                    new GpsPoint(getTimeStamp("2016-07-27T14:07:35Z"), 0, 60.2094501f, 25.1271599f),
                                    new GpsPoint(getTimeStamp("2016-07-27T14:08:36Z"), 0, 60.2094712f, 25.1271562f),
                                    new GpsPoint(getTimeStamp("2016-07-27T14:10:14Z"), 0, 60.2094154f, 25.1272122f),
                                    new GpsPoint(getTimeStamp("2016-07-27T14:11:15Z"), 0, 60.2094134f, 25.1272113f),
                                    new GpsPoint(getTimeStamp("2016-07-27T14:12:17Z"), 0, 60.2094154f, 25.1272122f),
                                    new GpsPoint(getTimeStamp("2016-07-27T14:13:23Z"), 0, 60.209414f, 25.1272175f),
                                    new GpsPoint(getTimeStamp("2016-07-27T14:14:24Z"), 0, 60.2094154f, 25.1272122f),
                                    new GpsPoint(getTimeStamp("2016-07-27T14:15:26Z"), 0, 60.2094127f, 25.1272114f),
                                    new GpsPoint(getTimeStamp("2016-07-27T14:16:28Z"), 0, 60.2094146f, 25.127211f),
                                    new GpsPoint(getTimeStamp("2016-07-27T14:17:59Z"), 0, 60.2094137f, 25.1272116f),
                                    new GpsPoint(getTimeStamp("2016-07-27T14:19:14Z"), 0, 60.2094154f, 25.1272122f),
                                    new GpsPoint(getTimeStamp("2016-07-27T14:20:15Z"), 0, 60.2094659f, 25.1271594f),
                                    new GpsPoint(getTimeStamp("2016-07-27T14:21:58Z"), 0, 60.2094672f, 25.1271615f),
                                    new GpsPoint(getTimeStamp("2016-07-27T14:23:18Z"), 0, 60.2094608f, 25.1271614f),
                                    new GpsPoint(getTimeStamp("2016-07-27T14:24:19Z"), 0, 60.209459f, 25.1271602f),
                                    new GpsPoint(getTimeStamp("2016-07-27T14:25:48Z"), 0, 60.2094573f, 25.1271594f),
                                    new GpsPoint(getTimeStamp("2016-07-27T14:26:50Z"), 0, 60.209466f, 25.1271578f),
                                    new GpsPoint(getTimeStamp("2016-07-27T14:28:12Z"), 0, 60.2094591f, 25.127161f),
                                    new GpsPoint(getTimeStamp("2016-07-27T14:29:21Z"), 0, 60.2094667500108f, 25.126914175220296f),
                                    new GpsPoint(getTimeStamp("2016-07-27T14:30:22Z"), 0, 60.2094669f, 25.1271575f),
                                    new GpsPoint(getTimeStamp("2016-07-27T14:31:32Z"), 0, 60.2094739f, 25.1271582f),
                                    new GpsPoint(getTimeStamp("2016-07-27T14:33:02Z"), 0, 60.2094695f, 25.1271595f),
                                    new GpsPoint(getTimeStamp("2016-07-27T14:34:23Z"), 0, 60.209475f, 25.1271597f),
                                    new GpsPoint(getTimeStamp("2016-07-27T14:35:08Z"), 0, 60.2094661f, 25.1271595f),
                                    new GpsPoint(getTimeStamp("2016-07-27T14:37:34Z"), 0, 60.2094799f, 25.1271567f),
                                    new GpsPoint(getTimeStamp("2016-07-27T14:42:34Z"), 0, 60.2094837f, 25.1271607f),
                                    new GpsPoint(getTimeStamp("2016-07-27T14:44:04Z"), 0, 60.2094817f, 25.1271582f),
                                    new GpsPoint(getTimeStamp("2016-07-27T14:45:06Z"), 0, 60.2094904f, 25.1271581f),
                                    new GpsPoint(getTimeStamp("2016-07-27T14:47:35Z"), 0, 60.209464f, 25.1271615f),
                                    new GpsPoint(getTimeStamp("2016-07-27T14:52:34Z"), 0, 60.2094959f, 25.1271591f),
                                    new GpsPoint(getTimeStamp("2016-07-27T14:53:59Z"), 0, 60.2094876f, 25.1271567f),
                                    new GpsPoint(getTimeStamp("2016-07-27T14:55:13Z"), 0, 60.209493f, 25.1271591f),
                                    new GpsPoint(getTimeStamp("2016-07-27T14:56:15Z"), 0, 60.2094893f, 25.1271611f),
                                    new GpsPoint(getTimeStamp("2016-07-27T14:57:30Z"), 0, 60.2094838f, 25.1271594f),
                                    new GpsPoint(getTimeStamp("2016-07-27T14:58:52Z"), 0, 60.2094829f, 25.1271607f),
                                    new GpsPoint(getTimeStamp("2016-07-27T15:02:38Z"), 0, 60.20718494978804f, 25.122619149696476f),
                                    new GpsPoint(getTimeStamp("2016-07-27T15:07:38Z"), 0, 60.206462266298075f, 25.11404414776483f),
                                    new GpsPoint(getTimeStamp("2016-07-27T15:12:35Z"), 0, 60.2064741f, 25.0765076f),
                                    new GpsPoint(getTimeStamp("2016-07-27T15:17:36Z"), 0, 60.209532313138354f, 25.077679709531292f),
                                    new GpsPoint(getTimeStamp("2016-07-27T15:19:11Z"), 0, 60.209708639613424f, 25.07759228975632f),
                                    new GpsPoint(getTimeStamp("2016-07-27T15:20:14Z"), 0, 60.20972154988354f, 25.077299348581974f),
                                    new GpsPoint(getTimeStamp("2016-07-27T15:21:18Z"), 0, 60.20961000166214f, 25.07725279412296f),
                                    new GpsPoint(getTimeStamp("2016-07-27T15:22:22Z"), 0, 60.20966102592015f, 25.073733916038858f),
                                    new GpsPoint(getTimeStamp("2016-07-27T15:23:25Z"), 0, 60.20954356162004f, 25.05975837201785f),
                                    new GpsPoint(getTimeStamp("2016-07-27T15:24:29Z"), 0, 60.212855843763684f, 25.059986412403777f),
                                    new GpsPoint(getTimeStamp("2016-07-27T15:25:43Z"), 0, 60.21818330491362f, 25.04984163654444f),
                                    new GpsPoint(getTimeStamp("2016-07-27T15:27:43Z"), 0, 60.22221553323264f, 25.03061031889116f),
                                    new GpsPoint(getTimeStamp("2016-07-27T15:42:39Z"), 0, 60.22666702370455f, 24.969411065528277f),
                                    new GpsPoint(getTimeStamp("2016-07-27T15:47:25Z"), 0, 60.2243933f, 24.9697628f),
                                    new GpsPoint(getTimeStamp("2016-07-27T15:53:16Z"), 0, 60.2246135f, 24.9696872f),
                                    new GpsPoint(getTimeStamp("2016-07-27T16:02:35Z"), 0, 60.2242592f, 24.969686f),
                                    new GpsPoint(getTimeStamp("2016-07-27T16:07:35Z"), 0, 60.2243216f, 24.9696765f),
                                    new GpsPoint(getTimeStamp("2016-07-27T16:12:33Z"), 0, 60.2242611f, 24.9696844f),
                                    new GpsPoint(getTimeStamp("2016-07-27T16:17:34Z"), 0, 60.2242713f, 24.9696861f),
                                    new GpsPoint(getTimeStamp("2016-07-27T16:22:34Z"), 0, 60.224322f, 24.9696762f),
                                    new GpsPoint(getTimeStamp("2016-07-27T16:27:33Z"), 0, 60.2242604f, 24.9696821f),
                                    new GpsPoint(getTimeStamp("2016-07-27T16:32:34Z"), 0, 60.224267f, 24.9696811f),
                                    new GpsPoint(getTimeStamp("2016-07-27T16:37:34Z"), 0, 60.224279f, 24.9696839f),
                                    new GpsPoint(getTimeStamp("2016-07-27T16:42:33Z"), 0, 60.2242696f, 24.9696805f),
                                    new GpsPoint(getTimeStamp("2016-07-27T16:47:33Z"), 0, 60.2242097f, 24.9696859f),
                                    new GpsPoint(getTimeStamp("2016-07-27T16:52:33Z"), 0, 60.2245541f, 24.9696372f),
                                    new GpsPoint(getTimeStamp("2016-07-27T16:57:33Z"), 0, 60.2242615f, 24.9696811f),
                                    new GpsPoint(getTimeStamp("2016-07-27T17:02:32Z"), 0, 60.2247192f, 24.969579f),
                                    new GpsPoint(getTimeStamp("2016-07-27T17:07:33Z"), 0, 60.2248005f, 24.9695429f),
                                    new GpsPoint(getTimeStamp("2016-07-27T17:12:34Z"), 0, 60.2247989f, 24.9695443f),
                                    new GpsPoint(getTimeStamp("2016-07-27T17:17:33Z"), 0, 60.2247985f, 24.9695477f),
                                    new GpsPoint(getTimeStamp("2016-07-27T17:22:34Z"), 0, 60.224797f, 24.9695514f),
                                    new GpsPoint(getTimeStamp("2016-07-27T17:27:33Z"), 0, 60.2247999f, 24.9695441f),
                                    new GpsPoint(getTimeStamp("2016-07-27T17:32:33Z"), 0, 60.2248026f, 24.9695348f),
                                    new GpsPoint(getTimeStamp("2016-07-27T17:37:32Z"), 0, 60.2247024f, 24.9696427f),
                                    new GpsPoint(getTimeStamp("2016-07-27T17:42:34Z"), 0, 60.2248093f, 24.9695232f),
                                    new GpsPoint(getTimeStamp("2016-07-27T17:47:37Z"), 0, 60.22445695771647f, 24.96984383283115f),
                                    new GpsPoint(getTimeStamp("2016-07-27T17:52:33Z"), 0, 60.22456746073795f, 24.97004172205639f),
                                    new GpsPoint(getTimeStamp("2016-07-27T17:53:58Z"), 0, 60.2247995f, 24.9695461f),
                                    new GpsPoint(getTimeStamp("2016-07-27T17:57:34Z"), 0, 60.2247372f, 24.9695773f),
                                    new GpsPoint(getTimeStamp("2016-07-27T18:02:33Z"), 0, 60.2247939f, 24.9695528f),
                                    new GpsPoint(getTimeStamp("2016-07-27T18:07:34Z"), 0, 60.2247984f, 24.9695454f),
                                    new GpsPoint(getTimeStamp("2016-07-27T18:12:39Z"), 0, 60.22446458593199f, 24.970067032072837f),
                                    new GpsPoint(getTimeStamp("2016-07-27T18:17:32Z"), 0, 60.2248067f, 24.9695273f),
                                    new GpsPoint(getTimeStamp("2016-07-27T18:27:33Z"), 0, 60.2248088f, 24.9695426f),
                                    new GpsPoint(getTimeStamp("2016-07-27T18:32:32Z"), 0, 60.2248293f, 24.969742f),
                                    new GpsPoint(getTimeStamp("2016-07-27T18:47:36Z"), 0, 60.214603931355484f, 24.985077086109463f),
                                    new GpsPoint(getTimeStamp("2016-07-27T18:52:37Z"), 0, 60.21450195784493f, 24.985536634720983f),
                                    new GpsPoint(getTimeStamp("2016-07-27T18:57:36Z"), 0, 60.214817617554935f, 24.985210596788047f),
                                    new GpsPoint(getTimeStamp("2016-07-27T19:02:29Z"), 0, 60.2153793f, 24.9850012f),
                                    new GpsPoint(getTimeStamp("2016-07-27T19:04:12Z"), 0, 60.215383f, 24.9849924f),
                                    new GpsPoint(getTimeStamp("2016-07-27T19:07:33Z"), 0, 60.2153795f, 24.9849958f),
                                    new GpsPoint(getTimeStamp("2016-07-27T19:12:33Z"), 0, 60.2153795f, 24.984996f),
                                    new GpsPoint(getTimeStamp("2016-07-27T19:17:33Z"), 0, 60.21683324094108f, 24.9659439391287f),
                                    new GpsPoint(getTimeStamp("2016-07-27T19:18:54Z"), 0, 60.22378603676173f, 24.96642238210676f),
                                    new GpsPoint(getTimeStamp("2016-07-27T19:19:55Z"), 0, 60.229589077865405f, 24.964510199515694f),
                                    new GpsPoint(getTimeStamp("2016-07-27T19:20:56Z"), 0, 60.234101502712114f, 24.966579727950645f),
                                    new GpsPoint(getTimeStamp("2016-07-27T19:22:32Z"), 0, 60.2345214f, 24.9660367f),
                                    new GpsPoint(getTimeStamp("2016-07-27T19:27:32Z"), 0, 60.2345212f, 24.9660478f),
                                    new GpsPoint(getTimeStamp("2016-07-27T19:32:21Z"), 0, 60.2345029f, 24.9660214f),
                                    new GpsPoint(getTimeStamp("2016-07-27T19:33:37Z"), 0, 60.2344815f, 24.966014f),
                                    new GpsPoint(getTimeStamp("2016-07-27T19:37:32Z"), 0, 60.2345131f, 24.9660362f),
                                    new GpsPoint(getTimeStamp("2016-07-27T19:39:14Z"), 0, 60.2345115f, 24.9660237f),
                                    new GpsPoint(getTimeStamp("2016-07-27T19:40:16Z"), 0, 60.2345149f, 24.9660345f),
                                    new GpsPoint(getTimeStamp("2016-07-27T19:41:32Z"), 0, 60.2345237f, 24.9660709f),
                                    new GpsPoint(getTimeStamp("2016-07-27T19:43:12Z"), 0, 60.2345217f, 24.9660477f),
                                    new GpsPoint(getTimeStamp("2016-07-27T19:44:13Z"), 0, 60.2345342f, 24.9660759f),
                                    new GpsPoint(getTimeStamp("2016-07-27T19:45:14Z"), 0, 60.2344924f, 24.9660516f),
                                    new GpsPoint(getTimeStamp("2016-07-27T19:46:15Z"), 0, 60.2345176f, 24.966022f),
                                    new GpsPoint(getTimeStamp("2016-07-27T19:47:32Z"), 0, 60.2345202f, 24.9660366f),
                                    new GpsPoint(getTimeStamp("2016-07-27T19:52:33Z"), 0, 60.234524f, 24.9660344f),
                                    new GpsPoint(getTimeStamp("2016-07-27T19:57:28Z"), 0, 60.2345183f, 24.9660402f),
                                    new GpsPoint(getTimeStamp("2016-07-27T19:58:37Z"), 0, 60.2345302f, 24.9660784f),
                                    new GpsPoint(getTimeStamp("2016-07-27T20:00:05Z"), 0, 60.23464675035858f, 24.966789825751448f),
                                    new GpsPoint(getTimeStamp("2016-07-27T20:02:32Z"), 0, 60.2345252f, 24.9660697f),
                                    new GpsPoint(getTimeStamp("2016-07-27T20:07:36Z"), 0, 60.23291107574982f, 24.960019033380338f),
                                    new GpsPoint(getTimeStamp("2016-07-27T20:12:35Z"), 0, 60.23183304169261f, 24.959305983564086f),
                                    new GpsPoint(getTimeStamp("2016-07-27T20:14:06Z"), 0, 60.2290347f, 24.9639671f),
                                    new GpsPoint(getTimeStamp("2016-07-27T20:15:08Z"), 0, 60.2290022f, 24.9639329f),
                                    new GpsPoint(getTimeStamp("2016-07-27T20:16:09Z"), 0, 60.2305292f, 24.9727823f),
                                    new GpsPoint(getTimeStamp("2016-07-27T20:17:16Z"), 0, 60.231032804987635f, 24.98376343154847f),
                                    new GpsPoint(getTimeStamp("2016-07-27T20:20:29Z"), 0, 60.22718344664179f, 25.012319365186933f),
                                    new GpsPoint(getTimeStamp("2016-07-27T20:21:34Z"), 0, 60.22469853930903f, 25.017070173252012f),
                                    new GpsPoint(getTimeStamp("2016-07-27T20:22:59Z"), 0, 60.223235676323334f, 25.02460484130985f),
                                    new GpsPoint(getTimeStamp("2016-07-27T20:27:32Z"), 0, 60.210226f, 25.0588864f),
                                    new GpsPoint(getTimeStamp("2016-07-27T20:32:34Z"), 0, 60.20926066715f, 25.076421106803853f),
                                    new GpsPoint(getTimeStamp("2016-07-27T20:33:52Z"), 0, 60.2093112288063f, 25.076827202233247f),
                                    new GpsPoint(getTimeStamp("2016-07-27T20:37:36Z"), 0, 60.20943117579061f, 25.092286306856156f),
                                    new GpsPoint(getTimeStamp("2016-07-27T20:42:35Z"), 0, 60.20710791433031f, 25.122266637499227f),
                                    new GpsPoint(getTimeStamp("2016-07-27T20:43:35Z"), 0, 60.2072202f, 25.1239699f),
                                    new GpsPoint(getTimeStamp("2016-07-27T20:47:31Z"), 0, 60.2094598f, 25.1271595f),
                                    new GpsPoint(getTimeStamp("2016-07-27T20:52:30Z"), 0, 60.2094661f, 25.1271595f),
                                    new GpsPoint(getTimeStamp("2016-07-27T20:54:00Z"), 0, 60.2094667f, 25.1271621f),
                                    new GpsPoint(getTimeStamp("2016-07-27T20:55:02Z"), 0, 60.2094635f, 25.1271615f),
                                    new GpsPoint(getTimeStamp("2016-07-27T20:56:03Z"), 0, 60.2095214f, 25.1271014f),
                                    new GpsPoint(getTimeStamp("2016-07-27T20:57:31Z"), 0, 60.2095214f, 25.127101f)
                            ), -1)
                    )
            );
            add(
                    new TestObject(

                            //messeniuksenkatu 5, caloniuksenkatu 9, tenholantie 16
                            new ArrayList<Coordinate>()
                            {{
                                add(new Coordinate(60.188141f, 24.915722f));
                                add(new Coordinate(60.174497f, 24.920987f));
                                add(new Coordinate(60.202457f, 24.911500f));
                            }},

                            createListOfPlaces(
                                    new GpsPoint(getTimeStamp("2016-07-21T07:06:15Z"), 0, 60.1883165f, 24.9156884f),
                                    new GpsPoint(getTimeStamp("2016-07-21T07:07:22Z"), 0, 60.1883578f, 24.9157133f),
                                    new GpsPoint(getTimeStamp("2016-07-21T07:08:22Z"), 0, 60.1883738f, 24.9158252f),
                                    new GpsPoint(getTimeStamp("2016-07-21T07:09:44Z"), 0, 60.1883645f, 24.9157579f),
                                    new GpsPoint(getTimeStamp("2016-07-21T07:10:56Z"), 0, 60.1883387f, 24.9156835f),
                                    new GpsPoint(getTimeStamp("2016-07-21T07:12:16Z"), 0, 60.1883617f, 24.9157677f),
                                    new GpsPoint(getTimeStamp("2016-07-21T07:13:17Z"), 0, 60.1883728f, 24.9157199f),
                                    new GpsPoint(getTimeStamp("2016-07-21T07:14:18Z"), 0, 60.1883222f, 24.9156687f),
                                    new GpsPoint(getTimeStamp("2016-07-21T07:15:19Z"), 0, 60.1883773f, 24.9157793f),
                                    new GpsPoint(getTimeStamp("2016-07-21T07:16:20Z"), 0, 60.1883707f, 24.9157497f),
                                    new GpsPoint(getTimeStamp("2016-07-21T07:17:28Z"), 0, 60.1883833f, 24.9158309f),
                                    new GpsPoint(getTimeStamp("2016-07-21T07:18:57Z"), 0, 60.1883364f, 24.9156424f),
                                    new GpsPoint(getTimeStamp("2016-07-21T07:19:58Z"), 0, 60.188391f, 24.9158453f),
                                    new GpsPoint(getTimeStamp("2016-07-21T07:20:59Z"), 0, 60.1883662f, 24.9157207f),
                                    new GpsPoint(getTimeStamp("2016-07-21T07:22:00Z"), 0, 60.1883962f, 24.9157518f),
                                    new GpsPoint(getTimeStamp("2016-07-21T07:23:01Z"), 0, 60.1883578f, 24.9157111f),
                                    new GpsPoint(getTimeStamp("2016-07-21T07:24:02Z"), 0, 60.1883577f, 24.9157115f),
                                    new GpsPoint(getTimeStamp("2016-07-21T07:25:23Z"), 0, 60.1883323f, 24.9156829f),
                                    new GpsPoint(getTimeStamp("2016-07-21T07:26:45Z"), 0, 60.1883462f, 24.9156586f),
                                    new GpsPoint(getTimeStamp("2016-07-21T07:27:45Z"), 0, 60.188334f, 24.915637f),
                                    new GpsPoint(getTimeStamp("2016-07-21T07:28:46Z"), 0, 60.1883651f, 24.9157167f),
                                    new GpsPoint(getTimeStamp("2016-07-21T07:29:54Z"), 0, 60.1883762f, 24.9157382f),
                                    new GpsPoint(getTimeStamp("2016-07-21T07:30:55Z"), 0, 60.1883664f, 24.9158201f),
                                    new GpsPoint(getTimeStamp("2016-07-21T07:31:56Z"), 0, 60.1883794f, 24.9157363f),
                                    new GpsPoint(getTimeStamp("2016-07-21T07:32:57Z"), 0, 60.1883308f, 24.9156316f),
                                    new GpsPoint(getTimeStamp("2016-07-21T07:33:58Z"), 0, 60.1883851f, 24.9157453f),
                                    new GpsPoint(getTimeStamp("2016-07-21T07:34:59Z"), 0, 60.1883814f, 24.9157404f),
                                    new GpsPoint(getTimeStamp("2016-07-21T07:36:00Z"), 0, 60.1883809f, 24.9157434f),
                                    new GpsPoint(getTimeStamp("2016-07-21T07:37:00Z"), 0, 60.1883507f, 24.9157149f),
                                    new GpsPoint(getTimeStamp("2016-07-21T07:38:24Z"), 0, 60.1883395f, 24.9156485f),
                                    new GpsPoint(getTimeStamp("2016-07-21T07:39:51Z"), 0, 60.1884051f, 24.9157826f),
                                    new GpsPoint(getTimeStamp("2016-07-21T07:40:52Z"), 0, 60.1883445f, 24.9157224f),
                                    new GpsPoint(getTimeStamp("2016-07-21T07:41:53Z"), 0, 60.1883309f, 24.9157014f),
                                    new GpsPoint(getTimeStamp("2016-07-21T07:42:54Z"), 0, 60.1883879f, 24.9158061f),
                                    new GpsPoint(getTimeStamp("2016-07-21T07:44:14Z"), 0, 60.1883324f, 24.9157119f),
                                    new GpsPoint(getTimeStamp("2016-07-21T07:45:16Z"), 0, 60.1883277f, 24.9157379f),
                                    new GpsPoint(getTimeStamp("2016-07-21T07:46:17Z"), 0, 60.1883666f, 24.9157037f),
                                    new GpsPoint(getTimeStamp("2016-07-21T07:47:17Z"), 0, 60.1883683f, 24.9157096f),
                                    new GpsPoint(getTimeStamp("2016-07-21T07:48:18Z"), 0, 60.1883456f, 24.9156693f),
                                    new GpsPoint(getTimeStamp("2016-07-21T07:49:19Z"), 0, 60.1883382f, 24.9158468f),
                                    new GpsPoint(getTimeStamp("2016-07-21T07:50:20Z"), 0, 60.1883472f, 24.9156645f),
                                    new GpsPoint(getTimeStamp("2016-07-21T07:51:41Z"), 0, 60.1883577f, 24.9157115f),
                                    new GpsPoint(getTimeStamp("2016-07-21T07:52:41Z"), 0, 60.1883616f, 24.9157113f),
                                    new GpsPoint(getTimeStamp("2016-07-21T07:54:03Z"), 0, 60.1883629f, 24.9156955f),
                                    new GpsPoint(getTimeStamp("2016-07-21T07:55:04Z"), 0, 60.188362f, 24.9156967f),
                                    new GpsPoint(getTimeStamp("2016-07-21T07:56:05Z"), 0, 60.188334f, 24.9156846f),
                                    new GpsPoint(getTimeStamp("2016-07-21T07:57:24Z"), 0, 60.1883652f, 24.9156975f),
                                    new GpsPoint(getTimeStamp("2016-07-21T07:58:25Z"), 0, 60.1883577f, 24.9157106f),
                                    new GpsPoint(getTimeStamp("2016-07-21T07:59:26Z"), 0, 60.1883877f, 24.9158183f),
                                    new GpsPoint(getTimeStamp("2016-07-21T08:00:27Z"), 0, 60.1883577f, 24.9157112f),
                                    new GpsPoint(getTimeStamp("2016-07-21T08:01:27Z"), 0, 60.1883457f, 24.9156696f),
                                    new GpsPoint(getTimeStamp("2016-07-21T08:02:39Z"), 0, 60.1883892f, 24.9157876f),
                                    new GpsPoint(getTimeStamp("2016-07-21T08:03:40Z"), 0, 60.1883903f, 24.9158761f),
                                    new GpsPoint(getTimeStamp("2016-07-21T08:04:41Z"), 0, 60.1883428f, 24.9156508f),
                                    new GpsPoint(getTimeStamp("2016-07-21T08:05:43Z"), 0, 60.1883577f, 24.9157112f),
                                    new GpsPoint(getTimeStamp("2016-07-21T08:06:44Z"), 0, 60.1883577f, 24.9157112f),
                                    new GpsPoint(getTimeStamp("2016-07-21T08:07:45Z"), 0, 60.1883577f, 24.9157114f),
                                    new GpsPoint(getTimeStamp("2016-07-21T08:09:04Z"), 0, 60.1883406f, 24.9157422f),
                                    new GpsPoint(getTimeStamp("2016-07-21T08:10:05Z"), 0, 60.1883747f, 24.9157402f),
                                    new GpsPoint(getTimeStamp("2016-07-21T08:11:27Z"), 0, 60.1883577f, 24.9157112f),
                                    new GpsPoint(getTimeStamp("2016-07-21T08:13:08Z"), 0, 60.1883577f, 24.9157106f),
                                    new GpsPoint(getTimeStamp("2016-07-21T08:14:09Z"), 0, 60.188364f, 24.9157142f),
                                    new GpsPoint(getTimeStamp("2016-07-21T08:15:10Z"), 0, 60.1883381f, 24.9158118f),
                                    new GpsPoint(getTimeStamp("2016-07-21T08:16:10Z"), 0, 60.1883316f, 24.9156323f),
                                    new GpsPoint(getTimeStamp("2016-07-21T08:17:11Z"), 0, 60.1883708f, 24.9157274f),
                                    new GpsPoint(getTimeStamp("2016-07-21T08:18:12Z"), 0, 60.1883378f, 24.9157658f),
                                    new GpsPoint(getTimeStamp("2016-07-21T08:19:13Z"), 0, 60.1883458f, 24.9156693f),
                                    new GpsPoint(getTimeStamp("2016-07-21T08:20:13Z"), 0, 60.1883382f, 24.9157116f),
                                    new GpsPoint(getTimeStamp("2016-07-21T08:21:14Z"), 0, 60.1883415f, 24.9156482f),
                                    new GpsPoint(getTimeStamp("2016-07-21T08:22:15Z"), 0, 60.1883186f, 24.9156508f),
                                    new GpsPoint(getTimeStamp("2016-07-21T08:23:16Z"), 0, 60.1883323f, 24.915765f),
                                    new GpsPoint(getTimeStamp("2016-07-21T08:24:25Z"), 0, 60.1883554f, 24.9158239f),
                                    new GpsPoint(getTimeStamp("2016-07-21T08:25:26Z"), 0, 60.1883378f, 24.9157343f),
                                    new GpsPoint(getTimeStamp("2016-07-21T08:26:50Z"), 0, 60.1883225f, 24.9156504f),
                                    new GpsPoint(getTimeStamp("2016-07-21T08:27:51Z"), 0, 60.1883291f, 24.9157618f),
                                    new GpsPoint(getTimeStamp("2016-07-21T08:29:47Z"), 0, 60.1881521f, 24.9155222f),
                                    new GpsPoint(getTimeStamp("2016-07-21T08:33:27Z"), 0, 60.18682354f, 24.91652906f),
                                    new GpsPoint(getTimeStamp("2016-07-21T08:34:32Z"), 0, 60.18658574f, 24.91809806f),
                                    new GpsPoint(getTimeStamp("2016-07-21T08:38:16Z"), 0, 60.1836058f, 24.9203937f),
                                    new GpsPoint(getTimeStamp("2016-07-21T08:43:09Z"), 0, 60.1794494f, 24.9235093f),
                                    new GpsPoint(getTimeStamp("2016-07-21T08:48:09Z"), 0, 60.1763597f, 24.9222057f),
                                    new GpsPoint(getTimeStamp("2016-07-21T08:53:16Z"), 0, 60.1748461f, 24.9212693f),
                                    new GpsPoint(getTimeStamp("2016-07-21T08:54:29Z"), 0, 60.1751678f, 24.9218653f),
                                    new GpsPoint(getTimeStamp("2016-07-21T08:58:36Z"), 0, 60.174702f, 24.9209886f),
                                    new GpsPoint(getTimeStamp("2016-07-21T09:01:52Z"), 0, 60.1747232f, 24.9210356f),
                                    new GpsPoint(getTimeStamp("2016-07-21T09:02:53Z"), 0, 60.1747036f, 24.9210255f),
                                    new GpsPoint(getTimeStamp("2016-07-21T09:03:58Z"), 0, 60.1746903f, 24.9209615f),
                                    new GpsPoint(getTimeStamp("2016-07-21T09:04:59Z"), 0, 60.1747075f, 24.9210188f),
                                    new GpsPoint(getTimeStamp("2016-07-21T09:06:19Z"), 0, 60.1746903f, 24.920982f),
                                    new GpsPoint(getTimeStamp("2016-07-21T09:07:40Z"), 0, 60.1746983f, 24.9210004f),
                                    new GpsPoint(getTimeStamp("2016-07-21T09:08:49Z"), 0, 60.1746911f, 24.9210297f),
                                    new GpsPoint(getTimeStamp("2016-07-21T09:10:29Z"), 0, 60.1746904f, 24.9210577f),
                                    new GpsPoint(getTimeStamp("2016-07-21T09:11:30Z"), 0, 60.1747019f, 24.9210529f),
                                    new GpsPoint(getTimeStamp("2016-07-21T09:12:31Z"), 0, 60.1746993f, 24.9210412f),
                                    new GpsPoint(getTimeStamp("2016-07-21T09:13:53Z"), 0, 60.1747243f, 24.9210709f),
                                    new GpsPoint(getTimeStamp("2016-07-21T09:15:39Z"), 0, 60.1747279f, 24.9210744f),
                                    new GpsPoint(getTimeStamp("2016-07-21T09:16:57Z"), 0, 60.1747079f, 24.9210537f),
                                    new GpsPoint(getTimeStamp("2016-07-21T09:18:38Z"), 0, 60.1747091f, 24.9210597f),
                                    new GpsPoint(getTimeStamp("2016-07-21T09:24:10Z"), 0, 60.1747283f, 24.9211436f),
                                    new GpsPoint(getTimeStamp("2016-07-21T09:26:12Z"), 0, 60.1747108f, 24.9210812f),
                                    new GpsPoint(getTimeStamp("2016-07-21T09:27:19Z"), 0, 60.1747117f, 24.9210748f),
                                    new GpsPoint(getTimeStamp("2016-07-21T09:29:01Z"), 0, 60.174667f, 24.9209437f),
                                    new GpsPoint(getTimeStamp("2016-07-21T09:31:17Z"), 0, 60.1747306f, 24.9211416f),
                                    new GpsPoint(getTimeStamp("2016-07-21T09:32:23Z"), 0, 60.1748039f, 24.9212334f),
                                    new GpsPoint(getTimeStamp("2016-07-21T09:33:44Z"), 0, 60.17473f, 24.9211489f),
                                    new GpsPoint(getTimeStamp("2016-07-21T09:35:28Z"), 0, 60.1747323f, 24.9211016f),
                                    new GpsPoint(getTimeStamp("2016-07-21T09:36:29Z"), 0, 60.1747519f, 24.9211439f),
                                    new GpsPoint(getTimeStamp("2016-07-21T09:37:30Z"), 0, 60.1747192f, 24.9210913f),
                                    new GpsPoint(getTimeStamp("2016-07-21T09:41:13Z"), 0, 60.1747237f, 24.9209649f),
                                    new GpsPoint(getTimeStamp("2016-07-21T09:45:16Z"), 0, 60.1747487f, 24.9210835f),
                                    new GpsPoint(getTimeStamp("2016-07-21T09:46:17Z"), 0, 60.1746969f, 24.9209762f),
                                    new GpsPoint(getTimeStamp("2016-07-21T09:47:18Z"), 0, 60.1747173f, 24.9210448f),
                                    new GpsPoint(getTimeStamp("2016-07-21T09:49:04Z"), 0, 60.1746807f, 24.9209726f),
                                    new GpsPoint(getTimeStamp("2016-07-21T09:50:43Z"), 0, 60.1747202f, 24.9210211f),
                                    new GpsPoint(getTimeStamp("2016-07-21T09:51:55Z"), 0, 60.1747643f, 24.9211198f),
                                    new GpsPoint(getTimeStamp("2016-07-21T09:53:37Z"), 0, 60.1747513f, 24.9211135f),
                                    new GpsPoint(getTimeStamp("2016-07-21T09:54:38Z"), 0, 60.1747224f, 24.920989f),
                                    new GpsPoint(getTimeStamp("2016-07-21T09:55:58Z"), 0, 60.1747239f, 24.9210249f),
                                    new GpsPoint(getTimeStamp("2016-07-21T09:57:20Z"), 0, 60.1747209f, 24.9210275f),
                                    new GpsPoint(getTimeStamp("2016-07-21T09:58:21Z"), 0, 60.1747449f, 24.9210643f),
                                    new GpsPoint(getTimeStamp("2016-07-21T09:59:22Z"), 0, 60.1747045f, 24.9209328f),
                                    new GpsPoint(getTimeStamp("2016-07-21T10:00:23Z"), 0, 60.1746779f, 24.920884f),
                                    new GpsPoint(getTimeStamp("2016-07-21T10:01:24Z"), 0, 60.1747221f, 24.9210113f),
                                    new GpsPoint(getTimeStamp("2016-07-21T10:03:10Z"), 0, 60.1746923f, 24.9209807f),
                                    new GpsPoint(getTimeStamp("2016-07-21T10:05:30Z"), 0, 60.1747032f, 24.920984f),
                                    new GpsPoint(getTimeStamp("2016-07-21T10:06:31Z"), 0, 60.1747354f, 24.9211393f),
                                    new GpsPoint(getTimeStamp("2016-07-21T10:08:06Z"), 0, 60.1747443f, 24.9211218f),
                                    new GpsPoint(getTimeStamp("2016-07-21T10:09:19Z"), 0, 60.1747113f, 24.921055f),
                                    new GpsPoint(getTimeStamp("2016-07-21T10:10:20Z"), 0, 60.1747113f, 24.9210691f),
                                    new GpsPoint(getTimeStamp("2016-07-21T10:11:21Z"), 0, 60.1747381f, 24.9211364f),
                                    new GpsPoint(getTimeStamp("2016-07-21T10:12:22Z"), 0, 60.1746996f, 24.9210998f),
                                    new GpsPoint(getTimeStamp("2016-07-21T10:13:23Z"), 0, 60.1747677f, 24.9211384f),
                                    new GpsPoint(getTimeStamp("2016-07-21T10:14:23Z"), 0, 60.1747101f, 24.9210658f),
                                    new GpsPoint(getTimeStamp("2016-07-21T10:15:25Z"), 0, 60.1747345f, 24.9211003f),
                                    new GpsPoint(getTimeStamp("2016-07-21T10:16:34Z"), 0, 60.1747473f, 24.9211141f),
                                    new GpsPoint(getTimeStamp("2016-07-21T10:17:35Z"), 0, 60.1747493f, 24.9211841f),
                                    new GpsPoint(getTimeStamp("2016-07-21T10:18:36Z"), 0, 60.1747379f, 24.9211042f),
                                    new GpsPoint(getTimeStamp("2016-07-21T10:19:37Z"), 0, 60.1747677f, 24.9211316f),
                                    new GpsPoint(getTimeStamp("2016-07-21T10:20:38Z"), 0, 60.1747267f, 24.9211104f),
                                    new GpsPoint(getTimeStamp("2016-07-21T10:21:39Z"), 0, 60.1747465f, 24.9211198f),
                                    new GpsPoint(getTimeStamp("2016-07-21T10:22:40Z"), 0, 60.17473f, 24.9210832f),
                                    new GpsPoint(getTimeStamp("2016-07-21T10:23:41Z"), 0, 60.1747215f, 24.9211144f),
                                    new GpsPoint(getTimeStamp("2016-07-21T10:24:41Z"), 0, 60.1747296f, 24.9210818f),
                                    new GpsPoint(getTimeStamp("2016-07-21T10:26:04Z"), 0, 60.1747199f, 24.9211061f),
                                    new GpsPoint(getTimeStamp("2016-07-21T10:27:45Z"), 0, 60.1747069f, 24.921038f),
                                    new GpsPoint(getTimeStamp("2016-07-21T10:28:46Z"), 0, 60.1746935f, 24.9210646f),
                                    new GpsPoint(getTimeStamp("2016-07-21T10:29:54Z"), 0, 60.1746898f, 24.9209987f),
                                    new GpsPoint(getTimeStamp("2016-07-21T10:31:16Z"), 0, 60.1746863f, 24.920945f),
                                    new GpsPoint(getTimeStamp("2016-07-21T10:32:50Z"), 0, 60.1747142f, 24.9210739f),
                                    new GpsPoint(getTimeStamp("2016-07-21T10:34:20Z"), 0, 60.1747146f, 24.9210203f),
                                    new GpsPoint(getTimeStamp("2016-07-21T10:35:41Z"), 0, 60.1746943f, 24.9210159f),
                                    new GpsPoint(getTimeStamp("2016-07-21T10:36:42Z"), 0, 60.1746859f, 24.9210388f),
                                    new GpsPoint(getTimeStamp("2016-07-21T10:37:43Z"), 0, 60.174701f, 24.9209947f),
                                    new GpsPoint(getTimeStamp("2016-07-21T10:39:06Z"), 0, 60.1747534f, 24.9211029f),
                                    new GpsPoint(getTimeStamp("2016-07-21T10:40:26Z"), 0, 60.1747002f, 24.9210759f),
                                    new GpsPoint(getTimeStamp("2016-07-21T10:41:27Z"), 0, 60.1747016f, 24.9209883f),
                                    new GpsPoint(getTimeStamp("2016-07-21T10:42:30Z"), 0, 60.1746874f, 24.9210423f),
                                    new GpsPoint(getTimeStamp("2016-07-21T10:45:33Z"), 0, 60.174711f, 24.9210563f),
                                    new GpsPoint(getTimeStamp("2016-07-21T10:46:34Z"), 0, 60.1747129f, 24.9211015f),
                                    new GpsPoint(getTimeStamp("2016-07-21T10:51:39Z"), 0, 60.1746467f, 24.9211013f),
                                    new GpsPoint(getTimeStamp("2016-07-21T10:52:40Z"), 0, 60.1747068f, 24.9210781f),
                                    new GpsPoint(getTimeStamp("2016-07-21T10:53:41Z"), 0, 60.1747111f, 24.9211062f),
                                    new GpsPoint(getTimeStamp("2016-07-21T10:55:23Z"), 0, 60.1746884f, 24.9210705f),
                                    new GpsPoint(getTimeStamp("2016-07-21T10:56:24Z"), 0, 60.1747025f, 24.9210187f),
                                    new GpsPoint(getTimeStamp("2016-07-21T10:57:25Z"), 0, 60.1747075f, 24.9210744f),
                                    new GpsPoint(getTimeStamp("2016-07-21T10:58:26Z"), 0, 60.1747191f, 24.9210884f),
                                    new GpsPoint(getTimeStamp("2016-07-21T10:59:27Z"), 0, 60.1747087f, 24.9210699f),
                                    new GpsPoint(getTimeStamp("2016-07-21T11:00:49Z"), 0, 60.1747262f, 24.9211418f),
                                    new GpsPoint(getTimeStamp("2016-07-21T11:02:10Z"), 0, 60.1747101f, 24.9210618f),
                                    new GpsPoint(getTimeStamp("2016-07-21T11:03:54Z"), 0, 60.1747128f, 24.9210934f),
                                    new GpsPoint(getTimeStamp("2016-07-21T11:05:21Z"), 0, 60.1747243f, 24.9210416f),
                                    new GpsPoint(getTimeStamp("2016-07-21T11:06:22Z"), 0, 60.174711f, 24.9210741f),
                                    new GpsPoint(getTimeStamp("2016-07-21T11:07:23Z"), 0, 60.1747227f, 24.9210591f),
                                    new GpsPoint(getTimeStamp("2016-07-21T11:08:26Z"), 0, 60.1746822f, 24.9208696f),
                                    new GpsPoint(getTimeStamp("2016-07-21T11:09:52Z"), 0, 60.17493089f, 24.92059903f),
                                    new GpsPoint(getTimeStamp("2016-07-21T11:10:51Z"), 0, 60.1747103f, 24.921061f),
                                    new GpsPoint(getTimeStamp("2016-07-21T11:12:13Z"), 0, 60.1746789f, 24.9209286f),
                                    new GpsPoint(getTimeStamp("2016-07-21T11:13:21Z"), 0, 60.1746866f, 24.9209643f),
                                    new GpsPoint(getTimeStamp("2016-07-21T11:14:22Z"), 0, 60.1747053f, 24.9210082f),
                                    new GpsPoint(getTimeStamp("2016-07-21T11:15:44Z"), 0, 60.1747294f, 24.9211295f),
                                    new GpsPoint(getTimeStamp("2016-07-21T11:17:16Z"), 0, 60.17479942f, 24.9207287f),
                                    new GpsPoint(getTimeStamp("2016-07-21T11:18:15Z"), 0, 60.1746884f, 24.921008f),
                                    new GpsPoint(getTimeStamp("2016-07-21T11:19:16Z"), 0, 60.1747472f, 24.921074f),
                                    new GpsPoint(getTimeStamp("2016-07-21T11:20:17Z"), 0, 60.1747227f, 24.9210886f),
                                    new GpsPoint(getTimeStamp("2016-07-21T11:21:18Z"), 0, 60.1747005f, 24.9210323f),
                                    new GpsPoint(getTimeStamp("2016-07-21T11:22:22Z"), 0, 60.1747276f, 24.9211009f),
                                    new GpsPoint(getTimeStamp("2016-07-21T11:23:23Z"), 0, 60.1747294f, 24.9211199f),
                                    new GpsPoint(getTimeStamp("2016-07-21T11:24:24Z"), 0, 60.1746847f, 24.9209924f),
                                    new GpsPoint(getTimeStamp("2016-07-21T11:25:25Z"), 0, 60.1747076f, 24.9210717f),
                                    new GpsPoint(getTimeStamp("2016-07-21T11:26:46Z"), 0, 60.1747015f, 24.9210772f),
                                    new GpsPoint(getTimeStamp("2016-07-21T11:30:37Z"), 0, 60.1747422f, 24.9211389f),
                                    new GpsPoint(getTimeStamp("2016-07-21T11:31:41Z"), 0, 60.1747045f, 24.9210505f),
                                    new GpsPoint(getTimeStamp("2016-07-21T11:33:02Z"), 0, 60.1747169f, 24.9209622f),
                                    new GpsPoint(getTimeStamp("2016-07-21T11:34:04Z"), 0, 60.1747182f, 24.9210351f),
                                    new GpsPoint(getTimeStamp("2016-07-21T11:36:25Z"), 0, 60.1747136f, 24.9209956f),
                                    new GpsPoint(getTimeStamp("2016-07-21T11:37:31Z"), 0, 60.1746872f, 24.9210066f),
                                    new GpsPoint(getTimeStamp("2016-07-21T11:38:32Z"), 0, 60.174695f, 24.9209917f),
                                    new GpsPoint(getTimeStamp("2016-07-21T11:39:34Z"), 0, 60.1747043f, 24.9210201f),
                                    new GpsPoint(getTimeStamp("2016-07-21T11:41:46Z"), 0, 60.17451409f, 24.92084659f),
                                    new GpsPoint(getTimeStamp("2016-07-21T11:43:26Z"), 0, 60.1746919f, 24.9210652f),
                                    new GpsPoint(getTimeStamp("2016-07-21T11:44:47Z"), 0, 60.1747344f, 24.9210643f),
                                    new GpsPoint(getTimeStamp("2016-07-21T11:46:28Z"), 0, 60.1748362f, 24.9212182f),
                                    new GpsPoint(getTimeStamp("2016-07-21T11:47:29Z"), 0, 60.1746955f, 24.9209615f),
                                    new GpsPoint(getTimeStamp("2016-07-21T11:49:31Z"), 0, 60.1746907f, 24.9209743f),
                                    new GpsPoint(getTimeStamp("2016-07-21T11:51:31Z"), 0, 60.1747241f, 24.9210983f),
                                    new GpsPoint(getTimeStamp("2016-07-21T11:52:35Z"), 0, 60.1747205f, 24.9211286f),
                                    new GpsPoint(getTimeStamp("2016-07-21T11:53:36Z"), 0, 60.174715f, 24.9210395f),
                                    new GpsPoint(getTimeStamp("2016-07-21T11:54:37Z"), 0, 60.174689f, 24.9209883f),
                                    new GpsPoint(getTimeStamp("2016-07-21T11:55:38Z"), 0, 60.1747085f, 24.921028f),
                                    new GpsPoint(getTimeStamp("2016-07-21T11:56:39Z"), 0, 60.1747095f, 24.9210354f),
                                    new GpsPoint(getTimeStamp("2016-07-21T11:57:40Z"), 0, 60.1746901f, 24.9209896f),
                                    new GpsPoint(getTimeStamp("2016-07-21T11:58:41Z"), 0, 60.1746905f, 24.9210052f),
                                    new GpsPoint(getTimeStamp("2016-07-21T11:59:42Z"), 0, 60.1747067f, 24.9210107f),
                                    new GpsPoint(getTimeStamp("2016-07-21T12:01:03Z"), 0, 60.1747265f, 24.9210765f),
                                    new GpsPoint(getTimeStamp("2016-07-21T12:02:04Z"), 0, 60.1747112f, 24.9210602f),
                                    new GpsPoint(getTimeStamp("2016-07-21T12:06:24Z"), 0, 60.1747289f, 24.9211138f),
                                    new GpsPoint(getTimeStamp("2016-07-21T12:07:46Z"), 0, 60.1746959f, 24.9210131f),
                                    new GpsPoint(getTimeStamp("2016-07-21T12:09:07Z"), 0, 60.1746891f, 24.9210501f),
                                    new GpsPoint(getTimeStamp("2016-07-21T12:10:24Z"), 0, 60.1747605f, 24.9210974f),
                                    new GpsPoint(getTimeStamp("2016-07-21T12:11:25Z"), 0, 60.1746822f, 24.9209237f),
                                    new GpsPoint(getTimeStamp("2016-07-21T12:12:48Z"), 0, 60.17460718f, 24.92058947f),
                                    new GpsPoint(getTimeStamp("2016-07-21T12:13:49Z"), 0, 60.1747105f, 24.9210638f),
                                    new GpsPoint(getTimeStamp("2016-07-21T12:14:50Z"), 0, 60.1747181f, 24.9210488f),
                                    new GpsPoint(getTimeStamp("2016-07-21T12:15:51Z"), 0, 60.1747004f, 24.920964f),
                                    new GpsPoint(getTimeStamp("2016-07-21T12:16:52Z"), 0, 60.1746979f, 24.9210251f),
                                    new GpsPoint(getTimeStamp("2016-07-21T12:17:53Z"), 0, 60.1747088f, 24.9209991f),
                                    new GpsPoint(getTimeStamp("2016-07-21T12:19:08Z"), 0, 60.1747297f, 24.9210871f),
                                    new GpsPoint(getTimeStamp("2016-07-21T12:20:48Z"), 0, 60.1747171f, 24.9209842f),
                                    new GpsPoint(getTimeStamp("2016-07-21T12:22:11Z"), 0, 60.1746957f, 24.9210161f),
                                    new GpsPoint(getTimeStamp("2016-07-21T12:23:12Z"), 0, 60.1746843f, 24.9210341f),
                                    new GpsPoint(getTimeStamp("2016-07-21T12:24:34Z"), 0, 60.1747168f, 24.9210174f),
                                    new GpsPoint(getTimeStamp("2016-07-21T12:25:50Z"), 0, 60.1747064f, 24.9210473f),
                                    new GpsPoint(getTimeStamp("2016-07-21T12:26:51Z"), 0, 60.1747025f, 24.9210506f),
                                    new GpsPoint(getTimeStamp("2016-07-21T12:27:52Z"), 0, 60.1746965f, 24.9209874f),
                                    new GpsPoint(getTimeStamp("2016-07-21T12:28:53Z"), 0, 60.1747237f, 24.9211145f),
                                    new GpsPoint(getTimeStamp("2016-07-21T12:30:08Z"), 0, 60.1747251f, 24.9211137f),
                                    new GpsPoint(getTimeStamp("2016-07-21T12:31:42Z"), 0, 60.17450829f, 24.9206934f),
                                    new GpsPoint(getTimeStamp("2016-07-21T12:32:41Z"), 0, 60.1747056f, 24.9210401f),
                                    new GpsPoint(getTimeStamp("2016-07-21T12:34:19Z"), 0, 60.1746858f, 24.9209629f),
                                    new GpsPoint(getTimeStamp("2016-07-21T12:36:49Z"), 0, 60.1747131f, 24.9210207f),
                                    new GpsPoint(getTimeStamp("2016-07-21T12:37:49Z"), 0, 60.1746975f, 24.9210223f),
                                    new GpsPoint(getTimeStamp("2016-07-21T12:38:50Z"), 0, 60.1746985f, 24.9210217f),
                                    new GpsPoint(getTimeStamp("2016-07-21T12:39:51Z"), 0, 60.174705f, 24.9210315f),
                                    new GpsPoint(getTimeStamp("2016-07-21T12:40:52Z"), 0, 60.1747011f, 24.9210216f),
                                    new GpsPoint(getTimeStamp("2016-07-21T12:42:33Z"), 0, 60.174718f, 24.9210697f),
                                    new GpsPoint(getTimeStamp("2016-07-21T12:46:03Z"), 0, 60.1746773f, 24.920978f),
                                    new GpsPoint(getTimeStamp("2016-07-21T12:47:26Z"), 0, 60.1746522f, 24.9211421f),
                                    new GpsPoint(getTimeStamp("2016-07-21T12:48:28Z"), 0, 60.1746708f, 24.9210051f),
                                    new GpsPoint(getTimeStamp("2016-07-21T12:53:32Z"), 0, 60.1747233f, 24.9211189f),
                                    new GpsPoint(getTimeStamp("2016-07-21T12:54:33Z"), 0, 60.174704f, 24.9210418f),
                                    new GpsPoint(getTimeStamp("2016-07-21T12:55:34Z"), 0, 60.1746867f, 24.9209963f),
                                    new GpsPoint(getTimeStamp("2016-07-21T12:57:17Z"), 0, 60.1746976f, 24.9210271f),
                                    new GpsPoint(getTimeStamp("2016-07-21T12:58:18Z"), 0, 60.1747162f, 24.9210441f),
                                    new GpsPoint(getTimeStamp("2016-07-21T12:59:19Z"), 0, 60.1746949f, 24.9210169f),
                                    new GpsPoint(getTimeStamp("2016-07-21T13:00:20Z"), 0, 60.1746956f, 24.9210355f),
                                    new GpsPoint(getTimeStamp("2016-07-21T13:03:01Z"), 0, 60.1747058f, 24.9210424f),
                                    new GpsPoint(getTimeStamp("2016-07-21T13:04:05Z"), 0, 60.1746874f, 24.9209972f),
                                    new GpsPoint(getTimeStamp("2016-07-21T13:05:27Z"), 0, 60.174701f, 24.9210894f),
                                    new GpsPoint(getTimeStamp("2016-07-21T13:07:05Z"), 0, 60.17465059f, 24.92065199f),
                                    new GpsPoint(getTimeStamp("2016-07-21T13:08:04Z"), 0, 60.1747035f, 24.9210827f),
                                    new GpsPoint(getTimeStamp("2016-07-21T13:09:26Z"), 0, 60.1747085f, 24.921074f),
                                    new GpsPoint(getTimeStamp("2016-07-21T13:11:49Z"), 0, 60.1747462f, 24.9211266f),
                                    new GpsPoint(getTimeStamp("2016-07-21T13:13:14Z"), 0, 60.1751008f, 24.9217644f),
                                    new GpsPoint(getTimeStamp("2016-07-21T13:18:18Z"), 0, 60.1788848f, 24.9228781f),
                                    new GpsPoint(getTimeStamp("2016-07-21T13:23:25Z"), 0, 60.18201804f, 24.92196433f),
                                    new GpsPoint(getTimeStamp("2016-07-21T13:28:16Z"), 0, 60.1860299f, 24.9185599f),
                                    new GpsPoint(getTimeStamp("2016-07-21T13:33:18Z"), 0, 60.188338f, 24.9156469f),
                                    new GpsPoint(getTimeStamp("2016-07-21T13:38:16Z"), 0, 60.1883393f, 24.9156444f),
                                    new GpsPoint(getTimeStamp("2016-07-21T13:39:37Z"), 0, 60.1882819f, 24.9154962f),
                                    new GpsPoint(getTimeStamp("2016-07-21T13:43:16Z"), 0, 60.1883574f, 24.9157111f),
                                    new GpsPoint(getTimeStamp("2016-07-21T13:48:17Z"), 0, 60.1883384f, 24.915646f),
                                    new GpsPoint(getTimeStamp("2016-07-21T13:53:37Z"), 0, 60.1883427f, 24.9156501f),
                                    new GpsPoint(getTimeStamp("2016-07-21T13:54:38Z"), 0, 60.1883577f, 24.9157115f),
                                    new GpsPoint(getTimeStamp("2016-07-21T13:58:16Z"), 0, 60.1883463f, 24.9156968f),
                                    new GpsPoint(getTimeStamp("2016-07-21T14:03:16Z"), 0, 60.1883606f, 24.915801f),
                                    new GpsPoint(getTimeStamp("2016-07-21T14:08:16Z"), 0, 60.1883395f, 24.9156485f),
                                    new GpsPoint(getTimeStamp("2016-07-21T14:09:27Z"), 0, 60.1883157f, 24.9156488f),
                                    new GpsPoint(getTimeStamp("2016-07-21T14:13:38Z"), 0, 60.1883708f, 24.9157483f),
                                    new GpsPoint(getTimeStamp("2016-07-21T14:14:47Z"), 0, 60.1883392f, 24.9156437f),
                                    new GpsPoint(getTimeStamp("2016-07-21T14:15:48Z"), 0, 60.1883424f, 24.915652f),
                                    new GpsPoint(getTimeStamp("2016-07-21T14:18:16Z"), 0, 60.1882974f, 24.9156088f),
                                    new GpsPoint(getTimeStamp("2016-07-21T14:23:13Z"), 0, 60.1883578f, 24.9157111f),
                                    new GpsPoint(getTimeStamp("2016-07-21T14:24:43Z"), 0, 60.1883097f, 24.9156031f),
                                    new GpsPoint(getTimeStamp("2016-07-21T14:28:11Z"), 0, 60.1881792f, 24.9156281f),
                                    new GpsPoint(getTimeStamp("2016-07-21T14:33:18Z"), 0, 60.18945847f, 24.91677052f),
                                    new GpsPoint(getTimeStamp("2016-07-21T14:38:11Z"), 0, 60.1916865f, 24.9133874f),
                                    new GpsPoint(getTimeStamp("2016-07-21T14:43:09Z"), 0, 60.19514622f, 24.90831473f),
                                    new GpsPoint(getTimeStamp("2016-07-21T14:44:02Z"), 0, 60.1960206f, 24.9080861f),
                                    new GpsPoint(getTimeStamp("2016-07-21T14:48:13Z"), 0, 60.19951635f, 24.90924166f),
                                    new GpsPoint(getTimeStamp("2016-07-21T14:53:14Z"), 0, 60.20246936f, 24.91135454f),
                                    new GpsPoint(getTimeStamp("2016-07-21T14:58:15Z"), 0, 60.20235269f, 24.91155993f),
                                    new GpsPoint(getTimeStamp("2016-07-21T14:59:20Z"), 0, 60.20239809f, 24.91144966f),
                                    new GpsPoint(getTimeStamp("2016-07-21T15:03:15Z"), 0, 60.20238675f, 24.91173749f),
                                    new GpsPoint(getTimeStamp("2016-07-21T15:04:20Z"), 0, 60.20238672f, 24.91138554f),
                                    new GpsPoint(getTimeStamp("2016-07-21T15:08:15Z"), 0, 60.20238554f, 24.91140265f),
                                    new GpsPoint(getTimeStamp("2016-07-21T15:13:13Z"), 0, 60.20243017f, 24.91141481f),
                                    new GpsPoint(getTimeStamp("2016-07-21T15:18:14Z"), 0, 60.20245366f, 24.91141947f),
                                    new GpsPoint(getTimeStamp("2016-07-21T15:23:13Z"), 0, 60.20247439f, 24.9114054f),
                                    new GpsPoint(getTimeStamp("2016-07-21T15:28:16Z"), 0, 60.20246911f, 24.9114223f),
                                    new GpsPoint(getTimeStamp("2016-07-21T15:33:14Z"), 0, 60.20245976f, 24.91142238f),
                                    new GpsPoint(getTimeStamp("2016-07-21T15:38:19Z"), 0, 60.20245356f, 24.91144942f),
                                    new GpsPoint(getTimeStamp("2016-07-21T15:43:13Z"), 0, 60.20246218f, 24.91146637f),
                                    new GpsPoint(getTimeStamp("2016-07-21T15:48:15Z"), 0, 60.20241783f, 24.9116641f),
                                    new GpsPoint(getTimeStamp("2016-07-21T15:53:16Z"), 0, 60.20218914f, 24.91149776f),
                                    new GpsPoint(getTimeStamp("2016-07-21T15:58:14Z"), 0, 60.20250543f, 24.91175107f),
                                    new GpsPoint(getTimeStamp("2016-07-21T16:03:13Z"), 0, 60.20265312f, 24.91124133f),
                                    new GpsPoint(getTimeStamp("2016-07-21T16:08:14Z"), 0, 60.20248107f, 24.91184537f),
                                    new GpsPoint(getTimeStamp("2016-07-21T16:13:15Z"), 0, 60.20249458f, 24.91175327f),
                                    new GpsPoint(getTimeStamp("2016-07-21T16:18:14Z"), 0, 60.2025241f, 24.91174268f),
                                    new GpsPoint(getTimeStamp("2016-07-21T16:23:14Z"), 0, 60.20251546f, 24.91169854f),
                                    new GpsPoint(getTimeStamp("2016-07-21T16:28:14Z"), 0, 60.20253974f, 24.91167635f),
                                    new GpsPoint(getTimeStamp("2016-07-21T16:33:15Z"), 0, 60.2025399f, 24.91167509f),
                                    new GpsPoint(getTimeStamp("2016-07-21T16:38:13Z"), 0, 60.20252986f, 24.91166494f),
                                    new GpsPoint(getTimeStamp("2016-07-21T16:43:15Z"), 0, 60.20261599f, 24.91159159f),
                                    new GpsPoint(getTimeStamp("2016-07-21T16:48:13Z"), 0, 60.20269592f, 24.91158244f),
                                    new GpsPoint(getTimeStamp("2016-07-21T16:53:14Z"), 0, 60.20252418f, 24.9115799f),
                                    new GpsPoint(getTimeStamp("2016-07-21T16:58:12Z"), 0, 60.20246735f, 24.91156805f),
                                    new GpsPoint(getTimeStamp("2016-07-21T17:03:14Z"), 0, 60.2024608f, 24.91157343f),
                                    new GpsPoint(getTimeStamp("2016-07-21T17:08:13Z"), 0, 60.20250173f, 24.91132393f),
                                    new GpsPoint(getTimeStamp("2016-07-21T17:13:12Z"), 0, 60.20246832f, 24.91138184f),
                                    new GpsPoint(getTimeStamp("2016-07-21T17:18:13Z"), 0, 60.20008161f, 24.90997215f),
                                    new GpsPoint(getTimeStamp("2016-07-21T17:22:53Z"), 0, 60.1960229f, 24.9082924f),
                                    new GpsPoint(getTimeStamp("2016-07-21T17:28:14Z"), 0, 60.19238234f, 24.91321786f),
                                    new GpsPoint(getTimeStamp("2016-07-21T17:33:03Z"), 0, 60.1896993f, 24.9166191f),
                                    new GpsPoint(getTimeStamp("2016-07-21T17:34:39Z"), 0, 60.18843191f, 24.91625642f),
                                    new GpsPoint(getTimeStamp("2016-07-21T17:36:01Z"), 0, 60.1883072f, 24.9155618f),
                                    new GpsPoint(getTimeStamp("2016-07-21T17:37:00Z"), 0, 60.1883566f, 24.9157122f),
                                    new GpsPoint(getTimeStamp("2016-07-21T17:38:02Z"), 0, 60.1883455f, 24.9156688f),
                                    new GpsPoint(getTimeStamp("2016-07-21T17:39:05Z"), 0, 60.1883397f, 24.91565f),
                                    new GpsPoint(getTimeStamp("2016-07-21T17:43:08Z"), 0, 60.1883576f, 24.9157109f),
                                    new GpsPoint(getTimeStamp("2016-07-21T17:44:30Z"), 0, 60.1883458f, 24.9156681f),
                                    new GpsPoint(getTimeStamp("2016-07-21T17:48:09Z"), 0, 60.1883578f, 24.9157111f),
                                    new GpsPoint(getTimeStamp("2016-07-21T17:53:09Z"), 0, 60.1883311f, 24.915627f),
                                    new GpsPoint(getTimeStamp("2016-07-21T17:58:10Z"), 0, 60.1882806f, 24.9154968f),
                                    new GpsPoint(getTimeStamp("2016-07-21T18:03:09Z"), 0, 60.1883334f, 24.9156363f),
                                    new GpsPoint(getTimeStamp("2016-07-21T18:08:08Z"), 0, 60.1883173f, 24.9156387f),
                                    new GpsPoint(getTimeStamp("2016-07-21T18:09:08Z"), 0, 60.1883452f, 24.9156686f),
                                    new GpsPoint(getTimeStamp("2016-07-21T18:13:09Z"), 0, 60.1883576f, 24.9157109f),
                                    new GpsPoint(getTimeStamp("2016-07-21T18:18:09Z"), 0, 60.1883569f, 24.9157109f),
                                    new GpsPoint(getTimeStamp("2016-07-21T18:23:48Z"), 0, 60.1883588f, 24.9157141f),
                                    new GpsPoint(getTimeStamp("2016-07-21T18:25:09Z"), 0, 60.1883672f, 24.9158448f),
                                    new GpsPoint(getTimeStamp("2016-07-21T18:26:30Z"), 0, 60.1883197f, 24.9156594f),
                                    new GpsPoint(getTimeStamp("2016-07-21T18:27:49Z"), 0, 60.1883803f, 24.9157559f),
                                    new GpsPoint(getTimeStamp("2016-07-21T18:33:08Z"), 0, 60.1883565f, 24.9157129f),
                                    new GpsPoint(getTimeStamp("2016-07-21T18:38:05Z"), 0, 60.1883573f, 24.9157105f),
                                    new GpsPoint(getTimeStamp("2016-07-21T18:43:05Z"), 0, 60.1883333f, 24.9156356f),
                                    new GpsPoint(getTimeStamp("2016-07-21T18:48:05Z"), 0, 60.1883569f, 24.9157109f),
                                    new GpsPoint(getTimeStamp("2016-07-21T18:53:43Z"), 0, 60.1879009f, 24.9155786f),
                                    new GpsPoint(getTimeStamp("2016-07-21T18:58:03Z"), 0, 60.188149f, 24.9154581f),
                                    new GpsPoint(getTimeStamp("2016-07-21T18:59:25Z"), 0, 60.1881621f, 24.9154939f),
                                    new GpsPoint(getTimeStamp("2016-07-21T19:03:04Z"), 0, 60.188137f, 24.9155106f),
                                    new GpsPoint(getTimeStamp("2016-07-21T19:08:05Z"), 0, 60.1881227f, 24.9154839f),
                                    new GpsPoint(getTimeStamp("2016-07-21T19:13:04Z"), 0, 60.1880537f, 24.9154374f),
                                    new GpsPoint(getTimeStamp("2016-07-21T19:18:18Z"), 0, 60.1882157f, 24.91564774f),
                                    new GpsPoint(getTimeStamp("2016-07-21T19:23:05Z"), 0, 60.1879858f, 24.9152748f),
                                    new GpsPoint(getTimeStamp("2016-07-21T19:28:13Z"), 0, 60.18836183f, 24.9158296f),
                                    new GpsPoint(getTimeStamp("2016-07-21T19:33:04Z"), 0, 60.1880771f, 24.9154413f),
                                    new GpsPoint(getTimeStamp("2016-07-21T19:38:03Z"), 0, 60.1880386f, 24.915403f),
                                    new GpsPoint(getTimeStamp("2016-07-21T19:39:04Z"), 0, 60.1882004f, 24.9154828f),
                                    new GpsPoint(getTimeStamp("2016-07-21T19:40:05Z"), 0, 60.1880412f, 24.9153847f),
                                    new GpsPoint(getTimeStamp("2016-07-21T19:41:06Z"), 0, 60.1880888f, 24.9154327f),
                                    new GpsPoint(getTimeStamp("2016-07-21T19:42:07Z"), 0, 60.1880425f, 24.9154449f),
                                    new GpsPoint(getTimeStamp("2016-07-21T19:43:08Z"), 0, 60.1880575f, 24.9154486f),
                                    new GpsPoint(getTimeStamp("2016-07-21T19:44:30Z"), 0, 60.1882018f, 24.9155438f),
                                    new GpsPoint(getTimeStamp("2016-07-21T19:45:52Z"), 0, 60.1880732f, 24.9154628f),
                                    new GpsPoint(getTimeStamp("2016-07-21T19:47:14Z"), 0, 60.18814f, 24.9154796f),
                                    new GpsPoint(getTimeStamp("2016-07-21T19:48:15Z"), 0, 60.1881604f, 24.9154697f),
                                    new GpsPoint(getTimeStamp("2016-07-21T19:49:15Z"), 0, 60.18828f, 24.9154966f),
                                    new GpsPoint(getTimeStamp("2016-07-21T19:50:16Z"), 0, 60.1881092f, 24.9154957f),
                                    new GpsPoint(getTimeStamp("2016-07-21T19:51:33Z"), 0, 60.1881026f, 24.9154497f),
                                    new GpsPoint(getTimeStamp("2016-07-21T19:52:34Z"), 0, 60.1882129f, 24.9155494f),
                                    new GpsPoint(getTimeStamp("2016-07-21T19:53:54Z"), 0, 60.1880417f, 24.9154207f),
                                    new GpsPoint(getTimeStamp("2016-07-21T19:55:16Z"), 0, 60.1880885f, 24.9154556f),
                                    new GpsPoint(getTimeStamp("2016-07-21T19:56:54Z"), 0, 60.1881148f, 24.9155213f),
                                    new GpsPoint(getTimeStamp("2016-07-21T19:57:54Z"), 0, 60.1881744f, 24.9154838f),
                                    new GpsPoint(getTimeStamp("2016-07-21T19:58:55Z"), 0, 60.1881484f, 24.9155267f),
                                    new GpsPoint(getTimeStamp("2016-07-21T20:00:04Z"), 0, 60.1880272f, 24.9154174f),
                                    new GpsPoint(getTimeStamp("2016-07-21T20:01:05Z"), 0, 60.1882865f, 24.9155983f),
                                    new GpsPoint(getTimeStamp("2016-07-21T20:02:06Z"), 0, 60.1878884f, 24.9156888f),
                                    new GpsPoint(getTimeStamp("2016-07-21T20:03:28Z"), 0, 60.1881418f, 24.9154762f),
                                    new GpsPoint(getTimeStamp("2016-07-21T20:04:28Z"), 0, 60.1880335f, 24.9154159f),
                                    new GpsPoint(getTimeStamp("2016-07-21T20:05:29Z"), 0, 60.1880805f, 24.9154394f),
                                    new GpsPoint(getTimeStamp("2016-07-21T20:06:30Z"), 0, 60.1880869f, 24.9154892f),
                                    new GpsPoint(getTimeStamp("2016-07-21T20:07:50Z"), 0, 60.1881191f, 24.9154863f),
                                    new GpsPoint(getTimeStamp("2016-07-21T20:08:51Z"), 0, 60.188117f, 24.915456f),
                                    new GpsPoint(getTimeStamp("2016-07-21T20:09:52Z"), 0, 60.1881477f, 24.9154774f),
                                    new GpsPoint(getTimeStamp("2016-07-21T20:10:53Z"), 0, 60.1881393f, 24.9154759f),
                                    new GpsPoint(getTimeStamp("2016-07-21T20:12:14Z"), 0, 60.1881198f, 24.9154543f),
                                    new GpsPoint(getTimeStamp("2016-07-21T20:13:15Z"), 0, 60.188039f, 24.9153764f),
                                    new GpsPoint(getTimeStamp("2016-07-21T20:14:15Z"), 0, 60.1879565f, 24.9153439f),
                                    new GpsPoint(getTimeStamp("2016-07-21T20:15:16Z"), 0, 60.1880953f, 24.915486f),
                                    new GpsPoint(getTimeStamp("2016-07-21T20:16:52Z"), 0, 60.18827f, 24.9155659f),
                                    new GpsPoint(getTimeStamp("2016-07-21T20:17:53Z"), 0, 60.1882932f, 24.9155846f),
                                    new GpsPoint(getTimeStamp("2016-07-21T20:19:16Z"), 0, 60.1882542f, 24.9155294f),
                                    new GpsPoint(getTimeStamp("2016-07-21T20:20:17Z"), 0, 60.1881937f, 24.9155508f),
                                    new GpsPoint(getTimeStamp("2016-07-21T20:21:20Z"), 0, 60.1881882f, 24.915495f),
                                    new GpsPoint(getTimeStamp("2016-07-21T20:22:42Z"), 0, 60.1881568f, 24.9154794f),
                                    new GpsPoint(getTimeStamp("2016-07-21T20:23:43Z"), 0, 60.1881695f, 24.9154879f),
                                    new GpsPoint(getTimeStamp("2016-07-21T20:24:43Z"), 0, 60.1881725f, 24.9155303f),
                                    new GpsPoint(getTimeStamp("2016-07-21T20:25:44Z"), 0, 60.188051f, 24.9154279f),
                                    new GpsPoint(getTimeStamp("2016-07-21T20:26:46Z"), 0, 60.1881701f, 24.9154766f),
                                    new GpsPoint(getTimeStamp("2016-07-21T20:27:47Z"), 0, 60.188255f, 24.9155288f),
                                    new GpsPoint(getTimeStamp("2016-07-21T20:28:48Z"), 0, 60.188119f, 24.9154859f),
                                    new GpsPoint(getTimeStamp("2016-07-21T20:29:48Z"), 0, 60.1881212f, 24.915459f),
                                    new GpsPoint(getTimeStamp("2016-07-21T20:30:54Z"), 0, 60.1880411f, 24.915426f),
                                    new GpsPoint(getTimeStamp("2016-07-21T20:31:55Z"), 0, 60.1881038f, 24.9154607f),
                                    new GpsPoint(getTimeStamp("2016-07-21T20:32:55Z"), 0, 60.1881244f, 24.9154663f),
                                    new GpsPoint(getTimeStamp("2016-07-21T20:33:56Z"), 0, 60.1881114f, 24.9154554f),
                                    new GpsPoint(getTimeStamp("2016-07-21T20:34:57Z"), 0, 60.1880757f, 24.9154321f),
                                    new GpsPoint(getTimeStamp("2016-07-21T20:36:14Z"), 0, 60.1880647f, 24.9154301f),
                                    new GpsPoint(getTimeStamp("2016-07-21T20:37:15Z"), 0, 60.1880792f, 24.9154535f),
                                    new GpsPoint(getTimeStamp("2016-07-21T20:38:16Z"), 0, 60.1880973f, 24.9154415f),
                                    new GpsPoint(getTimeStamp("2016-07-21T20:39:39Z"), 0, 60.1880884f, 24.9154711f),
                                    new GpsPoint(getTimeStamp("2016-07-21T20:40:40Z"), 0, 60.1880788f, 24.915478f)
                            )
                    )
            );
            add(
                    new TestObject(
                            //messeniuksenkatu 5, caloniuksenkatu 9, fredrikinkatu 46, salomonkatu 13, kaisaniemenkatu 5, mannerheimintie 53
                            new ArrayList<Coordinate>()
                            {{
                                add(new Coordinate(60.188130f, 24.915729f));
                                add(new Coordinate(60.174506f, 24.921122f));
                                add(new Coordinate(60.168940f, 24.930056f));
                                add(new Coordinate(60.169680f, 24.932042f));
                                add(new Coordinate(60.171325f, 24.947659f));
                                add(new Coordinate(60.189974f, 24.917420f));
                            }},

                            createListOfPlaces(
                                    new GpsPoint(getTimeStamp("2016-07-27T05:59:09Z"), 0, 60.188126f, 24.9154634f),
                                    new GpsPoint(getTimeStamp("2016-07-27T06:00:33Z"), 0, 60.1883202f, 24.9160571f),
                                    new GpsPoint(getTimeStamp("2016-07-27T06:03:21Z"), 0, 60.188281f, 24.9154961f),
                                    new GpsPoint(getTimeStamp("2016-07-27T06:07:38Z"), 0, 60.1882587f, 24.9155341f),
                                    new GpsPoint(getTimeStamp("2016-07-27T06:12:30Z"), 0, 60.1878406f, 24.9157591f),
                                    new GpsPoint(getTimeStamp("2016-07-27T06:13:48Z"), 0, 60.18713043f, 24.91661194f),
                                    new GpsPoint(getTimeStamp("2016-07-27T06:14:38Z"), 0, 60.1864479f, 24.9176407f),
                                    new GpsPoint(getTimeStamp("2016-07-27T06:16:09Z"), 0, 60.1850726f, 24.9184184f),
                                    new GpsPoint(getTimeStamp("2016-07-27T06:17:20Z"), 0, 60.18403587f, 24.91944491f),
                                    new GpsPoint(getTimeStamp("2016-07-27T06:22:48Z"), 0, 60.17960124f, 24.92453228f),
                                    new GpsPoint(getTimeStamp("2016-07-27T06:27:46Z"), 0, 60.17542971f, 24.92235781f),
                                    new GpsPoint(getTimeStamp("2016-07-27T06:32:38Z"), 0, 60.1747303f, 24.9210567f),
                                    new GpsPoint(getTimeStamp("2016-07-27T06:37:38Z"), 0, 60.1747406f, 24.9211334f),
                                    new GpsPoint(getTimeStamp("2016-07-27T06:42:37Z"), 0, 60.1747348f, 24.9211073f),
                                    new GpsPoint(getTimeStamp("2016-07-27T06:47:38Z"), 0, 60.1747348f, 24.9210544f),
                                    new GpsPoint(getTimeStamp("2016-07-27T06:52:38Z"), 0, 60.1748044f, 24.9212069f),
                                    new GpsPoint(getTimeStamp("2016-07-27T06:57:38Z"), 0, 60.1747105f, 24.9210599f),
                                    new GpsPoint(getTimeStamp("2016-07-27T07:02:59Z"), 0, 60.1747545f, 24.9211404f),
                                    new GpsPoint(getTimeStamp("2016-07-27T07:07:59Z"), 0, 60.1747352f, 24.9210897f),
                                    new GpsPoint(getTimeStamp("2016-07-27T07:12:38Z"), 0, 60.1747005f, 24.9210439f),
                                    new GpsPoint(getTimeStamp("2016-07-27T07:17:38Z"), 0, 60.1747912f, 24.9211676f),
                                    new GpsPoint(getTimeStamp("2016-07-27T07:22:58Z"), 0, 60.1747402f, 24.9210873f),
                                    new GpsPoint(getTimeStamp("2016-07-27T07:27:37Z"), 0, 60.1746727f, 24.9209416f),
                                    new GpsPoint(getTimeStamp("2016-07-27T07:28:56Z"), 0, 60.1746929f, 24.9210103f),
                                    new GpsPoint(getTimeStamp("2016-07-27T07:32:38Z"), 0, 60.1746862f, 24.9210059f),
                                    new GpsPoint(getTimeStamp("2016-07-27T07:37:38Z"), 0, 60.174708f, 24.9210658f),
                                    new GpsPoint(getTimeStamp("2016-07-27T07:42:38Z"), 0, 60.1747119f, 24.9210301f),
                                    new GpsPoint(getTimeStamp("2016-07-27T07:47:38Z"), 0, 60.1746962f, 24.9210078f),
                                    new GpsPoint(getTimeStamp("2016-07-27T07:52:58Z"), 0, 60.1746334f, 24.9208837f),
                                    new GpsPoint(getTimeStamp("2016-07-27T07:57:37Z"), 0, 60.1747615f, 24.9211704f),
                                    new GpsPoint(getTimeStamp("2016-07-27T08:02:37Z"), 0, 60.1747372f, 24.9210674f),
                                    new GpsPoint(getTimeStamp("2016-07-27T08:07:38Z"), 0, 60.1746381f, 24.9209146f),
                                    new GpsPoint(getTimeStamp("2016-07-27T08:12:37Z"), 0, 60.1746389f, 24.9209394f),
                                    new GpsPoint(getTimeStamp("2016-07-27T08:17:38Z"), 0, 60.174612f, 24.9209402f),
                                    new GpsPoint(getTimeStamp("2016-07-27T08:22:39Z"), 0, 60.1747082f, 24.9210802f),
                                    new GpsPoint(getTimeStamp("2016-07-27T08:27:38Z"), 0, 60.1747285f, 24.9210708f),
                                    new GpsPoint(getTimeStamp("2016-07-27T08:32:57Z"), 0, 60.1746059f, 24.9209535f),
                                    new GpsPoint(getTimeStamp("2016-07-27T08:37:38Z"), 0, 60.1747395f, 24.9210652f),
                                    new GpsPoint(getTimeStamp("2016-07-27T08:42:38Z"), 0, 60.174711f, 24.9210804f),
                                    new GpsPoint(getTimeStamp("2016-07-27T08:47:38Z"), 0, 60.1746906f, 24.9210141f),
                                    new GpsPoint(getTimeStamp("2016-07-27T08:52:39Z"), 0, 60.1747345f, 24.9210616f),
                                    new GpsPoint(getTimeStamp("2016-07-27T08:57:38Z"), 0, 60.174703f, 24.9210857f),
                                    new GpsPoint(getTimeStamp("2016-07-27T09:02:37Z"), 0, 60.1747302f, 24.9209874f),
                                    new GpsPoint(getTimeStamp("2016-07-27T09:07:38Z"), 0, 60.1714323f, 24.9246576f),
                                    new GpsPoint(getTimeStamp("2016-07-27T09:12:42Z"), 0, 60.16825866f, 24.93044187f),
                                    new GpsPoint(getTimeStamp("2016-07-27T09:17:38Z"), 0, 60.1689262f, 24.9301937f),
                                    new GpsPoint(getTimeStamp("2016-07-27T09:23:00Z"), 0, 60.1689108f, 24.9301516f),
                                    new GpsPoint(getTimeStamp("2016-07-27T09:27:40Z"), 0, 60.1688971f, 24.9300941f),
                                    new GpsPoint(getTimeStamp("2016-07-27T09:32:40Z"), 0, 60.1694874f, 24.9321633f),
                                    new GpsPoint(getTimeStamp("2016-07-27T09:43:20Z"), 0, 60.1698479f, 24.9311765f),
                                    new GpsPoint(getTimeStamp("2016-07-27T09:47:38Z"), 0, 60.1712367f, 24.932442f),
                                    new GpsPoint(getTimeStamp("2016-07-27T09:52:38Z"), 0, 60.1696265f, 24.9326437f),
                                    new GpsPoint(getTimeStamp("2016-07-27T09:54:01Z"), 0, 60.1712362f, 24.9324363f),
                                    new GpsPoint(getTimeStamp("2016-07-27T09:55:00Z"), 0, 60.1712354f, 24.9324377f),
                                    new GpsPoint(getTimeStamp("2016-07-27T09:55:52Z"), 0, 60.1712364f, 24.9324363f),
                                    new GpsPoint(getTimeStamp("2016-07-27T09:57:05Z"), 0, 60.1712365f, 24.9324367f),
                                    new GpsPoint(getTimeStamp("2016-07-27T09:58:04Z"), 0, 60.1712376f, 24.9324396f),
                                    new GpsPoint(getTimeStamp("2016-07-27T10:02:39Z"), 0, 60.1701725f, 24.9329873f),
                                    new GpsPoint(getTimeStamp("2016-07-27T10:07:40Z"), 0, 60.1698174f, 24.9378402f),
                                    new GpsPoint(getTimeStamp("2016-07-27T10:12:43Z"), 0, 60.17065376f, 24.94512186f),
                                    new GpsPoint(getTimeStamp("2016-07-27T10:13:44Z"), 0, 60.1713436f, 24.9467049f),
                                    new GpsPoint(getTimeStamp("2016-07-27T10:17:39Z"), 0, 60.1713024f, 24.9472077f),
                                    new GpsPoint(getTimeStamp("2016-07-27T10:22:51Z"), 0, 60.1710273f, 24.9471065f),
                                    new GpsPoint(getTimeStamp("2016-07-27T10:28:39Z"), 0, 60.1715059f, 24.9473032f),
                                    new GpsPoint(getTimeStamp("2016-07-27T10:32:47Z"), 0, 60.1720767f, 24.9474534f),
                                    new GpsPoint(getTimeStamp("2016-07-27T10:43:37Z"), 0, 60.1714415f, 24.9474317f),
                                    new GpsPoint(getTimeStamp("2016-07-27T10:44:59Z"), 0, 60.1714061f, 24.9475057f),
                                    new GpsPoint(getTimeStamp("2016-07-27T10:46:20Z"), 0, 60.1713774f, 24.9476414f),
                                    new GpsPoint(getTimeStamp("2016-07-27T10:47:40Z"), 0, 60.1714167f, 24.9475967f),
                                    new GpsPoint(getTimeStamp("2016-07-27T10:49:04Z"), 0, 60.1713965f, 24.94743f),
                                    new GpsPoint(getTimeStamp("2016-07-27T10:59:21Z"), 0, 60.1713959f, 24.9474161f),
                                    new GpsPoint(getTimeStamp("2016-07-27T11:07:13Z"), 0, 60.1713723f, 24.9475181f),
                                    new GpsPoint(getTimeStamp("2016-07-27T11:16:12Z"), 0, 60.1713345f, 24.9475456f),
                                    new GpsPoint(getTimeStamp("2016-07-27T11:18:25Z"), 0, 60.171393f, 24.947462f),
                                    new GpsPoint(getTimeStamp("2016-07-27T11:22:37Z"), 0, 60.1724696f, 24.9474869f),
                                    new GpsPoint(getTimeStamp("2016-07-27T11:24:16Z"), 0, 60.1731405f, 24.946196f),
                                    new GpsPoint(getTimeStamp("2016-07-27T11:25:53Z"), 0, 60.17436128f, 24.94488519f),
                                    new GpsPoint(getTimeStamp("2016-07-27T11:27:00Z"), 0, 60.1750487f, 24.94401848f),
                                    new GpsPoint(getTimeStamp("2016-07-27T11:28:05Z"), 0, 60.17586564f, 24.94337777f),
                                    new GpsPoint(getTimeStamp("2016-07-27T11:37:45Z"), 0, 60.18222252f, 24.93691642f),
                                    new GpsPoint(getTimeStamp("2016-07-27T11:39:15Z"), 0, 60.18298155f, 24.93642478f),
                                    new GpsPoint(getTimeStamp("2016-07-27T11:40:27Z"), 0, 60.18419174f, 24.93578585f),
                                    new GpsPoint(getTimeStamp("2016-07-27T11:41:39Z"), 0, 60.18474446f, 24.93524939f),
                                    new GpsPoint(getTimeStamp("2016-07-27T11:42:41Z"), 0, 60.18480503f, 24.93390746f),
                                    new GpsPoint(getTimeStamp("2016-07-27T11:47:22Z"), 0, 60.1843054f, 24.9267192f),
                                    new GpsPoint(getTimeStamp("2016-07-27T11:52:43Z"), 0, 60.18695305f, 24.92132319f),
                                    new GpsPoint(getTimeStamp("2016-07-27T11:57:45Z"), 0, 60.18941558f, 24.91762512f),
                                    new GpsPoint(getTimeStamp("2016-07-27T12:02:35Z"), 0, 60.1898481f, 24.9169182f),
                                    new GpsPoint(getTimeStamp("2016-07-27T12:03:25Z"), 0, 60.1898223f, 24.9168796f),
                                    new GpsPoint(getTimeStamp("2016-07-27T12:05:19Z"), 0, 60.19010783f, 24.91759026f),
                                    new GpsPoint(getTimeStamp("2016-07-27T12:06:45Z"), 0, 60.18980769f, 24.91777586f),
                                    new GpsPoint(getTimeStamp("2016-07-27T12:07:51Z"), 0, 60.19015292f, 24.91746752f),
                                    new GpsPoint(getTimeStamp("2016-07-27T12:09:01Z"), 0, 60.19031122f, 24.91745363f),
                                    new GpsPoint(getTimeStamp("2016-07-27T12:12:59Z"), 0, 60.1897817f, 24.9166776f),
                                    new GpsPoint(getTimeStamp("2016-07-27T12:18:00Z"), 0, 60.1885102f, 24.9163856f),
                                    new GpsPoint(getTimeStamp("2016-07-27T12:22:58Z"), 0, 60.1883893f, 24.9157668f),
                                    new GpsPoint(getTimeStamp("2016-07-27T12:28:30Z"), 0, 60.1883338f, 24.915625f),
                                    new GpsPoint(getTimeStamp("2016-07-27T12:32:37Z"), 0, 60.1884234f, 24.9160711f),
                                    new GpsPoint(getTimeStamp("2016-07-27T12:34:22Z"), 0, 60.18848165f, 24.91662861f),
                                    new GpsPoint(getTimeStamp("2016-07-27T12:37:37Z"), 0, 60.188281f, 24.9154965f)
                            )
                    )
            );
        }};

    }

    private static void print(Place place, int order){
        System.out.println("<trkpt lat=\"" + place.getCoordinate().getLatitude() + "\" lon=\"" + place.getCoordinate().getLongitude() +"\"><time>2016-07-21T"+order+":00:00Z</time><src>network</src></trkpt>");
    }

    @Test
    public void theresAlwaysOneGpsPointInDatabase() {
        GpsPointClusterizer GpsPointClusterizer = new GpsPointClusterizer(this.context);
        for(TestObject testObject : this.testObjects) {
            for(int i = 0; i < testObject.getTestData().size(); i++) {
                GpsPointDao.insert(testObject.getTestData().get(i));
                GpsPointClusterizer.updateVisitHistory(GpsPointDao.getAll());
                assertTrue(true);
            }
            System.out.println("At the end theres this many gps points: " + GpsPointDao.getAll().size());
        }
    }

    @Test
    public void clusterizerFindsCorrectPlaces() {
        GpsPointClusterizer GpsPointClusterizer = new GpsPointClusterizer(this.context);
        double distanceSum = 0;
        long placeSum = 0;
        for(TestObject testObject : this.testObjects){

            placeSum += testObject.getCorrectCoordinates().size();

            for(int i = 0; i < testObject.getTestData().size(); i++) {
                GpsPointDao.insert(testObject.getTestData().get(i));
                GpsPointClusterizer.updateVisitHistory(GpsPointDao.getAll());
            }

            List<Place> places = PlaceDao.getAll();
            for(Place place : places) {
                print(place, 0);
            }
            System.out.println("------------------");

            assertTrue(places.size() == testObject.correctCoordinates.size());
            for(int i = 0; i < testObject.correctCoordinates.size(); i++) {
                distanceSum+=testObject.getCorrectCoordinates().get(i).distanceTo(places.get(i).getCoordinate());
                System.out.println(testObject.getCorrectCoordinates().get(i).distanceTo(places.get(i).getCoordinate()));
                assertTrue(testObject.getCorrectCoordinates().get(i).distanceTo(places.get(i).getCoordinate()) < 100);
            }
            PlaceDao.deleteAllData();
            GpsPointDao.deleteAllData();
        }
        System.out.println("Average difference between calculated and real locations: " + distanceSum/placeSum);
        assertTrue(distanceSum/placeSum < 40);
    }

    private List<GpsPoint> createListOfPlaces(GpsPoint... GpsPoints) {
        return new ArrayList<GpsPoint>(Arrays.asList(GpsPoints));
    }

    private List<GpsPoint> deleteRandomPlacesFromList(List<GpsPoint> GpsPoints, double percentage) {
        Random random = new Random();
        List<GpsPoint> prunedList = new ArrayList<>();
        for (GpsPoint GpsPoint : GpsPoints) {
            if (random.nextDouble() > percentage) {
                prunedList.add(GpsPoint);
            }
        }
        return prunedList;
    }

    private static long getTimeStamp(String time) {
        TimeZone tz = TimeZone.getTimeZone("Europe/Helsinki");
        Calendar cal = Calendar.getInstance(tz);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        sdf.setCalendar(cal);
        try {
            cal.setTime(sdf.parse(time));
            return cal.getTimeInMillis();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    private static String getTimeFormatted(long timestamp) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        Date now = new Date(timestamp);
        String strDate = sdf.format(now);
        return strDate;
    }

}
