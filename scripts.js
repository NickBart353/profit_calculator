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
}); 

function loadCityToCaerleon(){
  const generate = document.getElementById("generate");
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
      //let stringBuilder = "";
      let caerleon = [];
      let other = [];
      let urls = [];
      const tbody = document.querySelector("#profitTable tbody");

      tbody.innerHTML = '';
            
      let index2 =0;

      Object.entries(myBigDictionary ).forEach(([key, val]) => {        
        const stringBuilder = String(tiersValue) + String(val);
        const url = `https://europe.albion-online-data.com/api/v2/stats/prices/${stringBuilder}.json?locations=${cityValue},caerleon&qualities=1`;
        urls.push(url);
        console.log(urls[index2]);
        index2++;
      });

      fetchWithDelay(urls)
      .then(allData => {
        try{
          allData.forEach((data,index)=>{
            if(data[index] && 'city' in data[index]){
              if(data[index].city == "Caerleon"){
                caerleon.push(data);
              }else{
                other.push(data);
              }
            }
          })
        }catch (error){
          console.log("ERROR!!"+error)
        }
      });

        other.forEach((otherItem,index)=>{          
          caerleon.forEach((caerItem,caerIndex)=> {
            if(otherItem[index].item_id == caerItem[caerIndex].item_id){

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
              return;
            }
          });
        });
        //listProfit.textContent = data1[0].city;
    });
}

function sleep(ms) {
  return new Promise(resolve => setTimeout(resolve, ms));
}

async function fetchWithDelay(urls) {
  const results = [];

  for (const url of urls) {
    try {
      console.log("fetching:\n"+url);
      const res = await fetch(url);
      if (!res.ok) throw new Error(`Failed to fetch ${url}`);
      const data = await res.json();
      results.push(data);
    } catch (error) {
      console.error(error);
    }
    await sleep(600); // wait 600ms before next request
  }

  return results;
}