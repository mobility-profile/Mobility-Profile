package fi.ohtu.mobilityprofile.data;

import java.util.List;

/**
 * DAO used for saving and reading CalendarTags to/from the database.
 */
public class CalendarTagDao {
    /**
     * Returns a calendar tag that has the given key. If there are multiple tags with the same key,
     * the tag with the highest counter is returned. Returns null if no tags with the given key
     * were found.
     *
     * @param key Key of the calendar tag
     * @return Calendar tag with the highest counter
     */
    public static CalendarTag findTheMostUsedTag(String key) {
        List<CalendarTag> calendarTags = CalendarTag.findWithQuery(CalendarTag.class,
                "SELECT * FROM CalendarTag " +
                        "WHERE key = ? " +
                        "ORDER BY counter DESC " +
                        "LIMIT 1", key);

        assert calendarTags.size() <= 1 : "Invalid SQL query: only one or zero entities should have been returned!";

        if (calendarTags.size() == 0) {
            return null;
        }

        return calendarTags.get(0);
    }

    /**
     * Saves the given tag to the database. If a tag with the same key-value pair already exists,
     * new row isn't added, but the tag's counter is instead increased by one. If the tag doesn't
     * exist yet, its counter is set to one, and the tag is then saved to the database.
     *
     * @param tag Tag to be saved
     */
    public static void insertCalendarTag(CalendarTag tag) {
        List<CalendarTag> calendarTags = CalendarTag.find(CalendarTag.class,
                "key = ? AND value = ?", tag.key, tag.value);

        assert calendarTags.size() <= 1 : "Inconsistent database: only one key-value pair should exist!";

        if (calendarTags.size() == 0) {
            // Tag with the key-value pair doesn't exist yet, so just save the given one.
            assert tag.counter == 1 : "New tag should always have its counter set to one!";
            tag.save();
            return;
        }

        // Tag already exists, just increase its counter by one and then save the modification to
        // the database.
        CalendarTag calendarTag = calendarTags.get(0);
        calendarTag.counter++;
        calendarTag.save();
    }
}
