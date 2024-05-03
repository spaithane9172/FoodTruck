function handleRating(n) {
  for (let i = 1; i <= 5; i++) {
    if (n >= i) {
      document.getElementById("star" + i).classList.remove("fa-regular");
      document.getElementById("star" + i).classList.add("fa-solid");
    } else {
      document.getElementById("star" + i).classList.remove("fa-solid");
      document.getElementById("star" + i).classList.add("fa-regular");
    }
  }
  document.getElementById("rating").value = n;
}

function handleFoodtruckMenu(div) {
  if (div == "menu") {
    document.getElementById("menu").classList.remove("hidden");
    document.getElementById("menuBtn").classList.contains("menuDesign")
      ? ""
      : document.getElementById("menuBtn").classList.add("menuDesign");

    document.getElementById("gallery").classList.contains("hidden")
      ? ""
      : document.getElementById("gallery").classList.add("hidden");

    document.getElementById("galleryBtn").classList.contains("menuDesign")
      ? document.getElementById("galleryBtn").classList.remove("menuDesign")
      : "";

    document.getElementById("feedback").classList.contains("hidden")
      ? ""
      : document.getElementById("feedback").classList.add("hidden");
    document.getElementById("feedbackBtn").classList.contains("menuDesign")
      ? document.getElementById("feedbackBtn").classList.remove("menuDesign")
      : "";
  }
  if (div == "gallery") {
    document.getElementById("gallery").classList.remove("hidden");
    document.getElementById("galleryBtn").classList.contains("menuDesign")
      ? ""
      : document.getElementById("galleryBtn").classList.add("menuDesign");

    document.getElementById("menu").classList.contains("hidden")
      ? ""
      : document.getElementById("menu").classList.add("hidden");

    document.getElementById("menuBtn").classList.contains("menuDesign")
      ? document.getElementById("menuBtn").classList.remove("menuDesign")
      : "";

    document.getElementById("feedback").classList.contains("hidden")
      ? ""
      : document.getElementById("feedback").classList.add("hidden");

    document.getElementById("feedbackBtn").classList.contains("menuDesign")
      ? document.getElementById("feedbackBtn").classList.remove("menuDesign")
      : "";
  }
  if (div == "feedback") {
    document.getElementById("feedback").classList.remove("hidden");
    document.getElementById("feedbackBtn").classList.contains("menuDesign")
      ? ""
      : document.getElementById("feedbackBtn").classList.add("menuDesign");

    document.getElementById("menu").classList.contains("hidden")
      ? ""
      : document.getElementById("menu").classList.add("hidden");
    document.getElementById("menuBtn").classList.contains("menuDesign")
      ? document.getElementById("menuBtn").classList.remove("menuDesign")
      : "";

    document.getElementById("gallery").classList.contains("hidden")
      ? ""
      : document.getElementById("gallery").classList.add("hidden");
    document.getElementById("galleryBtn").classList.contains("menuDesign")
      ? document.getElementById("galleryBtn").classList.remove("menuDesign")
      : "";
  }
}

function handleFilterBox() {
  element = document.getElementById("filterBox");
  element.classList.contains("hidden")
    ? element.classList.remove("hidden")
    : element.classList.add("hidden");
}

function showCoverPhotoBtn() {
  document.getElementById("coverPhoto").classList.remove("hidden");
}
function closeCoverPhotoBtn() {
  document.getElementById("coverPhoto").classList.add("hidden");
}
function coverPhotoBtn() {
  element = document.getElementById("coverPhotoChange");
  element.classList.contains("hidden")
    ? element.classList.remove("hidden")
    : element.classList.add("hidden");
}

function handleAddMenuItem() {
  element = document.getElementById("addMenuItemBox");
  element.classList.contains("hidden")
    ? element.classList.remove("hidden")
    : element.classList.add("hidden");
}
function closeImg() {
  document.getElementById("mainDiv").classList.remove("overflow-hidden");
  document.getElementById("imgDiv").classList.add("hidden");
}
function showImg(n) {
  document.getElementById("mainDiv").classList.add("overflow-hidden");
  document.getElementById("imgDiv").classList.remove("hidden");

  document.getElementById("BigImg").src =
    "data:image/jpeg;base64," + document.getElementById("img" + n).textContent;
}
