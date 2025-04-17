/*document.getElementById("fetchBtn").addEventListener("click", () => {
    const item_str = [
        "T4_BAG"//,T5_BAG,T6_BAG,T7_BAG,T8_BAG"
    ];

    const location_str = [
        "caerleon"//,bridgewatch"
    ]

    url = `https://europe.albion-online-data.com/api/v2/stats/prices/${item_str}.json?locations=${location_str}&qualities=1` ;
    //https://europe.albion-online-data.com/api/v2/stats/prices/T4_BAG,T5_BAG.json?locations=bridgewatch,caerleon&qualities=1
  
    fetch(url)
      .then(response => {
        if (!response.ok) throw new Error("Network response was not ok");        
        return response.json();        
      })

      .then(dataArray => {    
        return dataArray[0].item_id,dataArray[0].city,dataArray[0].buy_price_min;
      })

      .then(data => {
        document.getElementById("output").textContent = data;//JSON.stringify(data, null, 2);
      })

      .catch(error => {
        console.error("Fetch error:", error);
      });
  });*/
  document.addEventListener('DOMContentLoaded', () => {
    const main = document.getElementById("main");      
    setupButton('load_lookFor', 'pages/lookFor.html');
    setupButton('load_ToCaerleon', 'pages/toCaerleon.html');
    setupButton('load_mostProfit', 'pages/mostProfit.html');
    setupButton('load_twoCities', 'pages/twoCities.html');

    function setupButton(id, page) {
        const btn = document.getElementById(id);
        btn.addEventListener('click', () => {
          fetch(page)
            .then(res => res.text())
            .then(html => {
              main.innerHTML = html;
            });
        });
      }


});  