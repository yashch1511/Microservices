import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import API from "../services/api";

function Products() {
  const navigate = useNavigate();
  const role = localStorage.getItem("role");
  const isAdmin = role === "ADMIN";

  const [products, setProducts] = useState([]);
  const [selectedQuantities, setSelectedQuantities] = useState({});
  const [editingProductId, setEditingProductId] = useState(null);
  const [editForm, setEditForm] = useState({
    name: "",
    description: "",
    price: "",
    quantity: "",
  });

  useEffect(() => {
    API.get("/products")
      .then((res) => setProducts(res.data))
      .catch(() => alert("Error fetching products"));
  }, []);

  const loadProducts = async () => {
    const res = await API.get("/products", {
      validateStatus: () => true,
    });
    return Array.isArray(res.data) ? res.data : [];
  };

  const handleQuantityChange = (productId, value, maxStock) => {
    const parsed = Number(value);
    if (Number.isNaN(parsed)) return;

    const clamped = Math.max(1, Math.min(parsed, maxStock));
    setSelectedQuantities((prev) => ({
      ...prev,
      [productId]: clamped,
    }));
  };

  const handleBuy = async (productId, maxStock) => {
    const quantityToBuy = selectedQuantities[productId] || 1;

    if (quantityToBuy < 1 || quantityToBuy > maxStock) {
      window.alert("Please choose a valid quantity");
      return;
    }

    try {
      const res = await API.post(
        "/orders",
        {
          productId,
          quantity: quantityToBuy,
        },
        {
          validateStatus: () => true,
        }
      );

      if (res.status >= 200 && res.status < 300) {
        const refreshedProducts = await loadProducts();
        setProducts(refreshedProducts);
        setSelectedQuantities((prev) => ({
          ...prev,
          [productId]: 1,
        }));

        navigate("/order-confirmation", {
          state: {
            order: res.data || {
              id: "N/A",
              productId,
              quantity: quantityToBuy,
              totalPrice: "N/A",
              status: "PLACED",
            },
          },
        });
        return;
      }

      window.alert("Error placing order");
    } catch {
      window.alert("Error placing order");
    }
  };

  const startEditing = (product) => {
    setEditingProductId(product.id);
    setEditForm({
      name: product.name ?? "",
      description: product.description ?? "",
      price: String(product.price ?? ""),
      quantity: String(product.quantity ?? ""),
    });
  };

  const cancelEditing = () => {
    setEditingProductId(null);
    setEditForm({ name: "", description: "", price: "", quantity: "" });
  };

  const saveProductChanges = async (productId) => {
    const payload = {
      name: editForm.name.trim(),
      description: editForm.description.trim(),
      price: Number(editForm.price),
      quantity: Number(editForm.quantity),
    };

    if (
      !payload.name ||
      Number.isNaN(payload.price) ||
      Number.isNaN(payload.quantity) ||
      payload.price < 0 ||
      payload.quantity < 0
    ) {
      window.alert("Please enter valid product details");
      return;
    }

    try {
      const updateAttempts = [
        () => API.put(`/products/${productId}`, payload, { validateStatus: () => true }),
        () => API.patch(`/products/${productId}`, payload, { validateStatus: () => true }),
        () => API.put("/products", { id: productId, ...payload }, { validateStatus: () => true }),
      ];

      let updated = false;
      for (const attempt of updateAttempts) {
        const res = await attempt();
        if (res.status >= 200 && res.status < 300) {
          updated = true;
          break;
        }
      }

      if (!updated) {
        window.alert("Failed to update product");
        return;
      }

      const refreshed = await loadProducts();
      setProducts(refreshed);
      cancelEditing();
      window.alert("Product updated!");
    } catch {
      window.alert("Failed to update product");
    }
  };

  return (
    <div style={styles.container}>
      <h2 style={styles.heading}>Products</h2>

      <div style={styles.grid}>
        {products.map((p) => {
          const isEditing = editingProductId === p.id;

          return (
            <div key={p.id} style={styles.card}>
              {isEditing ? (
                <>
                  <input
                    value={editForm.name}
                    onChange={(e) => setEditForm({ ...editForm, name: e.target.value })}
                    style={styles.editInput}
                    placeholder="Name"
                  />
                  <input
                    value={editForm.description}
                    onChange={(e) =>
                      setEditForm({ ...editForm, description: e.target.value })
                    }
                    style={styles.editInput}
                    placeholder="Description"
                  />
                  <input
                    type="number"
                    min="0"
                    value={editForm.price}
                    onChange={(e) => setEditForm({ ...editForm, price: e.target.value })}
                    style={styles.editInput}
                    placeholder="Price"
                  />
                  <input
                    type="number"
                    min="0"
                    value={editForm.quantity}
                    onChange={(e) => setEditForm({ ...editForm, quantity: e.target.value })}
                    style={styles.editInput}
                    placeholder="Quantity"
                  />

                  <button style={styles.saveButton} onClick={() => saveProductChanges(p.id)}>
                    Save Changes
                  </button>
                  <button style={styles.cancelButton} onClick={cancelEditing}>
                    Cancel
                  </button>
                </>
              ) : (
                <>
                  <h3>{p.name}</h3>

                  <p style={styles.desc}>{p.description}</p>

                  <p style={styles.price}>Rs {p.price}</p>

                  <p style={styles.stock}>
                    {p.quantity > 0 ? `In Stock: ${p.quantity}` : "Out of Stock"}
                  </p>

                  {!isAdmin && (
                    <>
                      <input
                        type="number"
                        min="1"
                        max={p.quantity}
                        value={selectedQuantities[p.id] || 1}
                        onChange={(e) =>
                          handleQuantityChange(p.id, e.target.value, p.quantity)
                        }
                        style={styles.quantityInput}
                        disabled={p.quantity === 0}
                      />
                      <button
                        style={styles.button}
                        disabled={p.quantity === 0}
                        onClick={() => handleBuy(p.id, p.quantity)}
                      >
                        Buy Now
                      </button>
                    </>
                  )}

                  {isAdmin && (
                    <button style={styles.editButton} onClick={() => startEditing(p)}>
                      Edit Product
                    </button>
                  )}
                </>
              )}
            </div>
          );
        })}
      </div>
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
    textAlign: "center",
    marginBottom: "20px",
  },
  grid: {
    display: "grid",
    gridTemplateColumns: "repeat(auto-fill, minmax(250px, 1fr))",
    gap: "20px",
  },
  card: {
    background: "#fff",
    padding: "20px",
    borderRadius: "12px",
    boxShadow: "0 4px 12px rgba(0,0,0,0.08)",
    textAlign: "center",
  },
  desc: {
    fontSize: "14px",
    color: "#666",
    margin: "10px 0",
  },
  price: {
    fontWeight: "bold",
    fontSize: "18px",
    color: "#2e7d32",
  },
  stock: {
    fontSize: "13px",
    marginBottom: "10px",
  },
  button: {
    padding: "10px",
    width: "100%",
    backgroundColor: "#667eea",
    color: "#fff",
    border: "none",
    borderRadius: "8px",
    cursor: "pointer",
  },
  editButton: {
    padding: "10px",
    width: "100%",
    backgroundColor: "#0d9488",
    color: "#fff",
    border: "none",
    borderRadius: "8px",
    cursor: "pointer",
  },
  saveButton: {
    padding: "10px",
    width: "100%",
    backgroundColor: "#16a34a",
    color: "#fff",
    border: "none",
    borderRadius: "8px",
    cursor: "pointer",
    marginBottom: "8px",
  },
  cancelButton: {
    padding: "10px",
    width: "100%",
    backgroundColor: "#e5e7eb",
    color: "#111827",
    border: "none",
    borderRadius: "8px",
    cursor: "pointer",
  },
  quantityInput: {
    width: "100%",
    marginBottom: "10px",
    padding: "8px",
    borderRadius: "8px",
    border: "1px solid #ccc",
  },
  editInput: {
    width: "100%",
    marginBottom: "10px",
    padding: "8px",
    borderRadius: "8px",
    border: "1px solid #ccc",
  },
};

export default Products;
