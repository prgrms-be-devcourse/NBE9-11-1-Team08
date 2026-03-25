window.onload = getAllProducts;

function renderFormButtons(isEditing = false) {
    const primaryButtonText = isEditing ? "수정 완료" : "등록하기";
    const primaryButtonAction = isEditing ? "updateProduct()" : "createProduct()";
    const cancelButton = isEditing
        ? `<button class="btn btn-outline-secondary col-12 mb-2" type="button" onclick="resetForm()">취소</button>`
        : "";

    document.getElementById('button-area').innerHTML = `
        <button class="btn btn-dark col-12 mb-2" type="button" onclick="${primaryButtonAction}">${primaryButtonText}</button>
        ${cancelButton}`;
}

async function getAllProducts() {
    const response = await fetch('/products');
    const products = await response.json();
    const listBody = document.getElementById('product-list');
    listBody.innerHTML = '';

    let isFirst = true;

    products.forEach(p => {
        const imgPath = p.imageUrl && p.imageUrl !== "default.jpg" ? p.imageUrl : "https://i.imgur.com/HKOFQYa.jpeg";
        const mtClass = isFirst ? "mt-3" : "mt-2";
        isFirst = false;

        listBody.innerHTML += `
        <li class="list-group-item d-flex ${mtClass}">
          <div class="col-2"><img class="img-fluid" src="${imgPath}" alt=""></div>
          <div class="col">
            <div class="row text-muted">${p.category}</div>
            <div class="row">${p.name}</div>
          </div>
          <div class="col text-center price">${p.price.toLocaleString()}원</div>
          <div class="col text-end action">
            <button class="btn btn-small btn-outline-dark" onclick="prepareUpdate(${p.productId}, '${p.name}', ${p.price}, '${p.category}', '${p.imageUrl}')">수정</button>
            <button class="btn btn-small btn-outline-danger ms-1" onclick="deleteProduct(${p.productId})">삭제</button>
          </div>
        </li>`;
    });
}

function validateForm(name, price) {
    if (!name || name.trim() === "") {
        alert("상품명을 입력해주세요.");
        document.getElementById('name').focus();
        return false;
    }
    if (!price || price <= 0) {
        alert("가격을 1원 이상으로 입력해주세요.");
        document.getElementById('price').focus();
        return false;
    }
    return true;
}

async function createProduct() {
    const name = document.getElementById('name').value;
    const price = document.getElementById('price').value;
    let category = document.getElementById('category').value;
    let imageUrl = document.getElementById('imageUrl').value;

    if (!validateForm(name, price)) return;
    if (!category || category.trim() === "") category = "미분류";

    const data = {
        name: name,
        price: parseInt(price),
        category: category,
        imageUrl: imageUrl || "default.jpg"
    };

    const res = await fetch('/products', { method: 'POST', headers: { 'Content-Type': 'application/json' }, body: JSON.stringify(data) });
    if(res.ok) { resetForm(); getAllProducts(); }
}

function prepareUpdate(id, name, price, category, imageUrl) {
    document.getElementById('form-title').innerText = "상품 수정";
    document.getElementById('current-id').value = id;
    document.getElementById('name').value = name;
    document.getElementById('price').value = price;
    document.getElementById('category').value = category;
    document.getElementById('imageUrl').value = imageUrl === "default.jpg" ? "" : imageUrl;

    renderFormButtons(true);
}

async function updateProduct() {
    const id = document.getElementById('current-id').value;
    const name = document.getElementById('name').value;
    const price = document.getElementById('price').value;
    let category = document.getElementById('category').value;
    let imageUrl = document.getElementById('imageUrl').value;

    if (!validateForm(name, price)) return;
    if (!category || category.trim() === "") category = "미분류";

    const data = {
        name: name,
        price: parseInt(price),
        category: category,
        imageUrl: imageUrl || "default.jpg"
    };

    await fetch(`/products/${id}`, { method: 'PUT', headers: { 'Content-Type': 'application/json' }, body: JSON.stringify(data) });
    resetForm(); getAllProducts();
}

async function deleteProduct(id) {
    if(confirm('삭제하시겠습니까?')) {
        await fetch(`/products/${id}`, { method: 'DELETE' });
        getAllProducts();
    }
}

function resetForm() {
    document.getElementById('form-title').innerText = "상품 등록";
    document.getElementById('product-form').reset();
    renderFormButtons(false);
}
