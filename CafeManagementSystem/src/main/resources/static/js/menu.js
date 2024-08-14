// // View 1
// const view1 = document.querySelector(".one__view");
// const view1OpenBtn = document.querySelector(".one__view__openBtn");
// const view1CloseBtn = document.querySelector(".one__view__closeBtn");
// const view1Overlay = document.querySelector(".one__view-overlay");
//
// view1OpenBtn.addEventListener("click", function () {
//   view1.classList.add("one__showview");
//   view1Overlay.classList.add("one__transparentBcg");
//   html.classList.add("no-scroll");
// });
//
// view1CloseBtn.addEventListener("click", function () {
//   view1.classList.remove("one__showview");
//   view1Overlay.classList.remove("one__transparentBcg");
//   html.classList.remove("no-scroll");
// });

// Order 1
const order = document.querySelector(".order");
const orderOpenBtns = document.querySelectorAll(".order__openBtn");
const orderCloseBtn = document.querySelector(".order__closeBtn");
const orderOverlay = document.querySelector(".order-overlay");
const orderContent = document.querySelector(".order-content");

orderOpenBtns.forEach(btn => {
    btn.addEventListener("click", function() {
        const tableId = this.getAttribute("data-table-id");
        console.log("Fetching order details for table ID:", tableId);

        fetch(`/tables/order/${tableId}`)
            .then(response => {
                if (!response.ok) {
                    throw new Error(`Network response was not ok (${response.statusText})`);
                }
                return response.json();
            })
            .then(data => {
                console.log("Data received:", data);
                const orderDetails = data.orders.map(order => `
                  <div class="one__order">
                      <div class="one__order__closeBtn">
                          <i class="fas fa-window-close"></i>
                      </div>
                      <div class="one__view-content">
                          <h2>
                              <i class="fas fa-mug-hot"></i>
                              <span class="span-primary">Order for</span> ${order.customerName}
                              <i class="fas fa-mug-hot"></i>
                          </h2>
                          <div class="description">
                              Order placed at ${order.orderTime} for ${order.numPeople} people.
                              <br />
                              Note: ${order.note || 'No additional notes.'}
                          </div>
                          <img src="../../static/img/caffe-americano.jpg" alt="Order Image" />
                          <div class="nutritional_info">
                              <h2><span class="span-primary">Contact</span> Information</h2>
                              <div class="bar-medium">
                                  <b>Email</b> ${order.email}
                              </div>
                              <div class="bar-medium">
                                  <b>Phone Number</b> ${order.phoneNumber}
                              </div>
                          </div>
                      </div>
                  </div>
                `).join('');

                orderContent.innerHTML = `
                  <h2>Order Details for Table: ${data.tableFood.name}</h2>
                  ${orderDetails}
                `;
                orderOverlay.classList.add("show");
                html.classList.add("no-scroll");
            })
            .catch(error => {
                console.error('Error fetching data:', error);
                alert('Failed to fetch order details. Please check console for details.');
            });
    });
});

orderCloseBtn.addEventListener("click", function() {
    orderOverlay.classList.remove("show");
    html.classList.remove("no-scroll");
});
