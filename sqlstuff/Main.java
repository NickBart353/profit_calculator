import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.sql.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
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
        //ArrayList<Enchantment> enchantments = new ArrayList<>();
        ArrayList<Value> values = new ArrayList<>();
        ArrayList<Value_Order> value_orders_list = new ArrayList<>();
        

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
            }

            // 2.3 Lesen und befüllen von Tiers
            rs = stmt.executeQuery("SELECT * FROM Tiers;");
            while(rs.next()){
                tiers.add(new Tier(rs.getInt("id"),rs.getString("tech_name")));
            }
            
            // 2.4 Lesen und befüllen von Values
            rs = stmt.executeQuery("SELECT * FROM [Values];");
            while(rs.next()){
                values.add(new Value(rs.getInt("id"), rs.getInt("items_id"), rs.getInt("from_city_id"), rs.getInt("to_city_id"), rs.getInt("tier_id"), rs.getDouble("value"), rs.getString("last_updated")));
            }            

            // 2.5 Lesen und befüllen von Values_Order
            rs = stmt.executeQuery("SELECT * FROM Value_Order;");
            while(rs.next()){
                value_orders_list.add(new Value_Order(rs.getInt("id"), rs.getInt("value_id"), rs.getInt("sort_order"), rs.getString("last_updated")));
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
                    TimeUnit.SECONDS.sleep(1);

                    String item_builder = "";

                    for(Tier tier : tiers){
                        item_builder = item_builder + tier.getTech_name() + "_" + item.getTech_name();

                        HttpRequest request = HttpRequest.newBuilder()
                            .uri(URI.create("https://europe.albion-online-data.com/api/v2/stats/prices/" + item_builder + "?locations=bridgewatch,caerleon,fort_sterling,martlock,brecilien,thetfort,lymhurst&qualities=1"))
                            .build();
                        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

                        JSONArray jsonArray = new JSONArray(response.body());            

                        for (int i = 0; i < jsonArray.length(); i++) {      
                            JSONObject from_city_object = jsonArray.getJSONObject(i);  
                            String from_city = from_city_object.getString("city");
                            Double from_city_price = from_city_object.getDouble("sell_price_min");

                            for (int j = 0; j < jsonArray.length(); j++) {       
                                JSONObject to_city_object = jsonArray.getJSONObject(j);

                                String item_name = to_city_object.getString("item_id").substring(3);
                                String item_tier = to_city_object.getString("item_id").substring(0,1);
                                String to_city = to_city_object.getString("city");
                                Double to_city_price = to_city_object.getDouble("buy_price_max");

                                values.add(new Value(getItems_id_from_name(item_name,items), getCity_id_from_name(from_city, cities) , getCity_id_from_name(to_city, cities), getTier_id_from_name(item_tier,tiers), (to_city_price - from_city_price)));
                            }
                        }
                    }
                    item.set_is_completed(true);
                    try {
                        PreparedStatement update;
                        int myInt = item.get_is_completed() ? 1 : 0;
                        update = conn.prepareStatement("UPDATE Items SET is_completed = ? WHERE id = 1");
                        update.setString(1, Integer.toString(myInt));
                        update.executeUpdate();
                    } catch (SQLException ex) {
                        System.out.println(ex);
                    }
                }
            }
        } catch (IOException | InterruptedException e) {
            System.out.println(e);
            return;
        }

        // 5. Sort_Order festlegen

        values.sort(Comparator.comparingDouble(Value::getValue).reversed());

        for(int i = 0; i < values.size(); i++){
            value_orders_list.add(new Value_Order(i,values.get(i).getId()));
        }

        // 6. Daten speichern in value tabelle
        for (Value value : values){
            PreparedStatement insert = null;
            try {
                insert = conn.prepareStatement("INSERT INTO [Values] (items_id, from_city_id, to_city_id, tier_id, value, last_updated) VALUES (?, ?, ?, ?, ?, ?);");
                insert.setString(1, Integer.toString(value.getItems_id()));
                insert.setString(2, Integer.toString(value.getFrom_city_id()));
                insert.setString(3, Integer.toString(value.getTo_city_id()));
                insert.setString(4, Integer.toString(value.getTier_id()));
                insert.setString(5, Double.toString(value.getValue()));
                insert.setString(6, value.getLast_updated());
                insert.executeUpdate();
            } catch (SQLException e) {
                System.out.println("SQL Statement: "+ insert +"\nError: \n"+ e);
            }
        }

        // 7. Daten speichern in value_order tabelle
        for (Value_Order value_order : value_orders_list){
            PreparedStatement insert = null;
            try {
                insert = conn.prepareStatement("INSERT INTO Value_Order (value_id, sort_order, last_updated) VALUES (?, ?, ?);");
                insert.setString(1, Integer.toString(value_order.getValue_id()));
                insert.setString(2, Integer.toString(value_order.getSort_order()));
                insert.setString(3, value_order.getLast_updated());
                insert.executeUpdate();
            } catch (SQLException e) {
                System.out.println("SQL Statement: "+ insert +"\nError: \n"+ e);
            }
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
            if (city.getTech_name().equals(tech_name)){
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
}