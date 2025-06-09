fetch("http://localhost:5000/Albion_Calc_Data")
  .then(response => response.json())
  .then(data => {
    const table = document.getElementById("dataTable");

    data.forEach(row => {
      const tr = document.createElement("tr");

      row.forEach(cell => {
        const td = document.createElement("td");
        td.textContent = cell;
        tr.appendChild(td);
      });

      table.appendChild(tr);
    });
  })
  .catch(error => console.error("Error fetching data:", error));