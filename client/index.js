function search() {
  let textSearch = document.getElementById("searchInput").value;
  if (textSearch == "") {
    return;
  }
  document.location.href = "results.html?query=" + textSearch;
}
document.addEventListener("keyup", function (event) {
  if (event.key === "Enter" && event.target.id === "searchInput") {
    search();
  }
});
