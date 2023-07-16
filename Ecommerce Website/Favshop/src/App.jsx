import './App.css';
import Product from './pages/Product';
import Home from './pages/Home';
import ProductList from './pages/ProductList';
import Register from './pages/Register';
import Login from './pages/Login';
//import Cart from './pages/Cart';
import Wishlist from './pages/Wishlist'
//import Payapp from './payment/Payapp';
import {Routes, Route, Navigate} from "react-router-dom";
import Success from './pages/Success';
import { useSelector } from 'react-redux';
import Cart from './pages/Cart';
import Orders from './pages/Orders';

function App() {
  const user = useSelector((state) => state.user.currentUser);      //temporary variable
  return (
        <Routes>
          <Route exact path="/" element={<Home />}>
          </Route>
          <Route path="/products/:category" element={<ProductList />}>
          </Route>
          <Route path="/products" element={<ProductList />}>
          </Route>
          <Route path="/product/:id" element={<Product />}>
          </Route>
          <Route path="/cart" element={<Cart />}>
          </Route>
          <Route path="/wishlist" element={<Wishlist />}>
          </Route>
          <Route path="/success" element={<Success />}>
          </Route>
          <Route path="/yourorders" element={<Orders />}>
          </Route>
          <Route path="/login" element={user ?  <Navigate to="/" replace/> : <Login /> }>
          </Route>
          <Route path="/register" element={user ?  <Navigate to="/" replace/> :<Register />}>
          </Route>
        </Routes>
  );
}

export default App;
