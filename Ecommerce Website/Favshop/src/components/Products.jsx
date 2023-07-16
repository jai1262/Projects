import {React, useState, useEffect} from 'react'
//import {popularProducts} from "./data"
import styled from 'styled-components'
import Product from './Product'
import axios from "axios";

const Container = styled.div`
padding: 20px;
display: flex;
flex-wrap: wrap;
justify-content: space-between;
`

function Products(props) {
  //console.log(props);
  const [products, setProducts] = useState([]);
  const [filteredProducts, setFilteredProducts] = useState([]);

  useEffect(() => {
    const getProducts = async () => {
      try {
        const res = await axios.get((props.cat && props.cat !== "all") ? `http://localhost:5000/api/products?category=${props.cat}` : "http://localhost:5000/api/products/");
        console.log(res.data);
        setProducts(res.data);
      } catch(err) {}
    };
    getProducts();
  }, [props.cat]);

  useEffect(() => {
    props.cat && setFilteredProducts(
      products.filter(item => Object.entries(props.filter).every(([key, value]) =>
      item[key].includes(value)
      ))
    );
  }, [products, props.cat, props.filter]);

 /* useEffect(() => {
    props.gen && filteredProducts && setFilteredProducts(
      products.filter(item => item["categories"].includes(props.gen)))
  }, [props.gen, products]);*/

  useEffect(() => {
    if(props.sort === "newest") {
      setFilteredProducts(prev =>
        [...prev].sort((a,b) => a.createdAt - b.createdAt));
    } else if(props.sort === "asc") {
      setFilteredProducts(prev =>
        [...prev].sort((a,b) => a.price - b.price));
    } else {
      setFilteredProducts(prev =>
        [...prev].sort((a,b) => b.price - a.price));
    }
  }, [props.sort]);

  return (
    <Container>
        {props.cat === "all"
        ? products.map((item) => (
          <Product item={item} key={item.id} />))
        : props.cat
        ? filteredProducts.map((item) => (
            <Product item={item} key={item.id} />))
        : products.filter(item => item["categories"].includes("general")).map((item) => (
          <Product item={item} key={item.id} />))
        }
    </Container>
  )
}

export default Products