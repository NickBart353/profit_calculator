
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
              loadCityToCaerleon();
            });
        });
      }

    const item_str = [
        "T4_BAG"//,T5_BAG,T6_BAG,T7_BAG,T8_BAG"
    ];

    const location_str = [
        "caerleon"//,bridgewatch"
    ]
OFF_SHIELD_HELL
    url = `https://europe.albion-online-data.com/api/v2/stats/prices/${item_str}.json?locations=${location_str}&qualities=1` ;
    //https://europe.albion-online-data.com/api/v2/stats/prices/T4_OFF_SHIELD_HELL.json?locations=bridgewatch,caerleon&qualities=1
    const item = `https://render.albiononline.com/v1/item/${item}.png`;
  
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
  




}); 

function loadCityToCaerleon(){
  const generate = document.getElementById("generate");
  const listProfit = document.getElementById("listProfit");
  const cities = document.getElementById("cities");
  const tiers = document.getElementById("tiers");
  
  let cityValue = null;
  let tiersValue = null;  

    cities.addEventListener('change',function(){
      cityValue = cities.value;
    });
  
    tiers.addEventListener('change',function(){
      tiersValue = tiers.value;
    });
  
    generate.addEventListener('click', () => {
      let stringBuilder1 = "";
      let stringBuilder2 = "";
      let stringBuilder3 = "";
      let stringBuilder4 = "";
      let stringBuilder5 = "";
      let index = 0;
            
      Object.entries(myBigDictionary ).forEach(([key, val]) => {
        if (index<100){
          stringBuilder1 += String(tiersValue) + String(val) + ",";
        }
        if (index<200){
          stringBuilder2 += String(tiersValue) + String(val) + ",";
        }
        if (index<300){
          stringBuilder3 += String(tiersValue) + String(val) + ",";
        }
        if (index<400){
          stringBuilder4 += String(tiersValue) + String(val) + ",";
        }    
        if (index<500){
          stringBuilder5 += String(tiersValue) + String(val) + ",";
        }        
          index++;
      });

      if (stringBuilder1.endsWith(",")) {
        stringBuilder1 = stringBuilder1.slice(0, -1); 
      }
      if (stringBuilder2.endsWith(",")) {
        stringBuilder2 = stringBuilder2.slice(0, -1); 
      }
      if (stringBuilder3.endsWith(",")) {
        stringBuilder3 = stringBuilder3.slice(0, -1); 
      }
      if (stringBuilder4.endsWith(",")) {
        stringBuilder4 = stringBuilder4.slice(0, -1); 
      }
      if (stringBuilder5.endsWith(",")) {
        stringBuilder5 = stringBuilder5.slice(0, -1); 
      }

      url1 = `https://europe.albion-online-data.com/api/v2/stats/prices/${stringBuilder1}.json?locations=${cityValue},caerleon&qualities=1` ;
      url2 = `https://europe.albion-online-data.com/api/v2/stats/prices/${stringBuilder2}.json?locations=${cityValue},caerleon&qualities=1` ;
      url3 = `https://europe.albion-online-data.com/api/v2/stats/prices/${stringBuilder3}.json?locations=${cityValue},caerleon&qualities=1` ;
      url4 = `https://europe.albion-online-data.com/api/v2/stats/prices/${stringBuilder4}.json?locations=${cityValue},caerleon&qualities=1` ;
      url5 = `https://europe.albion-online-data.com/api/v2/stats/prices/${stringBuilder5}.json?locations=${cityValue},caerleon&qualities=1` ;

      listProfit.textContent = url;
    });
  
}