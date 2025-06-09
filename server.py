from flask import Flask, jsonify
from flask_cors import CORS
import sqlite3

app = Flask(__name__)
CORS(app)

@app.route("/Albion_Calc_Data")
def get_data():
    conn = sqlite3.connect("Albion_Calc_Data.db")
    cursor = conn.cursor()
    cursor.execute("SELECT * FROM [Values] ORDER BY value DESC LIMIT 1000")
    rows = cursor.fetchall()
    conn.close()
    return jsonify(rows)

if __name__=="__main__":
    app.run(debug=True, port = 5000)