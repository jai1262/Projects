import React from "react";
import { useDispatch, useSelector } from 'react-redux'
import styled from "styled-components"
import Navbar from "../components/Navbar"
import Announcement from "../components/Announcement"
import Newsletter from "../components/Newsletter"
import Footer from "../components/Footer"
import { mobile } from '../responsive'
import { deleteWishProduct } from "../redux/wishRedux";
import { addProduct } from "../redux/cartRedux";

const Container = styled.div``;
const Wrapper = styled.div`
    padding: 20px;
    ${mobile({padding: "10px"})}
`
const Title = styled.h1`
    margin: 20px;
    text-transform: capitalize;
    `;
const Info = styled.div`
    display: flex;
    flex-wrap: wrap;
    justify-content: space-around;
    margin: 10px;
`
const Product = styled.div`
display: flex;
justify-content: space-between;
text-transform: capitalize;
background-color: #f3f3f3;
cursor: pointer;
border-radius: 5px;
transition: all 0.5s ease &:hover {

}

&:hover{
    background-color: #f5fafa;
    transform: scale(1.1);
}
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

const Button = styled.button`
padding: 10px;
border: 1px solid ${(props) => (props.action === "add") ? '#469696' : '#e78d79'};
border-radius: 20px;
background-color: ${(props) => (props.action === "add") ? '#bde4e4' : '#fde1db'};
color: #000000;
letter-spacing: 0.5px;
cursor: pointer;
font-weight: 600;
margin: 0px 25px;

&:hover {
  background-color: #f8f4f4;
}
`

function Wishlist() {
    const wish = useSelector(state => state.wish)
    const dispatch = useDispatch();
    const handleWish = (item) => {
        dispatch(deleteWishProduct({...item}));
    }
    const handleCart = (item) => {
        dispatch(addProduct({...item, count: 1, color: item.color[0], size: item.size[0]}))
    }

    return (
        <Container>
            <Navbar />
            <Announcement />
            <Wrapper>
                <Title>YOUR WISHLIST</Title>
                <Info>
                {wish.products.map((prod) => (
                    <Product>
                        <ProductDetail>
                            <Image src={prod.img}/>
                            <Details>
                                <ProductName><b>Product : </b>{prod.title}</ProductName>
                                <ProductId><b>ID : </b> {prod._id}</ProductId>
                                <Button action="add" onClick={() => handleCart(prod)}>ADD TO CART</Button>
                                <Button action="rem" onClick={() => handleWish(prod)}>REMOVE FROM LIST</Button>
                            </Details>
                        </ProductDetail>
                    </Product>
                ))}
                </Info>
            </Wrapper>
            <Newsletter />
            <Footer />
        </Container>
    )
}

export default Wishlist