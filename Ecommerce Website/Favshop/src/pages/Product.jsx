import {React, useEffect, useState} from 'react'
import {useLocation} from 'react-router-dom'
import styled from "styled-components"
import Navbar from '../components/Navbar'
import Announcement from '../components/Announcement'
import Newsletter from '../components/Newsletter'
import Footer from '../components/Footer'
import { Add, Remove } from '@mui/icons-material'
import { mobile } from '../responsive'
import { publicRequest } from '../requestMethods'
import {useDispatch} from 'react-redux'
import { addProduct } from '../redux/cartRedux'

const Container = styled.div`
`
const Wrapper = styled.div`
  padding: 50px;
  display: flex;
  background-color: #f3f2f0;
  ${mobile({padding: "10px", flexDirection: "column"})};
  `
const ImgContainer = styled.div`
  flex: 1;
`
const Image = styled.img`
  width: 100%;
  height: 90vh;
  object-fit: cover;
  ${mobile({height: "40vh"})};
  `
const InfoContainer = styled.div`
  flex: 1;
  padding: 0px 50px;
  ${mobile({padding: "10px"})};
`
const Title = styled.h1`
  font-weight: 400;
  text-transform: capitalize;
`
const Desc = styled.p`
  margin: 20px 0px;
  text-transform: capitalize;
`
const Price = styled.span`
  font-weight: 100;
  font-size: 40px;
`
const FilterContainer = styled.div`
  width: 50%;
  margin: 30px 0px;
  display: flex;
  justify-content: space-between;
  ${mobile({width: "100%"})};
`
const Filter = styled.div`
  display: flex;
  align-items: center;
  `
const FilterTitle = styled.span`
  font-size: 20px;
  font-weight: 200;
  `
const FilterColor = styled.div`
  width: 20px;
  height: 20px;
  border-radius: 50%;
  background-color: ${props => props.color};
  margin: 0px 5px;
  cursor: pointer;
  `
const FilterSize = styled.select`
  margin-left: 10px;
  margin-right: 5px;
  padding: 5px;
  text-transform: capitalize;
  `
const FilterSizeOption = styled.option``
const AddContainer = styled.div`
  width: 50%;
  display: flex;
  align-items: center;
  justify-content: space-between;
  ${mobile({width: "100%"})}
  `
const AmountContainer = styled.div`
  display: flex;
  align-items: center;
  font-weight: 700;
  `
const Count = styled.span`
  width: 30px;
  height: 30px;
  border-radius: 10px;
  border: 1px solid teal;
  display: flex;
  align-items: center;
  justify-content: center;
  margin: 0px 5px;
  `
const Button = styled.button`
padding: 15px;
border: 2px solid teal;
background-color: white;
cursor: pointer;
font-weight: 500;

&:hover {
  background-color: #f8f4f4;
}
`

function Product() {
  const location = useLocation();
  const id = location.pathname.split("/")[2];
  const [product, setProduct] = useState({});
  const [count, setCount] = useState(1);
  const [color, setColor] = useState("");
  const [size, setSize] = useState("");
  const dispatch = useDispatch();

  useEffect(() => {
    const getProduct = async () => {
      try {
        const res = await publicRequest.get("/products/find/"+id);
        setProduct(res.data);
      } catch {
      };
    };
    getProduct();
  }, [id]);

  const handleCount = (type) => {
    if(type === "dec") {
      count>1 && setCount(count - 1);
    } else {
      setCount(count + 1);
    }
  };

  const handleCart = () => {
    //update cart
    //axios.post
    dispatch(addProduct({...product, count, color, size}));   //action.payload is the entire object
    //console.log(...product);
  };

  return (
    <Container>
      <Navbar />
      <Announcement />
      <Wrapper>
        <ImgContainer>
          <Image src={product.img} />
        </ImgContainer>
        <InfoContainer>
          <Title>{product.title}</Title>
          <Desc>{product.desc} Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.</Desc>
          <Price>$ {product.price}</Price>
          <FilterContainer>
            <Filter>
              <FilterTitle>Color</FilterTitle>
              {product.color?.map(c => (
                <FilterColor color={c} key={c} onClick={() => setColor(c)}></FilterColor>
              ))}
            </Filter>
            <Filter>
              <FilterTitle>Size</FilterTitle>
              <FilterSize onChange={(e) => setSize(e.target.value)}>
              <FilterSizeOption key="">--size--</FilterSizeOption>
                {product.size?.map((s) => (
                  <FilterSizeOption key={s}>{s}</FilterSizeOption>
                ))}
              </FilterSize>
            </Filter>
          </FilterContainer>
          <AddContainer>
            <AmountContainer>
              <Remove onClick={() => handleCount("dec")}/>
              <Count>{count}</Count>
              <Add onClick={() => handleCount("inc")}/>
            </AmountContainer>
            <Button onClick={handleCart}>ADD TO CART</Button>
          </AddContainer>
        </InfoContainer>
      </Wrapper>
      <Newsletter />
      <Footer />
    </Container>
  )
}

export default Product