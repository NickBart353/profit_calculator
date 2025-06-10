public class Enchantment{

    private final int id;
    private final String htmltext;
    private final String display_color;

    public Enchantment(int id, String htmltext, String display_color) {
        this.id = id;
        this.htmltext = htmltext;
        this.display_color = display_color;
    }

    public int getId() {
        return id;
    }

    public String getDisplay_color() {
        return display_color;
    }

    public String getHtmltext() {
        return htmltext;
    }
}