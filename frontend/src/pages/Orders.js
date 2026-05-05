import React, { useEffect, useState } from "react";
import API from "../services/api";

function Orders() {
  const [orders, setOrders] = useState([]);

  useEffect(() => {
    API.get("/orders")
      .then((res) => setOrders(res.data))
      .catch(() => alert("Error fetching orders"));
  }, []);

  return (
    <div style={styles.container}>
      <h2 style={styles.heading}>🧾 My Orders</h2>

      {orders.length === 0 ? (
        <p style={styles.empty}>No orders yet.</p>
      ) : (
        <div style={styles.grid}>
          {orders.map((o) => (
            <div key={o.id} style={styles.card}>
              <div style={styles.row}>
                <span style={styles.label}>Order ID</span>
                <span>{o.id}</span>
              </div>

              <div style={styles.row}>
                <span style={styles.label}>Product</span>
                <span>#{o.productId}</span>
              </div>

              <div style={styles.row}>
                <span style={styles.label}>Quantity</span>
                <span>{o.quantity}</span>
              </div>

              <div style={styles.row}>
                <span style={styles.label}>Total</span>
                <span style={styles.price}>₹{o.totalPrice}</span>
              </div>

              <div style={styles.statusContainer}>
                <span
                  style={{
                    ...styles.status,
                    backgroundColor:
                      o.status === "PLACED"
                        ? "#4CAF50"
                        : o.status === "PENDING"
                        ? "#ff9800"
                        : "#f44336",
                  }}
                >
                  {o.status}
                </span>
              </div>
            </div>
          ))}
        </div>
      )}
    </div>
  );
}

const styles = {
  container: {
    padding: "30px",
    backgroundColor: "#f5f7fa",
    minHeight: "100vh",
  },
  heading: {
    marginBottom: "20px",
    textAlign: "center",
  },
  empty: {
    textAlign: "center",
    color: "#777",
  },
  grid: {
    display: "grid",
    gridTemplateColumns: "repeat(auto-fill, minmax(280px, 1fr))",
    gap: "20px",
  },
  card: {
    background: "#fff",
    padding: "20px",
    borderRadius: "12px",
    boxShadow: "0 4px 12px rgba(0,0,0,0.08)",
    transition: "0.3s",
  },
  row: {
    display: "flex",
    justifyContent: "space-between",
    marginBottom: "10px",
  },
  label: {
    color: "#555",
    fontWeight: "500",
  },
  price: {
    fontWeight: "bold",
    color: "#2e7d32",
  },
  statusContainer: {
    marginTop: "15px",
    textAlign: "right",
  },
  status: {
    padding: "5px 12px",
    borderRadius: "20px",
    color: "#fff",
    fontSize: "12px",
  },
};

export default Orders;