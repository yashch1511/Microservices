import React from "react";
import { useLocation, useNavigate } from "react-router-dom";

function OrderConfirmation() {
  const navigate = useNavigate();
  const location = useLocation();
  const order = location.state?.order;

  if (!order) {
    return (
      <div style={styles.container}>
        <div style={styles.card}>
          <h2 style={styles.title}>No order data found</h2>
          <button style={styles.button} onClick={() => navigate("/products")}>
            Back to Products
          </button>
        </div>
      </div>
    );
  }

  return (
    <div style={styles.container}>
      <div style={styles.card}>
        <h2 style={styles.title}>Order Placed Successfully</h2>

        <div style={styles.row}>
          <span style={styles.label}>Order ID</span>
          <span>{order.id}</span>
        </div>

        <div style={styles.row}>
          <span style={styles.label}>Product ID</span>
          <span>{order.productId}</span>
        </div>

        <div style={styles.row}>
          <span style={styles.label}>Quantity</span>
          <span>{order.quantity}</span>
        </div>

        <div style={styles.row}>
          <span style={styles.label}>Total Price</span>
          <span>Rs {order.totalPrice ?? "N/A"}</span>
        </div>

        <div style={styles.row}>
          <span style={styles.label}>Status</span>
          <span>{order.status ?? "PLACED"}</span>
        </div>

        <div style={styles.actions}>
          <button style={styles.button} onClick={() => navigate("/orders")}>
            View My Orders
          </button>
          <button
            style={styles.secondaryButton}
            onClick={() => navigate("/products")}
          >
            Continue Shopping
          </button>
        </div>
      </div>
    </div>
  );
}

const styles = {
  container: {
    minHeight: "100vh",
    display: "flex",
    justifyContent: "center",
    alignItems: "center",
    backgroundColor: "#f5f7fa",
    padding: "20px",
  },
  card: {
    width: "100%",
    maxWidth: "460px",
    background: "#fff",
    borderRadius: "12px",
    padding: "24px",
    boxShadow: "0 6px 16px rgba(0,0,0,0.12)",
  },
  title: {
    textAlign: "center",
    marginBottom: "20px",
    color: "#1b5e20",
  },
  row: {
    display: "flex",
    justifyContent: "space-between",
    marginBottom: "12px",
  },
  label: {
    color: "#555",
    fontWeight: "600",
  },
  actions: {
    marginTop: "20px",
    display: "grid",
    gap: "10px",
  },
  button: {
    width: "100%",
    padding: "10px",
    border: "none",
    borderRadius: "8px",
    backgroundColor: "#2e7d32",
    color: "#fff",
    cursor: "pointer",
  },
  secondaryButton: {
    width: "100%",
    padding: "10px",
    border: "1px solid #ccc",
    borderRadius: "8px",
    backgroundColor: "#fff",
    color: "#333",
    cursor: "pointer",
  },
};

export default OrderConfirmation;
