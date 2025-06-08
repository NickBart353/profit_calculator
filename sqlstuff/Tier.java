public class Tier{
    private final int id;
    private final String tech_name;

    public Tier(int id, String tName){
        this.id = id;
        tech_name = tName;
    }

    public int getId() {
        return id;
    }

    public String getTech_name() {
        return tech_name;
    }
}