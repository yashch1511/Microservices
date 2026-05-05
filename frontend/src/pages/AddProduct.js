import React, { useState } from "react";
import API from "../services/api";
import { useNavigate } from "react-router-dom";

function AddProduct() {
  const navigate = useNavigate();

  const [product, setProduct] = useState({
    name: "",
    description: "",
    price: "",
    quantity: "",
  });

  const handleSubmit = async () => {
    try {
      await API.post("/products", product);
      alert("Product added!");
      navigate("/products");
    } catch {
      alert("Only admin allowed!");
    }
  };

  return (
    <div style={styles.container}>
      <div style={styles.card}>
        <h2>Add Product</h2>

        <input
          placeholder="Name"
          style={styles.input}
          onChange={(e) =>
            setProduct({ ...product, name: e.target.value })
          }
        />
        <input
          placeholder="Description"
          style={styles.input}
          onChange={(e) =>
            setProduct({ ...product, description: e.target.value })
          }
        />
        <input
          type="number"
          placeholder="Price"
          style={styles.input}
          onChange={(e) =>
            setProduct({ ...product, price: e.target.value })
          }
        />
        <input
          type="number"
          placeholder="Quantity"
          style={styles.input}
          onChange={(e) =>
            setProduct({ ...product, quantity: e.target.value })
          }
        />

        <button style={styles.button} onClick={handleSubmit}>
          Add Product
        </button>
      </div>
    </div>
  );
}

const styles = {
  container: {
    display: "flex",
    justifyContent: "center",
    marginTop: "50px",
  },
  card: {
    width: "350px",
    padding: "20px",
    background: "#fff",
    borderRadius: "10px",
    boxShadow: "0 4px 12px rgba(0,0,0,0.1)",
  },
  input: {
    width: "100%",
    marginBottom: "10px",
    padding: "10px",
  },
  button: {
    width: "100%",
    padding: "10px",
    background: "#4CAF50",
    color: "#fff",
    border: "none",
    borderRadius: "6px",
  },
};

export default AddProduct;