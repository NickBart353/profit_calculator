import java.io.IOException;
import java.net.InterfaceAddress;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.sql.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.concurrent.TimeUnit;

import org.json.JSONArray;
import org.json.JSONObject;



public class Main{
    public static void main(String[] args){
        Connection conn;
        Statement stmt;
        ResultSet rs;

        ArrayList<Item> items = new ArrayList<>();
        ArrayList<City> cities = new ArrayList<>();
        ArrayList<Tier> tiers = new ArrayList<>();
        ArrayList<Enchantment> enchantments = new ArrayList<>();
        ArrayList<Value> values = new ArrayList<>();
        

        try {

            // 1. Datenbankverbindung
            conn = DriverManager.getConnection("jdbc:sqlite:Albion_Calc_Data.db");
            
            if (conn != null) {
                System.out.println("Verbindung zur Datenbank erfolgreich hergestellt.");
            } else {
                System.out.println("Fehler beim Herstellen der Verbindung.");
            }
            
            stmt = conn.createStatement();

            // 2.1 Lesen und befüllen von Items
            rs = stmt.executeQuery("SELECT * FROM Items;");
            while(rs.next()){
                items.add(new Item(rs.getInt("id"),rs.getString("tech_name"),rs.getString("display_name"),rs.getBoolean("is_completed")));
            }

            // 2.2 Lesen und befüllen von Cities
            rs = stmt.executeQuery("SELECT * FROM Cities;");
            while(rs.next()){
                cities.add(new City(rs.getInt("id"),rs.getString("tech_name"),rs.getString("display_name")));
                //System.out.println(rs.getInt("id")+"\n"+rs.getString("tech_name")+"\n"+rs.getString("display_name"));
            }

            // 2.3 Lesen und befüllen von Tiers
            rs = stmt.executeQuery("SELECT * FROM Tiers;");
            while(rs.next()){
                tiers.add(new Tier(rs.getInt("id"),rs.getString("tech_name")));
            }

            // 2.4 Lesen und befüllen von Enchantments
            rs = stmt.executeQuery("SELECT * FROM Enchantments;");
            while(rs.next()){
                enchantments.add(new Enchantment(rs.getInt("id"), rs.getString("html_text"), rs.getString("display_color")));
            }
            
            // 2.5 Lesen und befüllen von Values
            rs = stmt.executeQuery("SELECT * FROM [Values];");
            while(rs.next()){
                values.add(new Value(rs.getInt("id"), rs.getInt("items_id"), rs.getInt("from_city_id"), rs.getInt("to_city_id"), rs.getInt("tier_id"), rs.getInt("rarity_id"), rs.getDouble("value"), rs.getString("last_updated")));
            }      

        } catch (SQLException ex) {
            System.out.println(ex);
            return;
        }
        
        try {
            // 4. HTTP Request senden
            HttpClient client = HttpClient.newHttpClient();
            for (Item item : items) {
                if (!item.get_is_completed()){
                    for(Tier tier : tiers){
                        for(Enchantment enchantment : enchantments){                        
                            String item_builder = "";
                            TimeUnit.SECONDS.sleep(1);
                            item_builder = item_builder + tier.getTech_name() + "_" + item.getTech_name() + enchantment.getHtmltext();

                            HttpRequest request = HttpRequest.newBuilder()
                                .uri(URI.create("https://europe.albion-online-data.com/api/v2/stats/prices/" + item_builder + "?locations=bridgewatch,caerleon,fort%20sterling,martlock,brecilien,thetford,lymhurst&qualities=1"))
                                .header("Accept-Encoding", "gzip, deflate")
                                .GET()
                                .build();
                            HttpResponse<byte[]> response = client.send(request, HttpResponse.BodyHandlers.ofByteArray());
                            byte[] responseBody = response.body();
                            String decompressed = "";

                            if ("gzip".equalsIgnoreCase(response.headers().firstValue("Content-Encoding").orElse(""))) {
                                // decompress gzip response
                                try (java.util.zip.GZIPInputStream gis = new java.util.zip.GZIPInputStream(new java.io.ByteArrayInputStream(responseBody))) {
                                    decompressed = new String(gis.readAllBytes(), java.nio.charset.StandardCharsets.UTF_8);
                                    //System.out.println("Successful GET!");
                                }
                            }
                            JSONArray jsonArray = new JSONArray(decompressed);    

                            for (int i = 0; i < jsonArray.length(); i++) {      
                                JSONObject from_city_object = jsonArray.getJSONObject(i);  
                                String from_city = from_city_object.getString("city");
                                Double from_city_price = from_city_object.getDouble("sell_price_min");

                                for (int j = 0; j < jsonArray.length(); j++) {  
                                    JSONObject to_city_object = jsonArray.getJSONObject(j);
                                    String to_city = to_city_object.getString("city");     
                                    
                                    if (!from_city.equals(to_city)) {
                                        
                                        String tempStr = to_city_object.getString("item_id");
                                        String item_name;
                                        String item_enchant = "";

                                                   

                                        if(tempStr.endsWith("1") || tempStr.endsWith("2") || tempStr.endsWith("3") ||tempStr.endsWith("4")){
                                            item_name = tempStr.substring(3,tempStr.length()-2);
                                            item_enchant = tempStr.substring(tempStr.length()-2);
                                        }else{
                                            item_name = tempStr.substring(3);
                                        }

                                        
                                        String item_tier = tempStr.substring(0,2);  
                                        Double to_city_price = to_city_object.getDouble("buy_price_max");

                                        Value value_to_add = new Value(getItems_id_from_name(item_name,items), getCity_id_from_name(from_city, cities) , getCity_id_from_name(to_city, cities), getTier_id_from_name(item_tier,tiers), getEnchant_id_from_name(item_enchant, enchantments), (to_city_price - from_city_price), from_city_price, to_city_price);
                                        values.add(value_to_add);
                                        //System.out.println("from city id: "+getCity_id_from_name(from_city,cities) + "\nto and from city name: "+ to_city + ", "+  from_city+"\nactual added object city id: "+value_to_add.getFrom_city_id());

                                        PreparedStatement insert = null;
                                        try {                                    
                                            insert = conn.prepareStatement("INSERT INTO [Values] (items_id, from_city_id, to_city_id, tier_id, value, last_updated, rarity_id, buy_price, sell_price) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?);");
                                            insert.setString(1, Integer.toString(value_to_add.getItems_id()));
                                            insert.setString(2, Integer.toString(value_to_add.getFrom_city_id()));
                                            insert.setString(3, Integer.toString(value_to_add.getTo_city_id()));
                                            insert.setString(4, Integer.toString(value_to_add.getTier_id()));
                                            insert.setString(5, Double.toString(value_to_add.getValue()));
                                            insert.setString(6, value_to_add.getLast_updated());
                                            insert.setString(7, Integer.toString(value_to_add.getEnchant_id()));
                                            insert.setString(8, Double.toString(value_to_add.getBuy_price()));
                                            insert.setString(9, Double.toString(value_to_add.getSell_price()));
                                            insert.executeUpdate();
                                            //System.out.println("Successful INSERT!"+j+" "+i);
                                        } catch (SQLException e) {
                                            System.out.println("SQL Statement: "+ insert +"\nError: \n"+ e);
                                        }
                                    }
                                }
                            }
                        }

                    }
                    item.set_is_completed(true);
                    try {
                        PreparedStatement update;
                        update = conn.prepareStatement("UPDATE Items SET is_completed = '1' WHERE id = ?");
                        update.setString(1, Integer.toString(item.getId()));
                        update.executeUpdate();
                        System.out.println("Successful UPDATE "+ item.getTech_name());
                    } catch (SQLException ex) {
                        System.out.println(ex);
                    }
                }
            }
        } catch (IOException | InterruptedException e) {
            System.out.println(e);
            return;
        }

        try {
            PreparedStatement update;
            update = conn.prepareStatement("UPDATE Items SET is_completed = '0' WHERE is_completed = '1'");
            update.executeUpdate();
        } catch (SQLException ex) {
            System.out.println(ex);
        }

        //Ressourcen freigeben
        try {
            stmt.close();
            conn.close();
        } catch (SQLException e) {
            System.out.println(e);
        }
    }

    private static int getItems_id_from_name(String tech_name,ArrayList<Item> itemlist){
        
        for(Item item : itemlist){
            if (item.getTech_name().equals(tech_name)){
                return item.getId();
            }
        }        
        return 0;
    }

    private static int getCity_id_from_name(String tech_name,ArrayList<City> citylist){
        
        for(City city : citylist){
            if (city.getDisplay_name().equals(tech_name)){
                return city.getId();
            }
        }        
        return 0;
    }

    private static int getTier_id_from_name(String tech_name,ArrayList<Tier> tierlist){
        
        for(Tier tier : tierlist){
            if (tier.getTech_name().equals(tech_name)){
                return tier.getId();
            }
        }        
        return 0;
    }

    private static int getEnchant_id_from_name(String html_text,ArrayList<Enchantment> enchantments){
        
        for(Enchantment enchant : enchantments){
            if (enchant.getHtmltext().equals(html_text)){
                return enchant.getId();
            }
        }        
        return 0;
    }
}