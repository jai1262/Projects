import {React, useState} from 'react'
import {useLocation} from 'react-router'
import styled from "styled-components"
import Navbar from "../components/Navbar"
import Announcement from "../components/Announcement"
import Products from "../components/Products"
import Newsletter from "../components/Newsletter"
import Footer from "../components/Footer"
import { mobile } from '../responsive'

const Container = styled.div``;
const Title = styled.h1`
    margin: 20px;
    text-transform: capitalize;
    `;
const FilterContainer = styled.div`
    display: flex;
    justify-content: space-between;
    `;
const Filter = styled.div`
    margin: 20px;
    ${mobile({width: "0px 20px", display: "flex", flexDirection: "column"})};
    `;
const FilterText = styled.span`
    font-size: 20px;
    font-weight: 600;
    margin-right: 20px;
    ${mobile({marginRight: "0px"})};
    `
const Select = styled.select`
    padding: 10px;
    margin-right: 20px;
    ${mobile({margin: "10px 0px"})};
    `
const Option = styled.option``

function ProductList() {
  const location = useLocation();
  console.log(location.pathname.split("/")[2]);     //prints category alone
  const cat = location.pathname.split("/")[2];
  const [filter, setFilter] = useState({});
  const [sort, setSort] = useState("newest");
  //const [gender, setGender] = useState("");

  const handleFilters = (event) => {
    const value = event.target.value;
    setFilter({
        ...filter,
        [event.target.name]: value.toLowerCase(),
    });
  };

  console.log(filter);

  return (
    <Container>
        <Navbar />
        <Announcement />
        <Title>{cat}</Title>
        <FilterContainer>
            <Filter>
            <FilterText>Filter Products: </FilterText>
            <Select name="color" onChange={handleFilters}>
                <Option disabled selected>
                    --Color--
                </Option>
                <Option>White</Option>
                <Option>Black</Option>
                <Option>Red</Option>
                <Option>Blue</Option>
                <Option>Pink</Option>
                <Option>Gray</Option>
            </Select>
            <Select name="size" onChange={handleFilters}>
                <Option disabled selected>
                    --Size--
                </Option>
                <Option>XS</Option>
                <Option>S</Option>
                <Option>M</Option>
                <Option>L</Option>
                <Option>XL</Option>
                <Option>XXL</Option>
                <Option>Free</Option>
            </Select>
            </Filter>
            <Filter>
            <FilterText>Sort Products: </FilterText>
            <Select onChange={e => setSort(e.target.value)}>
                <Option value="newest">Newest</Option>
                <Option value="asc">Price (asc)</Option>
                <Option value="desc">Price (desc)</Option>
            </Select>
            </Filter>
        </FilterContainer>
        <Products cat={cat} filter={filter} sort={sort} />
        <Newsletter />
        <Footer />
    </Container>
  )
}

export default ProductList

/*
<Select name="gen" onChange={(e) => setGender(e.target.value.toLowerCase())}>
                <Option disabled selected>
                    --Gender--
                </Option>
                <Option>Men</Option>
                <Option>Women</Option>
            </Select>
*/