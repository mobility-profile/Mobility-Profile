package fi.ohtu.mobilityprofile.domain;

import com.orm.SugarRecord;
import com.orm.query.Condition;
import com.orm.query.Select;

import java.util.List;

public class TransportMode extends SugarRecord {

    private String name;
    private boolean favourite;

    public TransportMode() {
    }

    public TransportMode(String name) {
        this.name = name;
        this.favourite = false;
    }

    public String getName() {
        return name;
    }

    public boolean isFavourite() {
        return favourite;
    }

    public void setFavourite(boolean favourite) {
        this.favourite = favourite;
    }

    public static TransportMode getByName(String name) {
        List<TransportMode> modes = Select.from(TransportMode.class)
                .where(Condition.prop("name").eq(name))
                .limit("1")
                .list();

        return modes.get(0);
    }
}
