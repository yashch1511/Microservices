// src/App.js
import { BrowserRouter, Routes, Route } from "react-router-dom";
import Login from "./pages/Login";
import Products from "./pages/Products";
import Orders from "./pages/Orders";
import Navbar from "./components/Navbar";
import AddProduct from "./pages/AddProduct";
import Register from "./pages/Register";
import OrderConfirmation from "./pages/OrderConfirmation";

function App() {
  return (
    <BrowserRouter>
      <Navbar />
      <Routes>
        <Route path="/" element={<Login />} />
        <Route path="/products" element={<Products />} />
        <Route path="/orders" element={<Orders />} />
        <Route path="/register" element={<Register />} />
        <Route path="/add-product" element={<AddProduct />} />
        <Route
          path="/order-confirmation"
          element={<OrderConfirmation />}
        />

      </Routes>
    </BrowserRouter>
  );
}

export default App;
