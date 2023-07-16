import React from 'react' //, { useState, useEffect }
import styled from 'styled-components'
import Navbar from '../components/Navbar'
import Announcement from '../components/Announcement'
import Footer from '../components/Footer'
import { Add, Delete, Remove } from '@mui/icons-material'
import { mobile } from '../responsive'
import { useDispatch, useSelector } from 'react-redux'
import {Link} from 'react-router-dom'       //useNavigate,
//import { userRequest } from '../requestMethods'
import { Icon } from '@mui/material'
import { deleteProduct, emptyCart, modifyCount } from '../redux/cartRedux'
import PaypalCheckout from '../components/PaypalCheckout'

//const KEY = 'pk_test_51NMRaYSEI5hS5Yw9hc5KfGkBNmK9lNqM8mkQ1CwLuy8QgD8zXIDMZcZHd8iVKxGQWEtOnpM0CxpXPncSguaZlcdX00Dexjlqfh';

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
const TopTexts = styled.div`
    ${mobile({display: "none"})};
`
const TopText = styled.span`
    text-decoration: underline;
    cursor: pointer;
    margin: 0px 10px;
    color: black;
`
const Bottom = styled.div`
    display: flex;
    justify-content: space-between;
    ${mobile({flexDirection: "column"})};
`
const Info = styled.div`
    flex: 3;
`
const Product = styled.div`
    display: flex;
    justify-content: space-between;
    text-transform: capitalize;
    ${mobile({flexDirection: "column"})}
`
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
const ProductColor = styled.div`
    width: 20px;
    height: 20px;
    border-radius: 50%;
    background-color: ${(props) => props.color};
    border: 1px solid green;
`
const ProductSize = styled.span``
const Summary = styled.div`
    flex: 1;
    border: 2px solid lightgray;
    border-radius: 10px;
    padding: 20px;
    height: 52vh;
`
const PriceDetail = styled.div`
    flex: 1;
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
`
const ProductAmountContainer = styled.div`
    display: flex;
    align-items: center;
    margin-bottom: 20px;
    border: 1px solid #F2F3FB;
`
const ProductAmount = styled.div`
    font-size: 24px;
    margin: 5px;
    ${mobile({margin: "5px 15px"})};
`
const ProductPrice = styled.div`
    font-size: 30px;
    font-weight: 200;
    margin-bottom: 20px;
    ${mobile({marginBottom: "20px"})}
`
const Hr = styled.hr`
    background-color: #eee;
    height: 2px;
    width: 90%;
    border: none;
`
const SummaryTitle = styled.h1`
    font-weight: 200;
`
const SummaryItem = styled.div`
    margin: 30px 0px;
    display: flex;
    justify-content: space-between;
    font-weight: ${props => props.type === "total" && "600"};
`
const SummaryItemText = styled.span``
const SummaryItemPrice = styled.span``
const Button = styled.button`
    width: 100%;
    padding: 10px;
    background-color: #e4b257;
    border: none;
    border-radius: 4px;
    color: #145314;
    font-size: 16px;
    font-weight: 600;
    cursor: pointer;
    margin: 10px 0px;
`

function Cart2() {
    const cart = useSelector((state) => state.cart);
    console.log(cart.products);
    const wish = useSelector((state) => state.wish);
    console.log(wish.quantity);
    //const navigate = useNavigate();

    const dispatch = useDispatch();
    const handleCart = (item) => {
        dispatch(deleteProduct({...item}))
    }
    const handleCount = (item) => {
        dispatch(modifyCount({...item}))
    }
    const empCart = () => {
        dispatch(emptyCart({}))
    }

  return (
    <Container>
        <Navbar />
        <Announcement />
        <Wrapper>
            <Title>YOUR BAG</Title>
            <Top>
                <Link to="/">
                <TopButton>CONTINUE SHOPPING</TopButton>
                </Link>
                <TopTexts>
                    <Link to="/cart">
                    <TopText>Shopping Bag ({cart.quantity})</TopText>
                    </Link>
                    <Link to="/wishlist">
                    <TopText>Your Wishlist ({wish.quantity})</TopText>
                    </Link>
                </TopTexts>
                <Link to="/yourorders">
                    <TopButton type="filled">YOUR ORDERS</TopButton>
                </Link>
            </Top>
            <Bottom>
                <Info>
                    {cart.products.map(prod => (
                        <div>
                        <Product>
                            <ProductDetail>
                            <Image src={prod.img}/>
                            <Details>
                                <ProductName><b>Product : </b>{prod.title}</ProductName>
                                <ProductId><b>ID : </b> {prod._id}</ProductId>
                                <ProductColor color={prod.color}/>
                                <ProductSize><b>Size : </b> {prod.size}</ProductSize>
                            </Details>
                        </ProductDetail>
                        <PriceDetail>
                            <ProductAmountContainer>
                                <Add onClick={() => handleCount({mod: "inc", id: prod._id})}/>
                                <ProductAmount>{prod.count}</ProductAmount>
                                <Remove onClick={() => handleCount({mod: "dec", id: prod._id})}/>
                            </ProductAmountContainer>
                            <ProductPrice>${prod.price * prod.count}</ProductPrice>
                            <Icon onClick={() => handleCart(prod)} style={{cursor: 'pointer'}}>
                                <Delete />
                            </Icon>
                        </PriceDetail>
                        </Product>
                        <Hr />
                        </div>
                    ))}
                </Info>
                <Summary>
                    <SummaryTitle>ORDER SUMMARY</SummaryTitle>
                    <SummaryItem>
                        <SummaryItemText>Subtotal</SummaryItemText>
                        <SummaryItemPrice>$ {cart.total}</SummaryItemPrice>
                    </SummaryItem>
                    <SummaryItem>
                        <SummaryItemText>Estimated Shipping</SummaryItemText>
                        <SummaryItemPrice>$ 5.90</SummaryItemPrice>
                    </SummaryItem>
                    <SummaryItem>
                        <SummaryItemText>Shipping Discount</SummaryItemText>
                        <SummaryItemPrice>$ -5.90</SummaryItemPrice>
                    </SummaryItem>
                    <SummaryItem type="total">
                        <SummaryItemText>Total</SummaryItemText>
                        <SummaryItemPrice>$ {cart.total}</SummaryItemPrice>
                    </SummaryItem>
                    <PaypalCheckout amt={cart.total}/>
                    <Button onClick={empCart}>Empty Cart</Button>
                </Summary>
            </Bottom>
        </Wrapper>
        <Footer />
    </Container>
  )
}

export default Cart2
