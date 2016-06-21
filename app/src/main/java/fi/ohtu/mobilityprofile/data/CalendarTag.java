package fi.ohtu.mobilityprofile.data;

import com.orm.SugarRecord;

/**
 * Class is used to create and save calendar tags, which have calendar event locations as keys
 * and user destination inputs as values.
 */
public class CalendarTag extends SugarRecord {
    String key;
    String value;
    int counter;

    /**
     *
     */
    public CalendarTag() {
    }

    /**
     * Creates CalendarTag.
     * @param key key of the tag
     * @param value value of the tag
     */
    public CalendarTag(String key, String value) {
        this.key = key;
        this.value = value;
        this.counter = 1;
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }
}
