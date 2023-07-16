import { FavoriteRounded, FavoriteTwoTone, Search, ShoppingCartTwoTone } from '@mui/icons-material'
/*import red from '@mui/material/colors';*/
import React, { useState } from 'react'
import styled from 'styled-components'
import Badge from '@mui/material/Badge'
import {mobile} from "../responsive"
import { useSelector } from 'react-redux'
import { Link } from 'react-router-dom'

function Navbar() {
  const Container = styled.div`
    margin-bottom: 5px;
    height: 75px;
    box-shadow: rgba(0, 0, 0, 0.4) 5px 5px 5px;
    background-color: #fdfdfd  ;  //#f7eefc; f0f5fa
    ${mobile({height: "75px", width: "100vw"})}
    /*@media only screen and (max-width: 380px) {
      display: none;
    }*/
  `;
  const Wrapper = styled.div`
    padding: 0px 20px;
    display: flex;
    align-items: center;
    justify-content: space-between;
    color: #6d106d;
    ${mobile({padding: "0px"})}
  `
  const Left = styled.div`
    flex: 1;
    display: flex;
    align-items: center;
    font-weight: 500;
    `
  const Language = styled.span`
    font-size: 14px;
    cursor: pointer;
    ${mobile({display: "none"})}
    `
  const SearchContainer = styled.div`
  border: 1px solid #f5c5f5;
  display: flex;
  align-items: center;
  margin-left: 25px;
  padding: 5px;
  background-color: white;
  ${mobile({marginLeft: "10px", marginRight: "5px"})}
  `
  const Input = styled.input`
  border: none;
  &:focus {
    outline: none;
  }
  ${mobile({width: "50px"})}
  `
  const Center = styled.div`
    flex: 1;
    text-align: center;
  `
  const Logo = styled.h1`
  font-weight: bold;
  display: flex;
  align-items: center;
  justify-content: center;
  ${mobile({fontSize: "24px"})}
  `
  const Right = styled.div`
    flex: 1;
    display: flex;
    justify-content: flex-end;
    align-items: center;
    font-weight: 600;
    ${mobile({flex: 2, justifyContent: "center"})}
  `
  const MenuItem = styled.div`
    font-size: 15px;
    cursor: pointer;
    margin-left: 25px;
    ${mobile({fontSize: "12px", marginLeft: "10px"})}
  `
  /*const FavRounded = styled.div`
    ${mobile({display: "none"})}
  `*/
  const cartQuantity = useSelector(state => state.cart.quantity);
  const wishQuantity = useSelector(state => state.wish.quantity);
  //console.log(cart);

  const Cate = () => {
    const [category, setCategory] = useState("");

    const handleChange = (e) => {
      setCategory(e.target.value);
    }

    return (
      <SearchContainer>
        <Input type="text" placeholder="Search" value={category} key="cat" onChange={handleChange}/>
        <Link to={`/products/${category}`}>
          <Search style={{color: "#6d106d", fontSize: 16}}/>
        </Link>
      </SearchContainer>
    )
  }

  return (
    <Container>
        <Wrapper>
          <Left>
            <Language>EN</Language>
            <Cate/>
          </Left>
          <Center><Logo>Fav<i>shop</i><FavoriteRounded  style={{color: "red"}}/></Logo></Center>
          <Right>
            <Link to="/register" style={{textDecoration: "none"}}>
            <MenuItem>REGISTER</MenuItem>
            </Link>
            <Link to="/login" style={{textDecoration: "none"}}>
            <MenuItem>SIGN IN</MenuItem>
            </Link>
            <Link to="/wishlist">
              <MenuItem>
                <Badge badgeContent={wishQuantity} color="secondary">
                  <FavoriteTwoTone />
                </Badge>
              </MenuItem>
            </Link>
            <Link to="/cart">
              <MenuItem>
                <Badge badgeContent={cartQuantity} color="secondary">
                  <ShoppingCartTwoTone />
                </Badge>
              </MenuItem>
            </Link>
          </Right>
        </Wrapper>
    </Container>
  );
};

export default Navbar