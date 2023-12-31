/*let aux = document.location.href.split("?query=");
let query = aux[1].replace(/\%20/g, " ");
let inputValue = (document.getElementById("txtSearch").value = query);*/
let nextpagevisibility = document.getElementById("nextPage");
let prevpagevisibility = document.getElementById("prevPage");
let count = 0;
const itemsPerPage = 6;
const items = [];
let totalPage;
let itemsList;
let currentPage = 1;
const maxPageButtons = 5;

function performSearch(query, page) {
  const startTime = Date.now();
  fetch("http://localhost:8080/api/search?query=" + query)
    .then((response) => response.json())
    .then((json) => {
      let html = "";
      const endTime = Date.now();
      let performanceTime = endTime - startTime;
      let formatedTime = (performanceTime / 1000).toFixed(2) + "s";
      for (let resultSearch of json) {
        count++;
        items.push(getHtmlResultSearch(resultSearch));
      }
      document.getElementById(
        "count"
      ).outerHTML = `<span class="totalResult">Cerca de ${count} resultados (${formatedTime})</span>`;
      displayItems(page);
      updatePagination();
      nextpagevisibility.style.visibility = "visible";
      backpagevisibility.style.visibility = "visible";
    });
}
function getHtmlResultSearch(resultSearch) {
  return ` <div class="result">
    <h3><a href="${resultSearch.url}" target="_blank">${resultSearch.title}</a></h3>
    <span>${resultSearch.description}</span>
</div>`;
}
document.addEventListener("DOMContentLoaded", function () {
  document.addEventListener("keyup", function (event) {
    if (event.key === "Enter" && event.target.id === "txtSearch") {
      miFuncion();
    }
  });

  document
    .getElementById("searchIcon")
    .addEventListener("click", function (event) {
      miFuncion();
    });

  function miFuncion() {
    let inputQuery = document.getElementById("txtSearch");
    let query = inputQuery.value;
    if (/^\s*$/.test(query)) {
      return;
    }
    document.location.href = "results.html?query=" + encodeURIComponent(query);
  }
});

//Pagination
function displayItems(pageNumber) {
  startIndex = (pageNumber - 1) * itemsPerPage;
  endIndex = startIndex + itemsPerPage;
  const itemsToDisplay = items.slice(startIndex, endIndex);

  itemsList = document.getElementById("items-list");
  itemsList.innerHTML = "";
  if (!items.length) {
    const noResults = ` <div class="sugerenciasList">
    <p> No se han encontrado resultados para tu búsqueda:</p>
    <span>${"(" + inputValue + ")"}</span>
    <p >Sugerencias:</p>
    <ul >
    <li>Asegúrate de que todas las palabras estén escritas correctamente.</li>
    <li>Prueba diferentes palabras clave.</li>
    <li>Prueba palabras clave más generales.</li>
    </ul>
</div>`;
    itemsList.innerHTML = noResults;
  }
  itemsToDisplay.forEach((item) => {
    const li = document.createElement("li");
    li.innerHTML = item;
    itemsList.appendChild(li);
  });
}

//pagination buttons and call
function updatePagination() {
  totalPage = Math.ceil(count / itemsPerPage);

  const pagination = document.getElementById("pagination");
  pagination.innerHTML = "";

  let startRange = currentPage - Math.floor(maxPageButtons / 2);
  let endRange = currentPage + Math.floor(maxPageButtons / 2);

  if (startRange < 1) {
    startRange = 1;
    endRange = Math.min(maxPageButtons, totalPage);
  }
  if (endRange > totalPage) {
    endRange = totalPage;
    startRange = Math.max(1, totalPage - maxPageButtons + 1);
  }

  for (let i = startRange; i <= endRange; i++) {
    const button = document.createElement("button");
    button.textContent = i;
    button.type = "button";
    if (i === currentPage) {
      button.classList.add("page-current");
    }
    button.addEventListener("click", () => {
      const currentPageRemove = document.querySelector(".page-current");
      if (currentPageRemove) {
        currentPageRemove.classList.remove("page-current");
      }
      currentPage = i;
      displayItems(i);
      button.classList.add("page-current");
    });
    pagination.appendChild(button);
  }
}
// Obtén referencias a los botones de navegación
const prevPageButton = document.getElementById("prevPage");
const nextPageButton = document.getElementById("nextPage");

// Agrega event listeners para los botones de navegación
prevPageButton.addEventListener("click", () => navigatePage(-1));
nextPageButton.addEventListener("click", () => navigatePage(1));

// Función para navegar entre páginas
function navigatePage(direction) {
  const newPage = currentPage + direction;

  if (newPage >= 1 && newPage <= totalPage) {
    const currentPageRemove = document.querySelector(".page-current");
    if (currentPageRemove) {
      currentPageRemove.classList.remove("page-current");
    }
    currentPage = newPage;
    displayItems(newPage);
    updatePagination();
    updateNavigationButtons();
  }
}

// Actualiza la visibilidad de los botones de navegación
function updateNavigationButtons() {
  prevPageButton.style.display = currentPage === 1 ? "none" : "flex";
  nextPageButton.style.display = currentPage === totalPage ? "none" : "flex";
}

// Llama a esta función después de actualizar la paginación
updateNavigationButtons();
performSearch(query, 1);
