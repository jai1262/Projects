import React from 'react'
import styled from 'styled-components'
import { mobile } from '../responsive'
import { Link } from 'react-router-dom'

const Container = styled.div`
  flex: 1;
  margin: 15px 5px;
  height: 80vh;
  position: relative;
  background-image: url("https://pipeline-theme-fashion.myshopify.com/cdn/shop/files/clothing-hero-flipped.jpg");  //?v=1666145888&width=1920
  background-repeat: no-repeat;
  background-size: cover;
  display: flex;
  align-items: center;
  justify-content: center;
  ${mobile({height: "50vh"})}
  //opacity: 0.9;
  `
const InfoContainer = styled.div`
    width: 50vw;
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    color: white;
    //background-color: white;
`
const Title = styled.h1`
    padding: 10px;
    font-size: 50px;
    text-align: center;
    font-weight: 500;
    font-family: Serif;
    letter-spacing: 4px;
    margin-bottom: 10px;
    background-color: rgba(224, 181, 181, 0.1);
    ${mobile({fontSize: "25px", letterSpacing: "2px", backgroundColor: "none"})}
`
const Desc = styled.p`
    font-size: 20px;
    font-weight: 600;
    letter-spacing: 1.5px;
    text-align: justify;
    padding: 10px 20px;
    background-color: rgba(224, 181, 181, 0.2);
    ${mobile({fontSize: "10px", padding: "5px"})}
`
const Button = styled.button`
        padding: 15px;
        font-size: 12px;
        font-weight: 500;
        letter-spacing: 1.5px;
        background-color: black;//#cbffff8f;
        color: white;   //#0d6580;
        border: none;   //1px solid black;
        border-radius: 5px;
        cursor: pointer;
        margin: 20px;
    &:hover {
        background-color: white;
        color: black;   //2px solid #fa9ff5;
    }
    ${mobile({padding: "10px"})}
`
function WomensFashion() {
    return (
        <Container>
            <InfoContainer>
                <Desc>A CONCIOUS WARDROBE</Desc>
                <Title>TIMELESS STYLE<br></br>SUSTAINABLE DESIGN</Title>
                <Link to="/products/women">
                    <Button>VIEW PRODUCTS</Button>
                </Link>
            </InfoContainer>
        </Container>
    )
}

export default WomensFashion