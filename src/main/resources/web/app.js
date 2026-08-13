let token = null;
let createdOrderId = null;

async function postJson(url, body, includeAuth) {
    const headers = { "Content-Type": "application/json" };
    if (includeAuth) {
        headers["Authorization"] = "Bearer " + token;
    }
    return fetch(url, { method: "POST", headers, body: JSON.stringify(body) });
}

document.getElementById("loginButton").addEventListener("click", async () => {
    const response = await postJson("/api/login", {
        email: document.getElementById("email").value,
        password: document.getElementById("password").value
    }, false);

    if (!response.ok) {
        document.getElementById("loginError").innerText = "Invalid credentials";
        return;
    }

    const body = await response.json();
    token = body.token;

    document.getElementById("loginError").innerText = "";
    document.getElementById("orderSection").hidden = false;
});

document.getElementById("createOrderButton").addEventListener("click", async () => {
    const response = await postJson("/api/orders", {
        productId: document.getElementById("productId").value,
        productName: document.getElementById("productName").value,
        quantity: Number(document.getElementById("quantity").value),
        unitPrice: Number(document.getElementById("unitPrice").value)
    }, true);

    if (!response.ok) {
        return;
    }

    const body = await response.json();
    createdOrderId = body.id;

    document.getElementById("orderId").innerText = body.id;
    document.getElementById("paymentSection").hidden = false;
});

document.getElementById("payButton").addEventListener("click", async () => {
    const response = await postJson("/api/payments", {
        orderId: createdOrderId,
        cardNumber: document.getElementById("cardNumber").value
    }, true);

    const body = await response.json();

    if (!response.ok) {
        document.getElementById("paymentError").innerText = body.message || "Payment failed";
        return;
    }

    document.getElementById("confirmationSection").hidden = false;
    document.getElementById("confirmationMessage").innerText = "Order confirmed successfully";
    document.getElementById("confirmationOrderId").innerText = createdOrderId;
    document.getElementById("orderStatus").innerText = "CONFIRMED";
});
