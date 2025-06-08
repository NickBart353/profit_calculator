public class Item{

    private final int id;
    private final String tech_name;
    private final String display_name;
    private boolean is_completed;

    public Item(int id, String tName, String dName, boolean is_completed){
        this.id = id;
        tech_name = tName;
        display_name = dName;
        this.is_completed = is_completed;
    }

    public boolean get_is_completed() {
        return is_completed;
    }

    public void set_is_completed(boolean is_completed) {
        this.is_completed = is_completed;
    }

    public String getDisplay_name() {
        return display_name;
    }

    public String getTech_name() {
        return tech_name;
    }

    public int getId() {
        return id;
    }  
}