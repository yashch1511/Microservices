import React from "react";
import { useNavigate } from "react-router-dom";

function Navbar() {
  const navigate = useNavigate();

  const token = localStorage.getItem("token");
  const role = localStorage.getItem("role");

  const handleAuth = () => {
    if (token) {
      localStorage.clear();
      navigate("/");
      window.location.reload();
    } else {
      navigate("/");
    }
  };

  return (
    <div style={styles.navbar}>
      <div style={styles.left}>
        <button onClick={() => navigate("/products")} style={styles.link}>
          Products
        </button>

        {token && (
          <button onClick={() => navigate("/orders")} style={styles.link}>
            My Orders
          </button>
        )}

        {/* ✅ ADMIN ONLY */}
        {role === "ADMIN" && (
          <button onClick={() => navigate("/add-product")} style={styles.link}>
            Add Product
          </button>
        )}
      </div>

      <button
        onClick={handleAuth}
        style={{
          ...styles.auth,
          backgroundColor: token ? "#e53935" : "#4CAF50",
        }}
      >
        {token ? "Logout" : "Login"}
      </button>
    </div>
  );
}

const styles = {
  navbar: {
    display: "flex",
    justifyContent: "space-between",
    padding: "15px 30px",
    background: "#1f2937",
  },
  left: {
    display: "flex",
    gap: "15px",
  },
  link: {
    background: "transparent",
    border: "none",
    color: "#fff",
    cursor: "pointer",
  },
  auth: {
    padding: "8px 15px",
    borderRadius: "6px",
    color: "#fff",
    border: "none",
    cursor: "pointer",
  },
};

export default Navbar;