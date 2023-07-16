import React, { useState } from 'react' //, { useState, useEffect }
import styled from 'styled-components'
import Navbar from '../components/Navbar'
import Announcement from '../components/Announcement'
import Footer from '../components/Footer'
import { mobile } from '../responsive'
import { useEffect } from 'react'
import { userRequest } from '../requestMethods' //publicRequest,
import { useSelector } from 'react-redux'
import { Link } from 'react-router-dom'

const Container = styled.div`
`
const Wrapper = styled.div`
    padding: 20px;
    ${mobile({padding: "10px"})}
`
const Title = styled.h1`
    font-weight: 500;
    text-align: center;
`
const Top = styled.div`
    display: flex;
    align-items: center;
    justify-content: space-between;
    padding: 20px;
`
const TopButton = styled.button`
    padding: 10px;
    font-weight: 600;
    cursor: pointer;
    border: ${props => props.type === "filled" && "none"};
    background-color: ${props => props.type === "filled" ? "black" : "white"};
    color: ${props => props.type === "filled" ? "white" : "black"};
`
const Info = styled.div`
    display: flex;
    align-items: center;
    justify-content: center;
    flex-direction: column;
`
const Order = styled.div`
    width: 65vw;
    border: 2px solid black;
    margin: 15px;
`
const OrderTitle = styled.div`
    background-color: #e8f1ed;
    padding: 5px;
    font-size: 20px;
    display: flex;
    align-items: center;
    justify-content: space-around;
    border-bottom: 2px solid black;
`
const Product = styled.div`
    display: flex;
    //flex-wrap: wrap;
    justify-content: space-around;
    text-transform: capitalize;
    padding: 20px;
    ${mobile({flexDirection: "column"})}
`
/*
const ProductDetail = styled.div`
    flex: 2;
    display: flex;
`
const Details = styled.div`
    padding: 20px;
    display: flex;
    flex-direction: column;
    justify-content: space-around;
`
const Image = styled.img`
    width: 200px;
`
const ProductName = styled.div``
const ProductId = styled.span``
*/
function Orders() {
    const [orders, setOrders] = useState([]);
    const user = useSelector(state => state.user.currentUser)
    useEffect(() => {
        const getOrders = async () => {
            try {
                const res = await userRequest.get(`/orders/find/${user._id}`);
                setOrders(res.data);
            } catch(err) {}
        };
        getOrders();
    }, [user]);
    console.log(orders);

    /*const getProduct = async(id) => {
        const res = await publicRequest.get(`/products/find/${id}`).then(value => value);
        return res.data;
    }*/

    return (
        <Container>
            <Navbar />
            <Announcement />
            <Wrapper>
                <Title>YOUR ORDERS</Title>
                <Top>
                    <Link to="/">
                        <TopButton>CONTINUE SHOPPING</TopButton>
                    </Link>
                    <Link to="/cart">
                        <TopButton type="filled">BACK TO CART</TopButton>
                    </Link>
                </Top>
                <Info>
                    {orders.map((order) => (
                        <Order>
                            <OrderTitle>
                                <p><b>ORDER ID : </b>{order._id}</p>
                                <p><b>TOTAL AMOUNT : </b>${order.amount}</p>
                            </OrderTitle>
                            {order.products.map((prod) => {
                            //const prodDetail = await getProduct(prod.productId);
                            //console.log(prodDetail);
                            return (<Product>
                                    <p><b>Prod Id : </b>{prod.productId}</p>
                                    <p><b>Quantity : </b>{prod.quantity}</p>
                                   </Product>
                            )})}
                    </Order>
                    ))}
                </Info>
            </Wrapper>
            <Footer />
        </Container>
    )
}

export default Orders

/*<ProductDetail>
                                        <Image src={prod.img}/>
                                        <Details>
                                            <ProductName><b>Product : </b>{prod.title}</ProductName>
                                            <ProductId><b>ID : </b> {prod._id}</ProductId>
                                        </Details>
                                    </ProductDetail>*/