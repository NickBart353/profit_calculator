
import java.text.SimpleDateFormat;
import java.util.Date;

public class Value{

    private int id;
    private final int items_id;
    private final int from_city_id;
    private final int to_city_id;
    private final int tier_id;
    private double value;
    private String last_updated;

    public Value(int items_id, int from_city_id, int to_city_id, int tier_id, double value) {
        this.items_id = items_id;
        this.from_city_id = from_city_id;
        this.to_city_id = to_city_id;
        this.tier_id = tier_id;
        this.value = value;

        Date now = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        last_updated = sdf.format(now);
    }

    public Value(int id, int items_id, int from_city_id, int to_city_id,  int tier_id, double value, String last_updated) {
        this.from_city_id = from_city_id;
        this.id = id;
        this.items_id = items_id;
        this.last_updated = last_updated;
        this.tier_id = tier_id;
        this.to_city_id = to_city_id;
        this.value = value;
    }

    public int getId() {
        return id;
    }

    public int getFrom_city_id() {
        return from_city_id;
    }

    public int getItems_id() {
        return items_id;
    }

    public int getTo_city_id() {
        return to_city_id;
    }

    public int getTier_id() {
        return tier_id;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public String getLast_updated() {
        return last_updated;
    }

    public void setLast_updated(String last_updated) {
        this.last_updated = last_updated;
    }

}