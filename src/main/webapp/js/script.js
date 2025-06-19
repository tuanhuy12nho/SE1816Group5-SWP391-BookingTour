
document.getElementById('next').onclick = function(){
    let lists = document.querySelectorAll('.item');
    document.getElementById('slide').appendChild(lists[0]);
}
document.getElementById('prev').onclick = function(){
    let lists = document.querySelectorAll('.item');
    document.getElementById('slide').prepend(lists[lists.length - 1]);
}
function toggleMap() {
    let map = document.querySelector(".map");
    map.classList.toggle("active"); 
}
function toggleMap() {
    let map = document.querySelector(".map");
    map.classList.toggle("active"); // Thêm hoặc xóa class "active"
}
document.addEventListener("DOMContentLoaded", function () {
    let map = document.querySelector(".map");

    map.addEventListener("click", function (event) {
        if (!map.classList.contains("active")) {
            map.classList.add("active"); // Phóng to bản đồ
            event.stopPropagation(); // Ngăn chặn sự kiện click lan ra ngoài
        }
    });

    // Khi click ra ngoài bản đồ, thu nhỏ lại
    document.addEventListener("click", function (event) {
        if (map.classList.contains("active") && !map.contains(event.target)) {
            map.classList.remove("active");
        }
    });
});

  
    function sortToursByPrice(ascending) {
        let container = document.querySelector(".trip-container");
        let items = Array.from(container.children);

        // Lấy giá từ HTML và chuyển thành số để sắp xếp
        items.sort((a, b) => {
            let priceA = parseFloat(a.querySelector(".price-date strong").innerText.replace(" VND", "").replace(",", ""));
            let priceB = parseFloat(b.querySelector(".price-date strong").innerText.replace(" VND", "").replace(",", ""));
            return ascending ? priceA - priceB : priceB - priceA; // Sắp xếp tăng hoặc giảm
        });

        // Xóa danh sách cũ và cập nhật danh sách mới
        container.innerHTML = "";
        items.forEach(item => container.appendChild(item));
    }
let originalTours = []; // Lưu danh sách tour gốc

document.addEventListener("DOMContentLoaded", function () {
    let tripContainer = document.querySelector(".trip-container");
    originalTours = Array.from(tripContainer.children); // Lưu lại danh sách ban đầu
});


function searchTours() {
    let input = document.getElementById("searchInput").value.toLowerCase();
    let tours = document.getElementsByClassName("trip-info");

    for (let i = 0; i < tours.length; i++) {
        let name = tours[i].querySelector("h1 a").textContent.toLowerCase();
        let location = tours[i].querySelector("p b").textContent.toLowerCase();

        if (name.includes(input) || location.includes(input)) {
            tours[i].style.display = "flex";
        } else {
            tours[i].style.display = "none";
        }
    }
}

function resetSearch() {
    document.getElementById("searchInput").value = "";
    searchTours();
}



