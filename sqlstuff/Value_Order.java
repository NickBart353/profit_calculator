import java.text.SimpleDateFormat;
import java.util.Date;

public class Value_Order{

    private int id;
    private final int value_id;
    private int sort_order;
    private String last_updated;

    public Value_Order(int sort_order, int value_id) {
        this.sort_order = sort_order;
        this.value_id = value_id;

        Date now = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        last_updated = sdf.format(now);
    }

    public Value_Order(int id, int value_id, int sort_order, String last_updated) {
        this.id = id;
        this.last_updated = last_updated;
        this.sort_order = sort_order;
        this.value_id = value_id;
    }

    public String getLast_updated() {
        return last_updated;
    }

    public void setLast_updated(String last_updated) {
        this.last_updated = last_updated;
    }

    public int getSort_order() {
        return sort_order;
    }

    public void setSort_order(int sort_order) {
        this.sort_order = sort_order;
    }

    public int getValue_id() {
        return value_id;
    }

    public int getId() {
        return id;
    }
}