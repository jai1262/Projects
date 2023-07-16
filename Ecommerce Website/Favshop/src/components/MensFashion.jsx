import React from 'react'
import styled from 'styled-components'
import { mobile, tablet } from '../responsive'
import { Link } from 'react-router-dom'

const Container = styled.div`
  margin: 5px;
  height: 80vh;
  background-color: #e5f3fa; //rgba(0, 0, 0, 0.8); dee3f1
  display: flex;
  align-items: flex-start;
  justify-content: space-around;
  ${mobile({height: "120vh"})}
  ${tablet({height: "90vh"})}
  `
const ImgContainer = styled.div`
    flex: 3;
    position: relative;
    ${mobile({display: "none"})}
    ${tablet({display: "none"})}
`
const ImgContainer1 = styled.div`
    height: 50vh;
    position: absolute;
    top: 8vh;
    left: 3vw;
    border: 5px solid #22A39F;
`
const ImgContainer2 = styled.div`
    height: 40vh;
    position: absolute;
    top: 30vh;
    left: 26vw;
    border: 5px solid #22A39F;
`
const Image = styled.img`
    width: 100%;
    height: 100%;
    object-fit: cover;
`
const InfoContainer = styled.div`
    flex: 2;
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    color: #19376D;
    margin-top: 8vh;
    ${mobile({flex: 1})}
    //background-color: white;
`
const Title = styled.h1`
    padding: 10px;
    font-size: 50px;
    //background-color: #e0b5b5;
`
const Desc = styled.p`
    font-size: 20px;
    font-weight: 400;
    letter-spacing: 1.5px;
    text-align: justify;
    padding: 10px 20px;
`
const Button = styled.button`
        padding: 12px;
        font-size: 20px;
        font-weight: 600;
        background-color: white;
        color: black;
        border: 1px solid black;
        border-radius: 5px;
        cursor: pointer;
        margin: 20px;
    &:hover {
        background-color: #e6effd;//#cbffff8f;
        color: #19376D;//#0d6580;
        border: 2px solid #0e7574;
    }
    `
function MensFashion() {
    return (
        <Container>
            <ImgContainer>
                <ImgContainer1>
                    <Image src="https://www.standout.co.uk/blog/wp-content/uploads/2021/06/shutterstock_646467355.jpg" />
                </ImgContainer1>
                <ImgContainer2>
                    <Image src="https://media.istockphoto.com/id/626085868/photo/mens-accessories.jpg?s=612x612&w=0&k=20&c=M4QqVxeUyMeChfMqOucfxtVaVMZ51g00-2tlc_Vgrx0=" />
                </ImgContainer2>
            </ImgContainer>
            <InfoContainer>
                <Title>MEN'S FASHION</Title>
                <Desc>
                    Our products were designed and developed by leading ecommerce professionals who are passionate about design and user experience,
                    creating intuitive, user friendly layouts that are optimized for success and sure to delight our customers.
                </Desc>
                <Link to="/products/men">
                    <Button>SHOP NOW {`>>`}</Button>
                </Link>
            </InfoContainer>
        </Container>
    )
}

export default MensFashion