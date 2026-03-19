const API_BASE_URL = "http://localhost:8080";

console.log("app.js loaded");

async function loadProducts() {
    console.log("loadProducts 실행");

    try {
        const response = await fetch(`${API_BASE_URL}/api/products`);
        console.log("products response status:", response.status);

        if (!response.ok) {
            throw new Error("상품 목록 조회 실패");
        }

        const products = await response.json();
        console.log("products data:", products);

        renderProducts(products);
    } catch (error) {
        console.error("상품 조회 에러:", error);
        alert("상품 목록을 불러오지 못했습니다.");
    }
}

function renderProducts(products) {
    const productList = document.getElementById("product-list");
    console.log("productList:", productList);

    if (!productList) {
        console.error("product-list 요소를 찾을 수 없습니다.");
        return;
    }

    productList.innerHTML = "";

    products.forEach((product) => {
        const li = document.createElement("li");
        li.className = "list-group-item d-flex mt-2";

        li.innerHTML = `
      <div class="col-2">
        <img class="img-fluid" src="${product.imageUrl}" alt="${product.name}">
      </div>
      <div class="col">
        <div class="row text-muted">${product.category}</div>
        <div class="row">${product.name}</div>
      </div>
      <div class="col text-center price">${product.price}원</div>
      <div class="col text-end action">
        <button type="button" class="btn btn-sm btn-outline-dark" onclick="addToCart(${product.id})">추가</button>
      </div>
    `;

        productList.appendChild(li);
    });
}

async function loadCart() {
    try {
        const response = await fetch(`${API_BASE_URL}/api/cart`);

        if (!response.ok) {
            throw new Error("장바구니 조회 실패");
        }

        const cart = await response.json();
        renderCart(cart);
    } catch (error) {
        console.error("장바구니 조회 에러:", error);
    }
}

function renderCart(cart) {
    const summaryList = document.getElementById("summary-list");
    const totalPrice = document.getElementById("total-price");

    if (!summaryList || !totalPrice) {
        console.error("summary-list 또는 total-price 요소를 찾을 수 없습니다.");
        return;
    }

    summaryList.innerHTML = "";

    if (!cart.items || cart.items.length === 0) {
        summaryList.innerHTML = `<div class="row"><h6 class="p-0">장바구니가 비어 있습니다.</h6></div>`;
        totalPrice.textContent = "0원";
        return;
    }

    cart.items.forEach((item) => {
        const row = document.createElement("div");
        row.className = "row mb-2";

        row.innerHTML = `
      <div class="d-flex justify-content-between align-items-center p-0">
        <h6 class="m-0">
          ${item.name} <span class="badge bg-dark">${item.quantity}개</span>
        </h6>
        <button type="button" class="btn btn-sm btn-outline-danger" onclick="removeFromCart(${item.productId})">삭제</button>
      </div>
    `;

        summaryList.appendChild(row);
    });

    totalPrice.textContent = `${cart.totalPrice}원`;
}

async function addToCart(productId) {
    try {
        const response = await fetch(`${API_BASE_URL}/api/cart/items`, {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify({
                productId: productId,
                quantity: 1
            })
        });

        if (!response.ok) {
            const errorText = await response.text();
            throw new Error(errorText || "장바구니 추가 실패");
        }

        await loadCart();
    } catch (error) {
        console.error("장바구니 추가 에러:", error);
        alert(error.message);
    }
}

async function removeFromCart(productId) {
    try {
        const response = await fetch(`${API_BASE_URL}/api/cart/items/${productId}`, {
            method: "DELETE"
        });

        if (!response.ok) {
            const errorText = await response.text();
            throw new Error(errorText || "장바구니 삭제 실패");
        }

        await loadCart();
    } catch (error) {
        console.error("장바구니 삭제 에러:", error);
        alert(error.message);
    }
}

async function order() {
    const email = document.getElementById("email").value.trim();
    const address = document.getElementById("address").value.trim();
    const postcode = document.getElementById("postcode").value.trim();

    try {
        const response = await fetch(`${API_BASE_URL}/api/orders`, {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify({
                email,
                address,
                postcode
            })
        });

        const data = await response.json();

        if (!response.ok) {
            throw new Error(data.message || "주문 실패");
        }

        alert(`${data.message}\n주문번호: ${data.orderId}\n총금액: ${data.totalPrice}원`);

        document.getElementById("email").value = "";
        document.getElementById("address").value = "";
        document.getElementById("postcode").value = "";

        await loadCart();
    } catch (error) {
        console.error("주문 에러:", error);
        alert(error.message);
    }
}


document.addEventListener("DOMContentLoaded", async () => {
    console.log("DOMContentLoaded");
    await loadProducts();
    await loadCart();
});