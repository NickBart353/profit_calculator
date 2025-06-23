document.getElementById("applyButton").addEventListener("click", applyBoxes);

function applyBoxes(){

  fetch("http://localhost:5000/Albion_Calc_Data")
    .then(response => response.json())
    .then(data => {
      
      const tbody = document.querySelector("#dataTable tbody");
      tbody.innerHTML = "";

        const from_dict = {
          "Brecilien" : document.getElementById("Brecilien0").checked,
          "Bridgewatch" : document.getElementById("Bridgewatch0").checked,
          "Caerleon" : document.getElementById("Caerleon0").checked,
          "Fort Sterling" : document.getElementById("Fort Sterling0").checked,
          "Lymhurst" : document.getElementById("Lymhurst0").checked,
          "Martlock" : document.getElementById("Martlock0").checked,
          "Thetford" : document.getElementById("Thetford0").checked,
        }
        const to_dict = {
          "Brecilien" : document.getElementById("Brecilien1").checked,
          "Bridgewatch" : document.getElementById("Bridgewatch1").checked,
          "Caerleon" : document.getElementById("Caerleon1").checked,
          "Sterling" : document.getElementById("Fort Sterling1").checked,
          "Lymhurst" : document.getElementById("Lymhurst1").checked,
          "Martlock" : document.getElementById("Martlock1").checked,
          "Thetford" : document.getElementById("Thetford1").checked
        };

        var from_box = [];
        var to_box = [];

        for (const [key, value] of Object.entries(from_dict)){
          if (value){ 
            from_box.push(key);      
          }
        }
        for (const [key, value] of Object.entries(to_dict)){
          if (value){ 
            to_box.push(key);      
          }
        }

      data.forEach(row => {
        const tableRow = document.createElement("tr");
        let allow = true;

        from_box.forEach ((city) => {
          if (from_box.includes(row[2]) || to_box.includes(row[3])) {
            allow = false;
          }
        });

        if (allow){     
          console.log(allow, row[2])
          
          row.forEach((cell, index) => {
            const isLast = index === row.length - 1;
            const tableD = document.createElement("td");
            tableD.textContent = cell;
            
            switch(row[8]){
              case "Green":
                tableD.style.backgroundColor = "rgb(90, 120, 90)";
                break;
              case "Yellow":
                tableD.style.backgroundColor = "rgb(140, 130, 70)";
                break;
              case "Purple":
                tableD.style.backgroundColor = "rgb(110, 90, 130)";
                break;
              case "Blue":
                tableD.style.backgroundColor = "rgb(85, 115, 140)";
                break;
              default:
                tableD.style.backgroundColor = "rgb(120, 120, 120)";
                break;
            }
            if (!isLast) {
              tableRow.appendChild(tableD);
            }
          });

          tbody.appendChild(tableRow);
        }
      });
    })
    .catch(error => console.error("Error fetching data:", error)); 
}


fetch("http://localhost:5000/Albion_Calc_Data")
  .then(response => response.json())
  .then(data => {

      const tbody = document.querySelector("#dataTable tbody");
      tbody.innerHTML = "";

    data.forEach(row => {
      const tableRow = document.createElement("tr");      

      row.forEach((cell, index) => {
        const isLast = index === row.length - 1;
        const tableD = document.createElement("td");
        tableD.textContent = cell;
        
        switch(row[8]){
          case "Green":
            tableD.style.backgroundColor = "rgb(90, 120, 90)";
            break;
          case "Yellow":
            tableD.style.backgroundColor = "rgb(140, 130, 70)";
            break;
          case "Purple":
            tableD.style.backgroundColor = "rgb(110, 90, 130)";
            break;
          case "Blue":
            tableD.style.backgroundColor = "rgb(85, 115, 140)";
            break;
          default:
            tableD.style.backgroundColor = "rgb(120, 120, 120)";
            break;
        }      
        if (!isLast) {
          tableRow.appendChild(tableD);
        }  
        
        
      });

      tbody.appendChild(tableRow);
    });
  })
  .catch(error => console.error("Error fetching data:", error));


