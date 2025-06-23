from flask import Flask, jsonify
from flask_cors import CORS
import sqlite3

app = Flask(__name__)
CORS(app)

@app.route("/Albion_Calc_Data")
def get_data():
    conn = sqlite3.connect("Albion_Calc_Data.db")
    cursor = conn.cursor()
    cursor.execute("""select
        (select tech_name from Tiers where id = tier_id) as Tier,
        (select display_name from Items where id = items_id) as Item,
        (select display_name from Cities where id = from_city_id) as from_city,
        (select display_name from Cities where id = to_city_id) as to_city,
        value,
        buy_price,
        sell_price,
        last_updated,
        (select display_color from Enchantments where id = rarity_id) as Enchantment

        FROM [Values] order by value desc LIMIT 1000;""")
    rows = cursor.fetchall()
    conn.close()
    return jsonify(rows)

if __name__=="__main__":
    app.run(debug=True, port = 5000)