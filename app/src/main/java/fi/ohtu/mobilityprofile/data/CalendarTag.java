package fi.ohtu.mobilityprofile.data;

import com.orm.SugarRecord;

/**
 *
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
     *
     * @param key
     * @param value
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
