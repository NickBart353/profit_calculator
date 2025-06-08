public class City{
    private final int id;
    private final String tech_name;
    private final String display_name;

    public City(int id, String tName, String dName){
        this.id = id;
        tech_name = tName;
        display_name = dName;
    }

    public int getId() {
        return id;
    }
    
    public String getTech_name() {
        return tech_name;
    }

    public String getDisplay_name() {
        return display_name;
    }

}