import React from 'react'
import { Link } from 'react-router-dom'
import styled from 'styled-components'

const Container = styled.div`
  margin: 15px;
  height: 20vh;
  display: flex;
  align-items: center;
  justify-content: center;
`
const Button = styled.button`
        padding: 20px;
        font-size: 16px;
        font-weight: 500;
        letter-spacing: 1.5px;
        background-color: #242542;//#cbffff8f;
        color: white;   //#0d6580;
        border: none;   //1px solid black;
        border-radius: 5px;
        cursor: pointer;
        margin: 20px;
        transition: all 0.5s ease;
    &:hover {
        background-color: white;
        color: black;   //2px solid #fa9ff5;
        border: 2px solid black;
        transform: scale(1.1);
    }
`

function AllProds() {
    return (
        <Container>
            <Link to="/products/all">
                <Button>VIEW ALL PRODUCTS {`=>`}</Button>
            </Link>
        </Container>
    )
}

export default AllProds