
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

   /* const item_str = [
        "T4_BAG"//,T5_BAG,T6_BAG,T7_BAG,T8_BAG"
    ];

    const location_str = [
        "caerleon"//,bridgewatch"
    ]

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
      });*/
  




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
      let caerleon = [];
      let other = [];
      const profitTable = document.getElementById("profitTable");
      const tbody = document.querySelector("#profitTable tbody");

      tbody.innerHTML = '';
            
      Object.entries(myBigDictionary ).forEach(([key, val]) => {
        if (index<20){
          stringBuilder1 += String(tiersValue) + String(val) + ",";
        }
        if (index<40 && index>20){
          stringBuilder2 += String(tiersValue) + String(val) + ",";
        }
        if (index<60 && index>40){
          stringBuilder3 += String(tiersValue) + String(val) + ",";
        }
        if (index<80 && index>60){
          stringBuilder4 += String(tiersValue) + String(val) + ",";
        }    
        if (index<100 && index>80){
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
      
      urls = [
        `https://europe.albion-online-data.com/api/v2/stats/prices/${stringBuilder1}.json?locations=${cityValue},caerleon&qualities=1`,
        `https://europe.albion-online-data.com/api/v2/stats/prices/${stringBuilder2}.json?locations=${cityValue},caerleon&qualities=1` ,
        `https://europe.albion-online-data.com/api/v2/stats/prices/${stringBuilder3}.json?locations=${cityValue},caerleon&qualities=1` ,
        `https://europe.albion-online-data.com/api/v2/stats/prices/${stringBuilder4}.json?locations=${cityValue},caerleon&qualities=1` ,
        `https://europe.albion-online-data.com/api/v2/stats/prices/${stringBuilder5}.json?locations=${cityValue},caerleon&qualities=1` 
      ];
      Promise.all(
        urls.map(url =>
          fetch(url)
            .then(res => {
              if (!res.ok) throw new Error(`Failed to fetch ${url}`);
              return res.json();
            })
        )
      )
      .then(allData => {
        console.log(allData.length);
        allData.forEach((data,index)=>{
          if(data[index].city == "Caerleon"){
            caerleon.push(data);
          }else{
            other.push(data);
          }
        });

        other.forEach((otherItem,index)=>{          
          caerleon.forEach((caerItem,caerIndex)=> {
            //if(otherItem[index].item_id == caerItem[caerIndex].item_id){

              const row = document.createElement("tr");

              const itemCell = document.createElement("td");
              itemCell.textContent = otherItem[index].item_id;
                        
              const nameCell = document.createElement("td");
              nameCell.textContent = tiersValue;
          
              const cityCell = document.createElement("td");
              cityCell.textContent = cityValue;

              const profitCell = document.createElement("td");
              profitCell.textContent = caerItem[caerIndex].sell_price_min - otherItem[index].buy_price_min;

              const minBuy = document.createElement("td");
              minBuy.textContent = otherItem[index].buy_price_min;

              const maxSell = document.createElement("td");
              maxSell.textContent = caerItem[caerIndex].sell_price_min;

              row.appendChild(itemCell);
              row.appendChild(nameCell);
              row.appendChild(cityCell);
              row.appendChild(profitCell);
              row.appendChild(minBuy);
              row.appendChild(maxSell);

              tbody.appendChild(row);
            //  return;
           // }
          });
        });


        //listProfit.textContent = data1[0].city;
    });
  });
}