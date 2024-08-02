// View 1
/*const view1 = document.querySelector(".one__view");
const view1OpenBtn = document.querySelector(".one__view__openBtn");
const view1CloseBtn = document.querySelector(".one__view__closeBtn");
const view1Overlay = document.querySelector(".one__view-overlay");

view1OpenBtn.addEventListener("click", function () {
  view1.classList.add("one__showview");
  view1Overlay.classList.add("one__transparentBcg");
  html.classList.add("no-scroll");
});

view1CloseBtn.addEventListener("click", function () {
  view1.classList.remove("one__showview");
  view1Overlay.classList.remove("one__transparentBcg");
  html.classList.remove("no-scroll");
}); */

// Order 1
const order = document.querySelector(".order");
const orderOpenBtn = document.querySelectorAll(".order__openBtn");
const orderCloseBtn = document.querySelector(".order__closeBtn");
const orderOverlay = document.querySelector(".order-overlay");
const orderContent = document.querySelector(".order-content");

orderOpenBtn.forEach(btn => {
  btn.addEventListener("click", function () {
    const tableId = this.getAttribute("data-table-id");

    fetch(`/tables/order/${tableId}`)
        .then(response => response.json())
        .then(data => {
          orderContent.innerHTML = `
          <h2>Order Details for Table: ${data.tableFood.name}</h2>
          <ul>
            ${data.orders.map(order => `
              <li>
                <h3>Order Time: ${order.orderTime}</h3>
                <p>Customer Name: ${order.customerName}</p>
                <p>Email: ${order.email}</p>
                <p>Phone Number: ${order.phoneNumber}</p>
                <p>Number of People: ${order.numPeople}</p>
                <p>Note: ${order.note}</p>
              </li>
            `).join('')}
          </ul>
        `;
          order.classList.add("showview");
          orderOverlay.classList.add("transparentBcg");
          html.classList.add("no-scroll");
        })
        .catch(error => console.error('Error:', error));
  });
});

orderCloseBtn.addEventListener("click", function () {
  order.classList.remove("showview");
  orderOverlay.classList.remove("transparentBcg");
  html.classList.remove("no-scroll");
});
